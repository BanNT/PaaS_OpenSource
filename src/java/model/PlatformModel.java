/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Config;
import entity.Hosting;
import itplus.util.Common;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author BanNT
 */
public class PlatformModel {

    public int saveConfig(Config config) throws SQLException {
        int id = 0;
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            String SQL = "insert into tblConfig(idUser,platform,url,gear,scaling,databasetype,phpMyAdmin) values(?,?,?,?,?,?,?)";
            conn = DBUtil.connectMysql();
            stmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
//            stmt = conn.prepareStatement(SQL);
            stmt.setInt(1, config.getUserid());
            stmt.setString(2, config.getPlatform());
            stmt.setString(3, config.getUrl());
            stmt.setInt(4, config.getGears());
            stmt.setInt(5, config.getScale());
            stmt.setString(6, config.getDatabase());
            stmt.setString(7, config.getTools());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                throw ex;
            }
        }
        return id;
    }

    public int saveHosting(Hosting hosting) throws SQLException {
        int id = 0;
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            String SQL = "insert into tblhosting(idUser,hostingname,internal_ip,external_ip,keypair,statusVM,datecreate) values(?,?,?,?,?,1,?)";
            conn = DBUtil.connectMysql();
            stmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
//            stmt = conn.prepareStatement(SQL);
            stmt.setInt(1, hosting.getUserid());
            stmt.setString(2, hosting.getHostingname());
            stmt.setString(3, hosting.getInternal_ip());
            stmt.setString(4, hosting.getExternal_ip());
            stmt.setString(5, hosting.getKeypair());
            stmt.setDate(6, Common.currentDate());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                throw ex;
            }
        }
        return id;

    }

    public Hosting getKeypair(int userid) throws Exception {
        Hosting hosting = new Hosting();
        PreparedStatement pr = null;
        Connection conn = null;
        ResultSet rs = null;
        int id = 0;
        try {
            conn = DBUtil.connectMysql();
            String sql = "select keypair,hostingname from tblhosting where idUser = ?";
            pr = conn.prepareStatement(sql);
            pr.setInt(1, userid);
            rs = pr.executeQuery();
            while (rs.next()) {
                hosting.setKeypair(rs.getString(1));
                hosting.setHostingname(rs.getString(2));
            }
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            pr.close();
            rs.close();
            conn.close();
        }
        return hosting;
    }
}
