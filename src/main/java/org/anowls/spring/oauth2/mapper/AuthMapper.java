package org.anowls.spring.oauth2.mapper;

import java.util.List;

import org.anowls.spring.oauth2.domain.SwanUserDetails;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


/**
 * 认证数据接口
 *
 * @author haopeisong
 */
@Mapper
public interface AuthMapper {

    /**
     * 获取用户信息
     *
     * @param username 用户登录名
     * @return SwanUserDetails
     */
    @Select("SELECT " +
            "                t1.id AS id," +
            "                t1.work_id AS workId," +
            "                t1.org_id AS orgId," +
            "                t1.school_id AS schoolId," +
            "                t1.campus_id AS campusId," +
            "                t1.system_code AS systemCode," +
            "                t1.logon_name AS userName," +
            "                t1.password_verify AS password," +
            "                t1.user_name AS fullName," +
            "                t1.account_avatar AS accountAvatar," +
            "                t1.user_type  AS  userType" +
            "            FROM " +
            "                system_users t1 " +
            "            WHERE " +
            "               t1.status = '1' and t1.deleted = '0' AND (t1.logon_name = #{username} or t1.system_code=#{username} " +
            "                or t1.id_card=#{username})")
    SwanUserDetails findUserByName(String username);

    /**
     * 获取权限列表
     *
     * @return list
     */
    @Select("")
    List<String> findAuthByAdmin();

    /**
     * 根据用户名获取个人权限
     *
     * @param userId //用户id
     * @return list
     */
    @Select("")
    List<String> findAuthByUser(String userId);
}
