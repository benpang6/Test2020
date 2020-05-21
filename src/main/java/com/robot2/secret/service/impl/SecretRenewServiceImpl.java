package com.robot2.secret.service.impl;

import com.robot2.secret.dao.SecretHandleDao;
import com.robot2.secret.dao.SecretRenewDao;
import com.robot2.secret.entity.Secret;
import com.robot2.secret.service.SecretRenewService;
import com.robot2.secret.tool.RedisTool;
import com.robot2.secret.tool.TimeTool;
import com.robot2.secret.tool.resultool.R;
import com.robot2.secret.tool.resultool.Renum;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Map;

@Service("SecretRenewService")
public class SecretRenewServiceImpl  implements SecretRenewService {
    @Resource
    private SecretRenewDao secretRenewDao;
    @Resource
    private RedisTool redisTool;

    /**
     * 续费功能
     *
     * @param map
     * String type: 平台 p  机器人 r
     * String deviceId
     * String 续费时常  例如：06y  03m  永久
     * @return
     */
    @Override
    public R insertTime(Map<String, String> map) {
        if(map.isEmpty()){
            return R.setResult(Renum.NULL_POINT);
            //throw new RuntimeException("没有接到任何数据");
        }
        if(map.size()!=3){
            return R.error().message("请补充完整字段信息");
        }
        String deviceId = map.get("deviceId");    //deviceId
        if(!deviceId.matches("[\\d]{22}")){
            return R.error().message("deviceId不正确");
        }
        String dataType = map.get("type");       //标识数据是机器人/平台
        String formInfoName = "";
        String formSecretName = "";
        /*
         *判断传入的是平台/机器人
         */
        switch(dataType){
            case "p":
                formInfoName = "robot_info_platform";
                formSecretName = "robot_secret_platform";
                break;
            case "r":
                formInfoName = "robot_info_robot";
                formSecretName = "robot_secret_robot";
                break;
            default:
                if(StringUtils.isEmpty(dataType)){
                    return R.error().message("type不能为空");
                }
                return R.error().message("请传入正确的类型");
        }
        int addMonth= 0;
        String addTime = map.get("time");         //增加续费时间
        /*
         * 判断续费时常
         */
        switch(addTime.length()){
            case 3:
                String num = addTime.substring(0,2);    //数量：n年，n月
                String type = addTime.substring(2);     //年/月
                for(int i=0;i<num.length();i++){
                    if (!Character.isDigit(num.charAt(i))) {
                        return R.error().message("请传入正确的数量，数量必须是阿拉伯数字");
                    }
                }
                int timeNum = Integer.parseInt(num);
                /*
                 * 对年/月进行处理
                 */
                switch (type.toUpperCase()){
                    case "M":
                        if(timeNum<1||timeNum>11){
                            return R.error().message("请输入1-11个月");
                        }
                        addMonth = addMonth+timeNum;
                        break;
                    case "Y":
                        if(timeNum<1||timeNum>10){
                            return R.error().message("请输入1-10年");
                        }
                        addMonth = addMonth+timeNum*12;
                        break;
                    default:
                        //System.out.println(type);
                        return R.error().message("请传入正确的年或月");
                }
                break;
            case 2:
                if(!addTime.equals("永久")){
                    return R.error().message("请传入”永久“字段");
                }
               addMonth =  addMonth+9999*12;                   //如果是永久：添加9999年
                break;
            default:
                return R.error().message("请传入正确的时间");
        }
        /*
         * 对数据进行处理
         * @Param deviceId
         * @Param addMonth
         */
        return timeHandle(deviceId,addMonth,formInfoName,formSecretName);

    }

    /**
     * 重置秘钥次数
     * @param deviceId
     * @param addMonth
     */
    private R timeHandle(String deviceId, int addMonth, String formInfoName, String formSecretName) {
        /*
         * 通过deviceId查询数据库的info表和secret表的数据
         * 获得secret
         * 获得到期时间
         */
        String finishTime = "";
        Secret s = secretRenewDao.selectSecretByDeviceId(deviceId,formInfoName,formSecretName);
        try{
            finishTime = s.getFinishTime();  //获取到期时间
        }catch(NullPointerException e){
            return R.error().message("该数据已不存在！");
        }

        String secret = s.getSecret();          //获取密钥
        LocalDateTime fintime = TimeTool.stringToDate(finishTime);
        int t = TimeTool.DateTimeCompare(fintime, LocalDateTime.now());
        //说明过期
        if(t==2){
            finishTime = TimeTool.addDateTime(LocalDateTime.now(),addMonth,2);
        }else{//没过期
            finishTime = TimeTool.addDateTime(fintime,addMonth,2);
        }
            int row = secretRenewDao.updateSecetByDeviceId(secret,formSecretName,finishTime);
            if(row==1){
                return R.ok().message("成功续费，过期时间是："+finishTime);
            }else{
               return R.error().message("数据删除失败");
            }
    }

    /**
     *
     * @param map
     * @Param    String deviceId
     * @Param    String type  平台 p  机器人r
     * @Param    String times  续费次数
     * @return
     */
    @Override
    public R insertTimes(Map<String,String> map) {
        if(map.isEmpty()){
            return R.setResult(Renum.NULL_POINT);
        }
        if(map.size()!=3){
            return R.error().message("请补充完整字段信息");
        }
        String deviceId = map.get("deviceId");    //deviceId
        if(!deviceId.matches("[\\d]{22}")){
            return R.error().message("deviceId不正确");
        }
        String dataType = map.get("type");       //标识数据是机器人/平台
        String formInfoName = "";
        String formSecretName = "";
        /*
         *判断传入的是平台/机器人
         */
        switch(dataType){
            case "p":
                formInfoName = "robot_info_platform";
                formSecretName = "robot_secret_platform";
                break;
            case "r":
                formInfoName = "robot_info_robot";
                formSecretName = "robot_secret_robot";
                break;
            default:
                if(StringUtils.isEmpty(dataType)){
                    return R.error().message("type不能为空");
                }
                return R.error().message("请传入正确的类型");
        }
        String times = map.get("times");  //增加的次数
        if(!times.matches("[\\d]{1,8}")){
            return R.error().message("请传入1-8位的数字");
        }
        int t = Integer.parseInt(times);
        if(t<1||t>10000000){
            return R.error().message("请输入1-1000万的数字");
        }
        return timesHandle(formInfoName,formSecretName,t,deviceId);
    }

    /**
     * 数据处理
     * @param formInfoName     info表名
     * @param formSecretName   secret表名
     * @param t    续费次数
     * @return
     */
    private R timesHandle(String formInfoName, String formSecretName, int t, String deviceId) {
        Secret s= secretRenewDao.selectSecretByDeviceId(deviceId, formInfoName, formSecretName);
        String secret = "";
        try{
             secret = s.getSecret();
        }catch (NullPointerException e){
            return R.error().message("数据已经不存在");
        }

        int sum= 0;
        if(redisTool.hHasKey("secret_blacklist",secret)){
            //删除黑名单的信息
            redisTool.hdel("secret_blacklist",secret);
            redisTool.hset(formSecretName,secret,t);
            sum = t;
            if((Integer)redisTool.hget(formSecretName,secret)!=sum){
                return R.error().message("添加失败");
            }
        }else {
            Integer value = (Integer) redisTool.hget(formSecretName,secret);
            //int t1 = Integer.parseInt(value);
            sum= value+t;
            redisTool.hset(formSecretName,secret,sum);
            if((Integer)redisTool.hget(formSecretName,secret)!=sum){
                return R.error().message("添加失败");
            }
        }
        return R.ok().message("成功添加"+t+"次");
    }


}
