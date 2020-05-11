package com.robot2.secret.service.impl;

import com.robot2.secret.dao.SecretRenewDao;
import com.robot2.secret.service.SecretRenewService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("SecretRenewService")
public class SecretRenewServiceImpl  implements SecretRenewService {
    @Resource
    private SecretRenewDao secretRenewDao;
}
