package pers.zhz.dao;

import pers.zhz.pojo.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao {
    /**
     * 获取用户角色列表
     *
     * @param connection
     * @return
     * @throws Exception
     */
    public List<Role> getRoleList(Connection connection) throws Exception {

        PreparedStatement pstm = null;
        ResultSet rs = null;
        ArrayList<Role> roleList = new ArrayList<Role>();
        if (connection != null) {
            String sql = "select * from smbms_role";
            Object[] params = {};
            rs = BaseDao.execute(connection, sql, pstm, params, rs);

            while (rs.next()) {
                Role _role = new Role();
                _role.setId(rs.getInt("id"));
                _role.setRoleCode(rs.getString("roleCode"));
                _role.setRoleName(rs.getString("roleName"));
                roleList.add(_role);
            }
            BaseDao.close(null, pstm, rs);
        }
        return roleList;
    }
}

