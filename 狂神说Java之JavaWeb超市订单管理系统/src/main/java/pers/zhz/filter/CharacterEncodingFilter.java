package pers.zhz.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * 字符编码过滤器
 */
public class CharacterEncodingFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("utf-8");
        servletResponse.setCharacterEncoding("utf-8");
        // 让请求继续通行，如果不写，程序到这里就被拦截停止
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {
    }
}
