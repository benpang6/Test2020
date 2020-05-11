package com.robot2.secret.tool;

import com.robot2.secret.entity.Secret;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;


public class KeyTool {
    /**
     * 秘钥生成工具
     * @param data 为秘钥本体内容；
     * @param type 为证书有效时间：1.年，2.月，3.日
     * @param number 有效时间数量;
     * @param dataType 生成类型；p-平台；r-机器人；u-用户
     * 例如：60cd54a928cbbcbb6e7b5595bab46a9e，1，2：表示将证书秘钥有效期为从当前时间起，两年失效。
     */
    //进行MD5加密，结果再进行SHA-1加密。
    public static Secret create(String data, int type, int number, String dataType) {
        Secret secret = new Secret();
        byte[] md5data = DigestUtils.md5Digest(data.getBytes());
        String str = DigestUtils.md5DigestAsHex(md5data);
        secret.setSecret(dataType+str);
        String createTime=TimeTool.getNowDay(3);
        secret.setCreateTime(createTime);
        String finishTime=TimeTool.addDateTime(LocalDateTime.now(),number,type);
        secret.setFinishTime(finishTime);
        return secret;
    }

}
