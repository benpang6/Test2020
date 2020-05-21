package com.robot2.secret.service;

import com.robot2.secret.VO.InfoFormDeleteVO;
import com.robot2.secret.VO.InfoFormVO;
import com.robot2.secret.entity.BaseInfo;
import com.robot2.secret.tool.resultool.R;

import java.util.List;

/**
 * (RobotSecret)表服务接口
 *
 * @author qiemengyan
 * @since 2020-04-22 15:30:05
 */
public interface SecretHandleService {

   R register(BaseInfo baseInfo);

   R check(String secret);

   int checkSecretNumber(String secret);

   void checkRobotSecretTime();
   void checkPlatformSecretTime();

    R deleteSecretAndInfo(InfoFormDeleteVO infoFormDeleteVO);

    R plantFormAndRobotUpdate(InfoFormVO info);
}