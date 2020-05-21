package com.robot2.secret.dao;

import com.robot2.secret.entity.Secret;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository("SecretRenewDao")
public interface SecretRenewDao {
    /*
     * 查询secret信息
     */
    @Select("select secret,finishTime from ${formSecretName} where id = (select secretId from ${formInfoName} where deviceId=#{deviceId})")
    Secret selectSecretByDeviceId(@Param("deviceId") String deviceId, @Param("formInfoName") String formInfoName, @Param("formSecretName") String formSecretName);

    /*
     * 通过secret修改数据到期时间
     */
    @Update("update ${formSecretName} set finishTime=#{finishTime} where secret=#{secret}")
    int updateSecetByDeviceId(@Param("secret") String secret, @Param("formSecretName") String formSecretName, @Param("finishTime") String finishTime);



}
