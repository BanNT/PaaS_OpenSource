/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.User;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import message.MessageUtil;
import model.LoginModel;

/**
 *
 * @author Administrator
 */
public class LoginBean {

    private User user;
    private String newPassword;
    private String confirmPass;
    private LoginModel lgModel;

    public LoginBean() {
        user = new User();
        lgModel = new LoginModel();
    }

//    @PostConstruct
//    void initialiseSession() {
//        FacesContext.getCurrentInstance().getExternalContext().getSession(true);
//    }
    public String checkLogin() throws Exception {
        //kiem tra thong tin nguoi dung, neu thanh cong thi chuyen
        //sang trang chu, nguoc lai thong bao loi
//        int idUser = lgModel.checkLogin(user);
        int idUser = 1;
        if (idUser == 0) {
            //day ra thong bao loi
            MessageUtil.errorMessage("Login false, try again.");
            user = new User();
            return "";
        } else {
            //tao session
            HttpSession session = SessionBean.newSession(true);
            session.setAttribute("user", user.getUsername());
            session.setAttribute("pass", user.getPassword());
            session.setAttribute("id", idUser);
            //tao cookie
//            Cookie cookie = new Cookie("username", user.getUsername());
////            cookie.setMaxAge(24 * 3600);//;ton tai 1 ngay
//            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//            response.addCookie(cookie);//day cookie ve trinh duyet web ng dung                        
        }
        return "dashboard?faces-redirect=true";
    }

    public String next() {
        return "/pages/homepage?faces-redirect=true";
    }

    public String myHosting() {
        return "myhosting?faces-redirect=true";
    }

    public String createHosting() {
        return "applicationtype?faces-redirect=true";
    }

    public String logout() {
        try {
            //huy session va huy cookie
            //lay ve session
            HttpSession ss = SessionBean.newSession(false);
            ss.invalidate();
//        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            //lay ve cookie
//            Cookie[] ck = request.getCookies();
//            for (int i = 0; i < ck.length; i++) {
//                ck[i].setValue("");
//                ck[i].setMaxAge(0);//huy cookie
//                response.addCookie(ck[i]);
//            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "index?faces-redirect=true";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPass() {
        return confirmPass;
    }

    public void setConfirmPass(String confirmPass) {
        this.confirmPass = confirmPass;
    }

}
