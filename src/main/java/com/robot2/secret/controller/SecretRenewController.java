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
     * @param map 续约时间。传入参数
     * @return 返回结果
     */
    @RequestMapping(method = RequestMethod.POST)
    public R renewTime(@RequestBody Map<String,String> map){
        return null;
    }
}
