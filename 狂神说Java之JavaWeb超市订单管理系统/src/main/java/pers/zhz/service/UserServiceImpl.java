package pers.zhz.service;

import pers.zhz.dao.BaseDao;
import pers.zhz.dao.UserDao;
import pers.zhz.dao.UserDaoImpl;
import pers.zhz.pojo.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 用户Service实现类
 */
public class UserServiceImpl implements UserService {

    // 用户Dao
    private UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }

    /**
     * 用户登录
     */
    public User login(String code, String password) {
        User user = null;
        Connection connection = BaseDao.getConnection();
        try {
            user = userDao.getLoginUser(connection, code, password);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.close(connection, null, null);
        }
        return user;
    }

    @Override
    public Boolean updatePwd(int id, String password) {
        Connection connection = null;
        Boolean flag = false;

        //修改密码
        try {
            connection = BaseDao.getConnection();
            if (userDao.updatePwd(connection, id, password) > 0) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.close(connection, null, null);
        }
        return flag;
    }

    /**
     * 查询记录数
     *
     * @param username
     * @param userRole
     * @return
     */
    public int getUserCount(String username, int userRole) {
        Connection connection = null;
        int count = 0;
        try {
            connection = BaseDao.getConnection();
            count = userDao.getUserCount(connection, username, userRole);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.close(connection, null, null);
        }
        return count;
    }

    //根据条件查询用户列表
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        Connection connection = null;
        List<User> userList = null;
        System.out.println("queryUserName ---- > " + queryUserName);
        System.out.println("queryUserRole ---- > " + queryUserRole);
        System.out.println("currentPageNo ---- > " + currentPageNo);
        System.out.println("pageSize ---- > " + pageSize);
        try {
            connection = BaseDao.getConnection();
            userList = userDao.getUserList(connection, queryUserName, queryUserRole, currentPageNo, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.close(connection, null, null);
        }
        return userList;
    }

    /**
     * 通过用户编码获取用户
     *
     * @param userCode
     * @return
     */
    public User getUserByUserCode(String userCode) throws SQLException {
        Connection connection = null;
        User user = null;
        try {
            connection = BaseDao.getConnection();
            user = userDao.getUserByUserCode(connection, userCode);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.close(connection, null, null);
        }
        return user;
    }

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @Override
    public int addUser(User user) throws SQLException {
        Connection connection = null;
        int updateRows = 0;
        try {
            connection = BaseDao.getConnection();
            updateRows = userDao.addUser(connection, user);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.close(connection, null, null);
        }
        return updateRows;
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @Override
    public int delUser(int id) throws SQLException {
        Connection connection = null;
        int updateRows = 0;
        try {
            connection = BaseDao.getConnection();
            updateRows = userDao.delUser(connection, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.close(connection, null, null);
        }
        return updateRows;
    }

    /**
     * 通过用户id查询用户
     *
     * @param id
     * @return
     */
    public User getUserById(int id) throws SQLException {
        User user = null;
        Connection connection = BaseDao.getConnection();
        try {
            user = userDao.getUserById(connection, id);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.close(connection, null, null);
        }
        return user;
    }

    /**
     * 通过id修改用户信息
     *
     * @param user
     * @return
     */
    public int modifyUser(User user) throws SQLException {
        Connection connection = null;
        int updateRows = 0;
        try {
            connection = BaseDao.getConnection();
            updateRows = userDao.modifyUser(connection, user);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.close(connection, null, null);
        }
        return updateRows;
    }

    @Test
    public void test() {
        User user = new UserServiceImpl().login("admin", "1234567");
        System.out.println(user != null ? user.getUserPassword() : "用户名或密码错误");
    }
}
