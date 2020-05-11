package com.robot2.secret.controller;

import com.robot2.secret.entity.BaseInfo;
import com.robot2.secret.service.SecretHandleService;
import com.robot2.secret.tool.resultool.R;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * (RobotSecret/RobotInfo)表控制层,
 * 功能包括：信息(秘钥)注册、秘钥监测、秘钥检测。
 * @author qiemengyan
 * @since 2020-04-22 15:30:06
 */
@RestController
@RequestMapping("/common")
public class SecretHandleController {
    /**
     * 服务对象
     */
    @Resource
    private SecretHandleService robotSecretService;

    /**
     * 收集注册信息，返回加密秘钥
     * @return 返回检测结果
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public R register(@RequestBody BaseInfo baseInfo) {
        return this.robotSecretService.register(baseInfo);
    }

    /**
     * 检查秘钥(总)
     * @param secret 秘钥内容
     * @return 返回检测结果包括到期时间、机器人/平台的基础信息、服务调用次数
     */
    @RequestMapping(value = "/check",method = RequestMethod.GET)
    public R check(String secret){
         return this.robotSecretService.check(secret);
    }

    /**
     * 检查秘钥（调用服务的剩余次数）
     * @param secret 秘钥内容
     * @return 返回剩余次数
     */
    @RequestMapping(value = "/check/number",method = RequestMethod.GET)
    public int checkSecretNumber(String secret){
        return this.robotSecretService.checkSecretNumber(secret);
    }

    /**
     * 定时任务。每天执行一次。判断当前在线机器人是否管控到期。
     */
    @Scheduled(cron="${secret.robot.time}")
    public void checkRobotSecretTime(){
        this.robotSecretService.checkRobotSecretTime();
    }
    /**
     * 定时任务。每天执行一次。判断当前在线平台是否管控到期。
     */
    @Scheduled(cron="${secret.platform.time}")
    public void checkPlatformSecretTime(){
        this.robotSecretService.checkPlatformSecretTime();
    }

}