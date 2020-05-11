package com.robot2.secret.service.impl;

import com.robot2.secret.dao.SecretHandleDao;
import com.robot2.secret.entity.BaseInfo;
import com.robot2.secret.entity.Secret;
import com.robot2.secret.service.SecretHandleService;
import com.robot2.secret.tool.KeyTool;
import com.robot2.secret.tool.TimeTool;
import com.robot2.secret.tool.resultool.R;
import com.robot2.secret.tool.RedisTool;
import com.robot2.secret.tool.SendEmailTool;
import com.robot2.secret.tool.resultool.Renum;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * (RobotSecret)表服务实现类
 *
 * @author qiemengyan
 * @since 2020-04-22 15:30:05
 */
@Service("robotSecretService")
public class SecretHandleServiceImpl implements SecretHandleService {
    @Resource
    private SendEmailTool sendEmailTool;
    @Resource
    private RedisTool redisTool;
    @Resource
    private SecretHandleDao secretHandleDao;

    public SecretHandleServiceImpl() {
    }

    /**
     * @param secret 秘钥内容，使用redis进行调用次数验证，mysql进行有效期认证
     * @return 如果验证成功，返回剩余调用调用次数。
     */
    @Override
    @Transactional
    public R check(String secret) {
        //截取第一个字符，判断是平台、机器人还是管理员
        String type = secret.substring(0, 1);
        //首先判断秘钥是否在黑名单
        boolean b = this.redisTool.hHasKey("secret_blacklist", secret);
        if(b){
            return R.setResult(Renum.INTERFACE_NOT_AVALIABLE_TIME);
        }
        //去redis里查询剩余调用次数。
        Object obj = checkNumber(secret, type);
        //根据秘钥获取基础信息
        BaseInfo baseInfo = getBaseInfo(secret, type);
        if (obj != null && baseInfo !=null) {
            return checkTime(obj, secret, type, baseInfo);
        } else {
            //如果redis里没有数据，则可能是redis数据被清空或者本来就没有注册
            //处理redis被清空的操作。去查数据库是否有此数据。如果有，则返回查询接口剩余调用次数失败
            if (baseInfo == null) {
                return R.setResult(Renum.AUTHORITY_FAILE);
            } else {
                return R.setResult(Renum.INTERFACE_NOT_NUMBERS_FAILE);
            }

        }


    }

    //检查使用秘钥调用接口剩余次数
    private Object checkNumber(String secret, String type) {
        //先判断秘钥是否过期。
        Object obj = null;
        switch (type) {
            case "p":
                obj = this.redisTool.hget("robot_secret_platform", secret);
                break;
            case "r":
                obj = this.redisTool.hget("robot_secret_robot", secret);
                break;
            case "u":
                obj = this.redisTool.hget("robot_secret_user", secret);
                break;
        }
        return obj;
    }

    //获取基础信息
    private BaseInfo getBaseInfo(String secret, String type) {
        BaseInfo baseInfo = null;
        switch (type) {
            case "p":
                baseInfo = this.secretHandleDao.queryRobotInfo_secret_platform(secret);
                break;
            case "r":
                baseInfo = this.secretHandleDao.queryRobotInfo_secret_robot(secret);

                break;
            case "u":
                baseInfo = this.secretHandleDao.queryRobotInfo_secret_user(secret);

                break;
        }
        return baseInfo;
    }

    //获取基础信息+检查秘钥有效期
    private R checkTime(Object obj, String secret, String type, BaseInfo baseInfo) {
        String finishDateTime = null;
        Integer number = (Integer) obj;
        if (number > 0) {
            switch (type) {
                case "p":
                    finishDateTime = this.secretHandleDao.queryFinishDateTime_secret_platform(secret);
                    break;
                case "r":
                    finishDateTime = this.secretHandleDao.queryFinishDateTime_secret_robot(secret);

                    break;
                case "u":
                    finishDateTime = this.secretHandleDao.queryFinishDateTime_secret_user(secret);
                    break;
            }
            LocalDateTime fintime = TimeTool.stringToDate(finishDateTime);
            int s = TimeTool.DateTimeCompare(fintime, LocalDateTime.now());
            //说明没有过期
            if (s != 2) {
                Map<String, Object> map = new HashMap<>();
                map.put("restNum", number);
                map.put("finishTime", finishDateTime);
                map.put("deviceId", baseInfo.getDeviceId());
                return R.ok()
                        .message("验证通过")
                        .data(map);
            } else {
                return R.setResult(Renum.INTERFACE_NOT_AVALIABLE_TIME);
            }
        } else {
            return R.setResult(Renum.INTERFACE_NOT_AVALIABLE_NUMBER);
        }
    }


