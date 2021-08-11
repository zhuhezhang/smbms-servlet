package pers.zhz.servlet;

import pers.zhz.pojo.User;
import pers.zhz.service.UserService;
import pers.zhz.service.UserServiceImpl;
import pers.zhz.utils.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录接口（servlet层相当于mvc架构的controller层）
 */
public class LoginServlet extends HttpServlet {

    // 用户Service
    private UserService userService;

    public LoginServlet() {
        userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入LoginServlet--doGet");
        String code = req.getParameter("userCode");
        String password = req.getParameter("userPassword");
        // 获取登录用户
        User user = userService.login(code, password);
        // 有此用户，可以登录
        if (user != null) {
            // 将用户信息放到session中
            req.getSession().setAttribute(Constants.USER_SESSION,user);
            // 重定向到主页
            resp.sendRedirect("jsp/frame.jsp");
        } else {
            // 无此用户，转发到登录页,返回提示信息
            req.setAttribute("error","用户名或密码错误");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}

