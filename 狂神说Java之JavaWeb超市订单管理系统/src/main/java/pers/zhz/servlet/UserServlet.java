package pers.zhz.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import pers.zhz.dao.BaseDao;
import pers.zhz.pojo.Role;
import pers.zhz.pojo.User;
import pers.zhz.service.RoleService;
import pers.zhz.service.RoleServiceImpl;
import pers.zhz.service.UserService;
import pers.zhz.service.UserServiceImpl;
import pers.zhz.utils.Constants;
import pers.zhz.utils.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户视图解析
 */
public class UserServlet extends HttpServlet {

    // 用户Service
    private UserService userService;

    public UserServlet() {
        userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            System.out.println("进入UserServlet--doGet");
            String method = req.getParameter("method");
            System.out.println("method: " + method);
            if ("savepwd".equals(method)) {//保存密码
                updateOwnPwd(req, resp);
            } else if ("pwdmodify".equals(method)) {//检查原密码是否正确
                checkPwd(req, resp);
            } else if ("query".equals(method)) {//用户信息查询
                query(req, resp);
            } else if ("getRoleList".equals(method)) {// 用户角色列表查询
                getRoleList(req, resp);
            } else if ("userCodeExist".equals(method)) {// 添加用户
                userCodeExist(req, resp);
            } else if ("addUser".equals(method)) {// 添加用户
                addUser(req, resp);
            } else if ("delUser".equals(method)) {// 删除用户
                delUser(req, resp);
            } else if ("view".equals(method) || "viewModifyUser".equals(method)) {// 根据id查看单个用户信息
                getUserById(req, resp);
            } else if ("modifyUser".equals(method)) {// 根据id修改用户信息
                modifyUser(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            doGet(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证自己的密码
     *
     * @param req
     * @param resp
     */
    private void checkPwd(HttpServletRequest req, HttpServletResponse resp) {
        Map<String, String> resultMap = new HashMap<String, String>();
        String oldpassword = req.getParameter("oldpassword");
        // 旧密码输入为空
        if (StringUtils.isNullOrEmpty(oldpassword)) {
            resultMap.put("result", "error");
        } else {
            Object o = req.getSession().getAttribute(Constants.USER_SESSION);
            // 当前用户session过期，请重新登录
            if (o == null) {
                resultMap.put("result", "sessionerror");
            } else {
                String password = ((User) o).getUserPassword();
                System.out.println(("原密码" + ((User) o).getUserPassword()) + "输入的密码" + oldpassword);
                // 旧密码正确
                if (oldpassword.equals(password)) {
                    resultMap.put("result", "true");
                    // 旧密码输入不正确
                } else {
                    resultMap.put("result", "false");
                }
            }
        }
        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JSON.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改自己的密码
     *
     * @param req
     * @param resp
     */
    private void updateOwnPwd(HttpServletRequest req, HttpServletResponse resp) {
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        if (o != null && ((User) o).getId() != null) {
            Integer id = ((User) o).getId();
            String password = req.getParameter("newpassword");
            boolean flag = userService.updatePwd(id, password);
            // 修改密码成功，移除session中的用户信息
            if (flag) {
                req.getSession().removeAttribute(Constants.USER_SESSION);
                req.setAttribute("message", "修改密码成功，请退出，使用新密码登录");
            } else {
                // 修改密码失败
                req.setAttribute("message", "修改密码失败");
            }
        } else {
            // 修改密码失败
            req.setAttribute("message", "修改密码失败");
        }
        // 转发到修改密码页
        try {
            req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询用户
     *
     * @param req
     * @param resp
     */
    public void query(HttpServletRequest req, HttpServletResponse resp) {
        //从前端获取数据
        String queryUserName = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");

        //获取用户列表
        UserServiceImpl userService = new UserServiceImpl();

        int pageSize = 5;//可以把其写在配置文件
        int currentPageNo = 1;

        int queryUserRole = 0;
        if (queryUserName == null) {
            queryUserName = "";
        }
        if (temp != null && !temp.equals("")) {
            queryUserRole = Integer.parseInt(temp); //给查询赋值
        }
        if (pageIndex != null) {
            currentPageNo = Integer.parseInt(pageIndex);
        }
        //获取用户的总数(分页：上一页，下一页)
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);
        //总页数支持
        PageSupport pageSupport = new PageSupport();
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setPageSize(pageSize);
        pageSupport.setTotalCount(totalCount);

        int totalPageCount = totalCount / pageSize + 1;//总共有几页

        //控制首页和尾页
        //如果页面要小于一了，就显示第一页的东西
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {//页面大于最后一页
            currentPageNo = totalPageCount;
        }

        //获取用户列表展示
        List<User> userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        req.setAttribute("userList", userList);
        RoleServiceImpl roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        req.setAttribute("roleList", roleList);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo", currentPageNo);
        req.setAttribute("queryUserName", queryUserName);
        req.setAttribute("queryUserRole", queryUserRole);

        //返回前端
        try {
            req.getRequestDispatcher("userlist.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加用户界面的查询用户角色表
     *
     * @param req
     * @param resp
     */
    public void getRoleList(HttpServletRequest req, HttpServletResponse resp) {
        RoleService roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JSON.toJSONString(roleList));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查用户code(用户名)是否存在
     *
     * @param req
     * @param resp
     */
    private void userCodeExist(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        User user = userService.getUserByUserCode(req.getParameter("userCode"));
        Map<String, String> resultMap = new HashMap<>();
        if (user.getUserCode() == null) {
            resultMap.put("userCode", "notExist");
        } else {
            resultMap.put("userCode", "exist");
        }
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSON.toJSONString(resultMap));
        writer.flush();
        writer.close();
    }

    /**
     * 添加用户
     *
     * @param req
     * @param resp
     */
    public void addUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        System.out.println("进入到addUser函数");
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setAddress(address);
        user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        user.setGender(Integer.valueOf(gender));
        user.setPhone(phone);
        user.setUserRole(Integer.valueOf(userRole));
        user.setCreationDate(new Date());
        user.setCreatedBy(((User) req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyBy(user.getCreatedBy());
        user.setModifyDate(new Date());

        UserService userService = new UserServiceImpl();
        if (userService.addUser(user) == 1) {
            resp.sendRedirect(req.getContextPath() + "/jsp/user.do?method=query");
        } else {
            req.getRequestDispatcher("useradd.jsp").forward(req, resp);
        }
    }

    /**
     * 删除用户
     *
     * @param req
     * @param resp
     */
    public void delUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        int id = Integer.parseInt(req.getParameter("uid"));
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (id <= 0) {
            resultMap.put("delResult", "notexist");
        } else {
            UserService userService = new UserServiceImpl();
            if (userService.delUser(id) == 1) {
                resultMap.put("delResult", "true");
            } else {
                resultMap.put("delResult", "false");
            }
        }

        //把resultMap转换成json对象输出
        resp.setContentType("application/json");
        PrintWriter outPrintWriter = resp.getWriter();
        outPrintWriter.write(JSONArray.toJSONString(resultMap));
        outPrintWriter.flush();
        outPrintWriter.close();
    }

    /**
     * 通过用户id查询用户
     *
     * @param req
     * @param resp
     * @return
     */
    public void getUserById(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        int id = 0;
        String id1 = req.getParameter("uid");
        String id2 = req.getParameter("id");// 用于区分转发到查看用户界面还是修改用户界面
        System.out.println("id1: " + id1 + "   id2:" + id2);
        if (id1 == null) {// 修改用户信息
            id = Integer.parseInt(id2);
        } else {
            id = Integer.parseInt(id1);
        }

        User user = null;
        Connection connection = BaseDao.getConnection();
        try {
            UserService userService = new UserServiceImpl();
            user = userService.getUserById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDao.close(connection, null, null);
        }
        req.setAttribute("user", user);


        try {
            if (id1 != null) {
                req.getRequestDispatcher("userview.jsp").forward(req, resp);
            } else {
                req.getRequestDispatcher("usermodify.jsp").forward(req, resp);
            }
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过id修改用户信息
     *
     * @param req
     * @param resp
     * @return
     */
    public void modifyUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");
        int id = Integer.parseInt(req.getParameter("id"));

        User user = new User();
        user.setId(id);
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setAddress(address);
        user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        user.setGender(Integer.valueOf(gender));
        user.setPhone(phone);
        user.setUserRole(Integer.valueOf(userRole));
        user.setModifyBy(((User) req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());

        UserService userService = new UserServiceImpl();
        userService.modifyUser(user);
        resp.sendRedirect(req.getContextPath() + "/jsp/user.do?method=query");
    }

}
