package pers.zhz.dao;

import pers.zhz.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 用户Dao接口
 */
public interface UserDao {

    /**
     * 根据编号和密码获取用户
     */
    User getLoginUser(Connection connection, String code, String password) throws SQLException;

    /**
     * 修改用户密码
     *
     * @param connection
     * @param id
     * @param password
     * @return
     * @throws Exception
     */
    int updatePwd(Connection connection, int id, String password) throws Exception;

    /**
     * 查询用户总数
     *
     * @param connection
     * @param username
     * @param userRole
     * @return
     * @throws Exception
     */
    int getUserCount(Connection connection, String username, int userRole) throws Exception;

    /**
     * 获取用户列表
     *
     * @param connection
     * @param userName
     * @param userRole
     * @param currentPageNo
     * @param pageSize
     * @return
     * @throws Exception
     */
    List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception;

    /**
     * 通过用户编码获取用户
     *
     * @param connection
     * @param userCode
     * @return
     */
    User getUserByUserCode(Connection connection, String userCode) throws SQLException;

    /**
     * 添加用户
     *
     * @param connection
     * @param user
     * @return
     */
    int addUser(Connection connection, User user) throws SQLException;

    /**
     * 删除用户
     *
     * @param id
     * @param connection
     * @return
     */
    int delUser(Connection connection, int id) throws SQLException;

    /**
     * 通过用户id查询用户
     *
     * @param id
     * @param connection
     * @return
     */
    User getUserById(Connection connection, int id) throws SQLException;

    /**
     * 通过id修改用户信息
     *
     * @param connection
     * @param user
     * @return
     */
    int modifyUser(Connection connection, User user) throws SQLException;
}

