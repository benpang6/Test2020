package com.robot2.secret.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * (RobotSecret)实体类,证书秘钥存储实体，对应robot_sercet_*表
 *
 * @author qiemengyan
 * @since 2020-04-22 15:30:02
 */
@Data
public class Secret implements Serializable {
    private static final long serialVersionUID = -83483585027267523L;
    
    private Integer id;
    
    private String secret;

    private String createTime;

    private String finishTime;


}