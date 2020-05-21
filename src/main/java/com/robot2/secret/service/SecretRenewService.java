package com.robot2.secret.service;

import com.robot2.secret.tool.resultool.R;

import java.util.Map;

public interface SecretRenewService {
    R insertTime(Map<String, String> map);

    R insertTimes(Map<String, String> map);
}
