package com.robot2.secret.controller;

import com.robot2.secret.service.SecretRenewService;
import com.robot2.secret.tool.resultool.R;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/renew")
public class SecretRenewController {
    @Resource
    private SecretRenewService secretRenewService;
    /**
     * 传入deviceId，续期时间（可为：1y(一年)、3M（3个月）
     * @param map 续约时间。传入参数
     * @return 返回结果
     */
    @RequestMapping(value = "/keyTime",method = RequestMethod.POST)
    public R renewTime(@RequestBody Map<String,String> map){
        return secretRenewService.insertTime(map);
    }

    /**
     * 传入deviceId,续约次数1-10000000
     * @param map   传入参数
     * @return      返回结果
     */
    @RequestMapping(value = "/keyTimes",method = RequestMethod.POST)
    public R RenewTimes(@RequestBody Map<String,String> map){
        return secretRenewService.insertTimes(map);
    }
}
