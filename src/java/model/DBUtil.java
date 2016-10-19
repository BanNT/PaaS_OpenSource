/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BanNT
 */
public class DBUtil {

    public static void main(String[] args) {
        connectMysql();
    }

    public static Connection connectMysql() {
        Connection con = null;
        try {
            //nap driver
            Class.forName("com.mysql.jdbc.Driver");

            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost/nucestack", "root", "");
            if (con != null) {
                System.out.println("Connected:");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return con;
    }
    
}
