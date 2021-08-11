package pers.zhz.service;

import pers.zhz.dao.BaseDao;
import pers.zhz.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * 用户Service接口
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param code
     * @param password
     * @return
     */
    User login(String code, String password);

    /**
     * 修改用户密码
     *
     * @param id
     * @param password
     * @return
     */
    Boolean updatePwd(int id, String password);

    /**
     * 查询记录数
     *
     * @param username
     * @param userRole
     * @return
     */
    int getUserCount(String username, int userRole);


    /**
     * 根据条件查询用户列表
     *
     * @param queryUserName
     * @param currentPageNo
     * @param queryUserRole
     * @param pageSize
     * @return
     */
    List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize);

    /**
     * 通过用户编码获取用户
     *
     * @param userCode
     * @return
     */
    User getUserByUserCode(String userCode) throws SQLException;

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    int addUser(User user) throws SQLException;

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    int delUser(int id) throws SQLException;

    /**
     * 通过用户id查询用户
     *
     * @param id
     * @return
     */
    User getUserById(int id) throws SQLException;

    /**
     * 通过id修改用户信息
     *
     * @param user
     * @return
     */
    public int modifyUser(User user) throws SQLException;
}
