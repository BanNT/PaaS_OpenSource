/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Product;
import entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author BanNT
 */
public class LoginModel {

    public int checkLogin(User user) throws SQLException, Exception {
        PreparedStatement pr = null;
        Connection conn = null;
        ResultSet rs = null;
        int id = 0;
        try {
            conn = DBUtil.connectMysql();
            String sql = "select * from tbluser where username = ? and password = ?";
            pr = conn.prepareStatement(sql);
            pr.setString(1, user.getUsername());
            pr.setString(2, user.getPassword());
            rs = pr.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            pr.close();
            rs.close();
            conn.close();
        }
        return id;
    }
}
