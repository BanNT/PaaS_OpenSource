/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package itplus.util;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author BanNT
 */
public class MyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        //kiem tra seession co ton tai khong
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
//        String username = (String) req.getSession().getAttribute("username");
        Cookie ck[] = req.getCookies();
        String username = "";
        for (int i = 0; i < ck.length; i++) {
            if (ck[i].getName().equals("username")) {
                username = ck[i].getValue();
            }
        }
        if (!username.equals("")) {
            chain.doFilter(req, res);
        } else {
            //day ve trang login
            String contextPath = req.getContextPath();
            res.sendRedirect(contextPath + "/faces/login.xhtml");
        }

    }

    @Override
    public void destroy() {
    }
}