    /**
     * 注册，分为平台、机器人、管理员。注册之后将信息存入数据库，返回注册结果
     *
     * @param baseInfo,type=1，表示平台；type=2,表示机器人；type=3表示管理员
     * @return 返回秘钥信息。
     */
    @Override
    @Transactional
    public R register(BaseInfo baseInfo) {
        //创建证书，时效为一年。
        //将证书秘钥和基本信息存入MySQL数据库,将秘钥存入redis缓存库
        //data:加密主体；type:生效年月;number:具体生效时间；dataType:主体类型
        Secret secret = null;
        R r = null;
        switch (baseInfo.getType()) {
            case "1": {
                //拼接加密主体data：平台名称/机器人名称/管路员名称+注册地址+email邮箱
                String stringBuffer = baseInfo.getName() +
                        baseInfo.getAddress() +
                        baseInfo.getEmail() +
                        "robot";
                secret = KeyTool.create(stringBuffer, 1, 1, "p");
                if (isNotPool(secret.getSecret(), "1")) {
                    //将秘钥存入mysql的秘钥表
                    this.secretHandleDao.insert_secret_platform(secret);
                    //将id和基本信息存入mysql的info表
                    baseInfo.setSecretId(secret.getId());
                    //随机生成生成设备ID存入mysql库
                    baseInfo.setDeviceId(getRandom(22));
                    this.secretHandleDao.insert_info_platform(baseInfo);
                    //将秘钥存入缓存库,并设置请求次数
                    this.redisTool.hset("robot_secret_platform", secret.getSecret(), 1000);
                } else {
                    r = R.setResult(Renum.REGISTER_FAILE);
                }
                break;
            }
            case "2": {
                String stringBuffer = baseInfo.getName() +
                        baseInfo.getAddress() +
                        baseInfo.getEmail() +
                        "robot";
                secret = KeyTool.create(stringBuffer, 1, 1, "r");
                if (isNotPool(secret.getSecret(), "2")) {
                    //将秘钥存入mysql的秘钥表
                    this.secretHandleDao.insert_secret_robot(secret);
                    //将id和基本信息存入mysql的info表
                    baseInfo.setSecretId(secret.getId());
                    //随机生成生成设备ID存入mysql库
                    baseInfo.setDeviceId(getRandom(22));
                    this.secretHandleDao.insert_info_robot(baseInfo);
                    //将秘钥存入缓存库
                    this.redisTool.hset("robot_secret_robot", secret.getSecret(), 1000);
                } else {
                    r = R.setResult(Renum.REGISTER_FAILE);

                }
                break;
            }
            case "3": {
                String stringBuffer = baseInfo.getUsername() +
                        baseInfo.getAddress() +
                        baseInfo.getEmail() +
                        "robot";
                secret = KeyTool.create(stringBuffer, 1, 1, "u");
                System.out.println(secret);
                if (isNotPool(secret.getSecret(), "3")) {
                    //将秘钥存入mysql的秘钥表
                    this.secretHandleDao.insert_secret_user(secret);
                    //将id和基本信息存入mysql的info表
                    baseInfo.setSecretId(secret.getId());
                    //随机生成生成设备ID存入mysql库
                    baseInfo.setDeviceId(getRandom(22));
                    this.secretHandleDao.insert_info_user(baseInfo);
                    //将秘钥存入缓存库
                    this.redisTool.hset("robot_secret_user", secret.getSecret(), 1000);
                } else {
                    r = R.setResult(Renum.REGISTER_FAILE);
                }
                break;
            }
        }
        assert secret != null;
        //将秘钥发送至邮箱
        if (r == null) {
            this.sendEmailTool.sendEmail(secret.getSecret(), baseInfo);
            Map<String, Object> map = new HashMap<>();
            map.put("info", baseInfo);
            map.put("secret", secret);
            return R.ok().data(map).message("证书已生成，请前往邮箱查收！");
        }
        return r;
    }

