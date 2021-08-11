package pers.zhz.dao;

import com.mysql.cj.util.StringUtils;
import org.junit.Test;
import pers.zhz.pojo.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户Dao实现类
 */
public class UserDaoImpl implements UserDao {

    /**
     * 根据编号和密码获取用户
     */
    public User getLoginUser(Connection connection, String code, String password) throws SQLException {
        User user = null;
        if (connection != null) {
            PreparedStatement ps = null;
            ResultSet rs = null;
            String sql = "select * from smbms_user where userCode=? and userPassword=?";
            Object[] params = {code, password};
            rs = BaseDao.execute(connection, sql, ps, params, rs);

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));//获取值
            }

            // 关闭资源，连接有可能还要使用，不关闭
            BaseDao.close(null, ps, rs);
        }
        return user;
    }

    /**
     * 修改用户密码
     *
     * @param connection
     * @param id
     * @param password
     * @return
     * @throws Exception
     */
    @Override
    public int updatePwd(Connection connection, int id, String password) throws Exception {
        PreparedStatement pstm = null;
        int execute = 0;
        if (connection != null) {
            String sql = "update smbms_user set userPassword = ? where id = ?";
            Object params[] = {password, id};
            execute = BaseDao.execute(connection, sql, pstm, params);
            BaseDao.close(null, pstm, null);
        }
        return execute;
    }

    /**
     * 根据用户名或者角色查询用户总数
     *
     * @param connection
     * @param username
     * @param userRole
     * @return
     * @throws Exception
     */
    @Override
    public int getUserCount(Connection connection, String username, int userRole) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;

        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("select count(1) as count from smbms_user u,smbms_role r where u.userRole = r.id");
            ArrayList<Object> list = new ArrayList<Object>();//存放我们的参数

            if (!StringUtils.isNullOrEmpty(username)) {
                sql.append(" and u.userName like ?");
                list.add("%" + username + "%"); //index:0
            }

            if (userRole > 0) {
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }

            //怎么把list转换为数组
            Object[] params = list.toArray();
            System.out.println("UserDaoImpl-->getUserCount:" + sql.toString());//输出最后完整的SQL语句
            rs = BaseDao.execute(connection, sql.toString(), pstm, params, rs);
            if (rs.next()) {
                count = rs.getInt("count");//从结果集中获取最终的数量
            }
            BaseDao.close(null, pstm, rs);
        }
        return count;
    }

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
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<User> userList = new ArrayList<User>();
        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id");
            List<Object> list = new ArrayList<Object>();
            if (!StringUtils.isNullOrEmpty(userName)) {
                sql.append(" and u.userName like ?");
                list.add("%" + userName + "%");
            }
            if (userRole > 0) {
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }

            //在数据库中，分页使用  limit startIndex，pageSize； 总数
            //当前页 （当前页-1）*页面大小
            //0,5    1 0  01234
            //5,5    2  5  56789
            //10,5   3  10 1011121314

            sql.append(" order by creationDate DESC limit ?,?");
            currentPageNo = (currentPageNo - 1) * pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params = list.toArray();
            System.out.println("sql ----> " + sql.toString());
            rs = BaseDao.execute(connection, sql.toString(), pstm, params, rs);
            while (rs.next()) {
                User _user = new User();
                _user.setId(rs.getInt("id"));
                _user.setUserCode(rs.getString("userCode"));
                _user.setUserName(rs.getString("userName"));
                _user.setGender(rs.getInt("gender"));
                _user.setBirthday(rs.getDate("birthday"));
                _user.setPhone(rs.getString("phone"));
                _user.setUserRole(rs.getInt("userRole"));
                _user.setUserRoleName(rs.getString("userRoleName"));
                userList.add(_user);
            }
            BaseDao.close(null, pstm, rs);
        }
        return userList;
    }


    /**
     * 通过用户编码获取用户
     *
     * @param connection
     * @param userCode
     * @return
     * @throws SQLException
     */
    @Override
    public User getUserByUserCode(Connection connection, String userCode) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        User user = new User();
        if (connection != null) {
            String sql = "select * from smbms_user where userCode = ?";
            ArrayList<String> tmp = new ArrayList<>();
            tmp.add(userCode);
            Object[] params = tmp.toArray();
            rs = BaseDao.execute(connection, sql, pstm, params, rs);
            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setUserRole(rs.getInt("userRole"));
                break;
            }
            BaseDao.close(null, pstm, rs);
        }
        return user;
    }

    /**
     * 新增用户
     *
     * @param connection
     * @param user
     * @return
     */
    @Override
    public int addUser(Connection connection, User user) throws SQLException {
        PreparedStatement pstm = null;
        int updateRows = 0;
        if (connection != null) {
            String sql = "insert into smbms_user(userCode,userName,userPassword," +
                    "gender,birthday,phone,address,userRole,createdBy,creationDate,modifyBy,modifyDate)" +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {user.getUserCode(), user.getUserName(), user.getUserPassword(),
                    user.getGender(), user.getBirthday(), user.getPhone(),
                    user.getAddress(), user.getUserRole(), user.getCreatedBy(),
                    user.getCreationDate(), user.getModifyBy(), user.getModifyDate()};
            updateRows = BaseDao.execute(connection, sql, pstm, params);
            BaseDao.close(null, pstm, null);
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
    public int delUser(Connection connection, int id) throws SQLException {
        PreparedStatement pstm = null;
        int updateRows = 0;
        if (connection != null) {
            String sql = "delete from smbms_user where id=?";
            Object[] params = {id};
            updateRows = BaseDao.execute(connection, sql, pstm, params);
            BaseDao.close(null, pstm, null);
        }
        return updateRows;
    }

    /**
     * 通过用户id查询用户
     *
     * @param id
     * @param connection
     * @return
     */
    public User getUserById(Connection connection, int id) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        User user = new User();
        if (connection != null) {
            String sql = "select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id and u.id = ?";
            Object[] params = new Object[1];
            params[0] = id;
            rs = BaseDao.execute(connection, sql, pstm, params, rs);
            while (rs.next()) {
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setUserRoleName(rs.getString("userRoleName"));
                break;
            }
            BaseDao.close(null, pstm, rs);
        }
        return user;
    }

    /**
     * 通过id修改用户信息
     *
     * @param connection
     * @param user
     * @return
     */
    public int modifyUser(Connection connection, User user) throws SQLException {
        PreparedStatement pstm = null;
        int updateRows = 0;
        if (connection != null) {
            String sql = "update smbms_user set userCode=?, userName=?, gender=?, birthday=?, phone=?, " +
                    "address=?, userRole=?, modifyBy=?, modifyDate=? where id=?";
            Object[] params = {user.getUserCode(), user.getUserName(), user.getGender(), user.getBirthday(),
                    user.getPhone(), user.getAddress(), user.getUserRole(), user.getModifyBy(),
                    user.getModifyDate(), user.getId()};
            updateRows = BaseDao.execute(connection, sql, pstm, params);
            BaseDao.close(null, pstm, null);
        }
        return updateRows;
    }
}