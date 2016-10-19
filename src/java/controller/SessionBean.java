/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.Config;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Administrator
 */
public class SessionBean {

    public SessionBean() {

    }

    public static HttpSession newSession(boolean value) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(value);
        return session;
    }

    public String mUsername() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        String username = (String) session.getAttribute("user");
        return username;
    }

    public int userid() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        int id = (Integer) session.getAttribute("id");
        return id;
    }

    public Config mConfig() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        Config config = (Config) session.getAttribute("config");
        if (config == null) {
            config = new Config();
        }
        return config;
    }

    public static SessionBean getInstance() {
        return new SessionBean();
    }

    public String getGear(int i) {
        String value = "";
        if (i == 1) {
            value = "Small";
        } else if (i == 2) {
            value = "Medium";
        } else {
            value = "Large";
        }
        return value;
    }

}