    /**
     * 生成一个22位随机数，伪装为设备ID
     *
     * @param len 数列长度
     * @return 返回拼接之后的字符串
     */
    public String getRandom(int len) {
        Random r = new Random();
        StringBuilder rs = new StringBuilder();
        for (int i = 0; i < len; i++) {
            rs.append(r.nextInt(10));
        }
        return rs.toString();
    }

    /**
     * @param secret 秘钥
     * @param type   注册类型
     * @return 存在该秘钥返回true;不存在返回false;
     */
    public boolean isNotPool(String secret, @NotNull String type) {
        boolean b = false;
        //先去redis里查，如果没有，再去mysql查。如果还没有，证明可以注册，返回true；否则，返回false
        //如果b==false，则让if==true，执行代码。否则不执行if
        switch (type) {
            case "1":
                b = this.redisTool.hHasKey("robot_secret_platform", secret);
                if (!b) {
                    b = this.secretHandleDao.ifSercetExist_platform(secret);
                }
                break;
            case "2":
                b = this.redisTool.hHasKey("robot_secret_robot", secret);
                if (!b) {
                    b = this.secretHandleDao.ifSercetExist_robot(secret);
                }
                break;
            case "3":
                b = this.redisTool.hHasKey("robot_secret_user", secret);
                if (!b) {
                    b = this.secretHandleDao.ifSercetExist_user(secret);
                }
                break;
        }

        return !b;

    }

    /**
     * 校验服务调用次数
     *
     * @param secret 秘钥
     * @return 调用剩余次数,没有次数返回0，有次数返回相应次数；如果该秘钥在黑名单中，则返回-1
     */
    @Override
    public int checkSecretNumber(String secret) {
        String type = secret.substring(0, 1);
        boolean b = this.redisTool.hHasKey("secret_blacklist", secret);
        if(b){
            return -1;
        }
        Object obj;
        int number = 0;
        switch (type) {
            case "p":
                obj = this.redisTool.hget("robot_secret_platform", secret);
                if ((Integer) obj != 0) {
                    number = (Integer) obj - 1;
                    this.redisTool.hset("robot_secret_platform", secret, number);
                }
                break;
            case "r":
                obj = this.redisTool.hget("robot_secret_robot", secret);
                if ((Integer) obj != 0) {
                    number = (Integer) obj - 1;
                    this.redisTool.hset("robot_secret_robot", secret, number);
                }
                break;
            case "u":
                obj = this.redisTool.hget("robot_secret_user", secret);
                if ((Integer) obj != 0) {
                    number = (Integer) obj - 1;
                    this.redisTool.hset("robot_secret_user", secret, number);
                }
                break;
        }
        return number;
    }

    /**
     * 定时任务。每天执行一次。判断当前在线机器人是否管控到期。
     */
    @Override
    @Transactional

    public void checkRobotSecretTime() {

        //查询所有当前在线机器人，返回他们的秘钥和过期时间
        List<Secret> secretList;
        secretList =this.secretHandleDao.queryOnlineFinishTime_robot();
        onlineProcess(secretList,"robot");
    }

    /**
     * 定时任务。每天执行一次。判断当前在线平台是否管控到期。
     */
    @Override
    @Transactional

    public void checkPlatformSecretTime() {
        //查询所有当前在线机器人，返回他们的秘钥和过期时间
        List<Secret> platformSecretList;
        platformSecretList=this.secretHandleDao.queryOnlineFinishTime_platform();
        onlineProcess(platformSecretList,"platform");
    }

    private void onlineProcess(List<Secret> secretList, String name) {
        if(secretList.size()!=0) {
            for (Secret secret :secretList){
                LocalDateTime finishtime = TimeTool.stringToDate(secret.getFinishTime());
                int s = TimeTool.DateTimeCompare(finishtime, LocalDateTime.now());
                //如果等于2说明已经到期了。將其加入到redis的黑名单中。并在mysql中让其下线。

                if (s == 2) {
                    this.redisTool.hset("secret_blacklist", secret.getSecret(), secret.getFinishTime());
                    if("platform".equals(name)){
                        this.secretHandleDao.offline_platform(secret.getId());
                    }else if("robot".equals(name)){
                        this.secretHandleDao.offline_robot(secret.getId());
                    }


                }
            }
        }
    }

}