package com.robot2.secret.dao;

import com.robot2.secret.entity.BaseInfo;
import com.robot2.secret.entity.Secret;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (RobotSecret)表数据库访问层
 *
 * @author qiemengyan
 * @since 2020-04-22 15:30:04
 */
@Repository("RobotSecretDao")
public interface SecretHandleDao {

    /**
     * 新增秘钥数据数据
     *
     * @param secret 实例对象
     */
    @Insert("INSERT INTO robot_secret_platform (secret,createTime,finishTime) values (#{secret},#{createTime},#{finishTime})")
    @Options(useGeneratedKeys=true,keyProperty = "id", keyColumn = "id")
    void insert_secret_platform(Secret secret);
    @Insert("INSERT INTO robot_secret_robot (secret,createTime,finishTime) values (#{secret},#{createTime},#{finishTime})")
    @Options(useGeneratedKeys=true,keyProperty = "id", keyColumn = "id")
    void insert_secret_robot(Secret secret);
    @Insert("INSERT INTO robot_secret_user (secret,createTime,finishTime) values (#{secret},#{createTime},#{finishTime})")
    @Options(useGeneratedKeys=true,keyProperty = "id", keyColumn = "id")
    void insert_secret_user(Secret secret);

    /**
     *
     *
     * 新增基本信息数据
     * @param baseInfo 平台/机器人/用户/基本信息
     */
    @Insert("INSERT INTO robot_info_user (deviceId,secretId,username,password,phone,email,identity,address,org1,org2,online) values" +
            " (#{deviceId},#{secretId},#{username},#{password},#{phone},#{email},#{identity},#{address},#{org1},#{org2},#{online})")
    void insert_info_user(BaseInfo baseInfo);
    @Insert("INSERT INTO robot_info_robot (deviceId,secretId,name,phone,email,identity,address,org1,org2,online) values" +
            " (#{deviceId},#{secretId},#{name},#{phone},#{email},#{identity},#{address},#{org1},#{org2},#{online})")
    void insert_info_robot(BaseInfo baseInfo);
    @Insert("INSERT INTO robot_info_platform (deviceId,secretId,name,phone,email,identity,address,org1,org2,online) values" +
            " (#{deviceId},#{secretId},#{name},#{phone},#{email},#{identity},#{address},#{org1},#{org2},#{online})")
    void insert_info_platform(BaseInfo baseInfo);

    /**
     * 判断秘钥是否存在。查mysql，暂时不用
     * @param secret   秘钥
     * @return 存在返回true，否则返回false
     */
    @Select("select count(secret) from robot_secret_platform where secret=#{secret}")
    boolean ifSercetExist_platform(String secret);
    @Select("select count(secret) from robot_secret_robot where secret=#{secret}")
    boolean ifSercetExist_robot(String secret);
    @Select("select count(secret) from robot_secret_user where secret=#{secret}")
    boolean ifSercetExist_user(String secret);

    /**
     * 查询秘钥过期时间
     * @param secret 秘钥
     * @return 秘钥对应的失效时间
     */
    @Select("select finishTime from robot_secret_platform where secret=#{secret}")
    String queryFinishDateTime_secret_platform(String secret);
    @Select("select finishTime from robot_secret_robot where secret=#{secret}")
    String queryFinishDateTime_secret_robot(String secret);
    @Select("select finishTime from robot_secret_user where secret=#{secret}")
    String queryFinishDateTime_secret_user(String secret);

    /**
     *
     * @param secret 秘钥
     * @return 返回基础信息
     */
    @Select("select * from robot_info_platform where " +
            "secretId in (select id from robot_secret_platform where " +
            "secret=#{secret})")
    BaseInfo queryRobotInfo_secret_platform(String secret);
    @Select("select * from robot_info_robot where " +
            "secretId in (select id from robot_secret_robot where " +
            "secret=#{secret})")
    BaseInfo queryRobotInfo_secret_robot(String secret);
    @Select("select * from robot_info_user where " +
            "secretId in (select id from robot_secret_user where " +
            "secret=#{secret})")
    BaseInfo queryRobotInfo_secret_user(String secret);

    /**
     *
     * @return 返回当前在线的平台/机器人秘钥信息列表
     */
    @Select("select * from robot_secret_robot where id in " +
            "(select secretId from robot_info_robot where online=1)")
    List<Secret> queryOnlineFinishTime_robot();
    @Select("select * from robot_secret_platform where id in " +
            "(select secretId from robot_info_platform where online=1)")
    List<Secret> queryOnlineFinishTime_platform();

    /**
     *
     * @param secretId 秘钥ID，将过期的秘钥所对应的机器人/平台下线。
     */
    @Update("UPDATE  robot_info_platform SET online=0 where secretId=#{secretId}")
    void offline_platform(Integer secretId);

    @Update("UPDATE  robot_info_robot SET online=0 where secretId=#{secretId}")
    void offline_robot(Integer secretId);
}