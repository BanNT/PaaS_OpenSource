/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import itplus.util.Common;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import vtca.connectionpool.DBPool;

/**
 *
 * @author BanNT
 */
public class RegisterModel {

    /*
    public ArrayList<Register> getData() throws Exception {
        ArrayList<Register> arr = new ArrayList<Register>();
        Statement stmt = null;
        ResultSet rs = null;
        Connection cn = null;
        try {
            cn = DBPool.getConnection();
            stmt = cn.createStatement();
            String SQL = "SELECT * from tblRegister";
            rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                Register reg = new Register();
                reg.setId(rs.getInt(1));
                reg.setFirstname(rs.getString(2));
                reg.setLastname(rs.getString(3));
                reg.setGender(rs.getBoolean(4));
                reg.setEmail(rs.getString(5));
                reg.setMobile(rs.getString(6));
                reg.setAddress(rs.getString(7));
                reg.setUsername(rs.getString(8));
                reg.setPassword(rs.getString(9));
                arr.add(reg);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            try {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (stmt != null) {
//                    stmt.close();
//                }
//                if (cn != null) {
//                    cn.close();
//                }
                DBPool.releaseConnection(cn, stmt, rs);
            } catch (Exception e) {
                throw e;
            }
        }
        return arr;
    }
     */
    public int addRegister(Register register) throws SQLException {
        int id = 0;
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            String SQL = "insert into tbluser(fullname,email,company,country,username,password,status,datecreate) values(?,?,?,?,?,?,1,?)";
            conn = DBUtil.connectMysql();
            stmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
//            stmt = conn.prepareStatement(SQL);
            stmt.setString(1, register.getFullname());
            stmt.setString(2, register.getEmail());
            stmt.setString(3, register.getCompany());
            stmt.setString(4, register.getCountry());
            stmt.setString(5, register.getUsername());
            stmt.setString(6, register.getPassword());
            //lay ve thoi gian he thong
            stmt.setDate(7, Common.currentDate());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            id = rs.getInt(1);
            System.out.println("ID: " + id);
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
    /*
    //delete
    public void editRegister(Register register) throws SQLException {
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            String SQL = "update tblRegister set firstname = ? where id = ?";
            conn = DBUtil.connectSQL();
            SimpleDateFormat simple = new SimpleDateFormat("dd/MM/yyyy");
            stmt = conn.prepareStatement(SQL);
            stmt.setString(1, register.getFirstname());
            stmt.setInt(2, register.getId());
            stmt.executeUpdate();
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                throw ex;
            }
        }
    }

    public void deleteRegister(int id) throws SQLException {
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            String SQL = "delete tblRegister  where id = ?";
            conn = DBUtil.connectSQL();
            stmt = conn.prepareStatement(SQL);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                throw ex;
            }
        }
    }

    public void deleteRegister(ArrayList<Register> arr) throws SQLException, Exception {
        PreparedStatement stmt = null;
        Connection conn = null;
        try {
            conn = DBPool.getConnection();
            conn.setAutoCommit(false);//tao transaction
            for (Register register : arr) {
                String SQL = "delete tblRegister  where id = ?";
                stmt = conn.prepareStatement(SQL);
                stmt.setInt(1, register.getId());
                stmt.executeUpdate();
            }
            conn.commit();

        } catch (Exception ex) {
            conn.rollback();
            conn.setAutoCommit(true);
            throw new Exception(ex.getMessage());

        } finally {

            DBPool.releaseConnection(conn, stmt);
        }
    }*/
}
