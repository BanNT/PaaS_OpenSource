/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vtca.connectionpool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

/**
 *
 * @author BanNT
 */
public class TestPool {

    public static void main(String[] args) {
        DBPool.build(10);//goi ham tao 10 connections đẩy là Linkedlist
        loadForm();
    }

    public static List<Vector> loadForm() {
        List<Vector> vtData = null;
        Statement stmt = null;
        ResultSet rs = null;
        Connection cn = null;
        try {
            cn = DBPool.getConnection();
            stmt = cn.createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        String SQL = "SELECT   ROW_NUMBER () OVER (ORDER BY b.cus_id ASC) AS stt, "
                + " b.name, "
                + " TO_CHAR (b.birthday, 'dd/mm/yyyy') birthday,"
                + " CASE WHEN b.gender = 1 THEN 'Nam' ELSE 'Nu' END gender, "
                + " b.area, b.cus_id "
                + " FROM   customer b ";

        try {
            rs = stmt.executeQuery(SQL);
            vtData = new ArrayList<Vector>();
            while (rs.next()) {
                Vector dataRow = new Vector(6);
                for (int i = 1; i <= 6; i++) {
                    dataRow.addElement(rs.getObject(i));
                }
                vtData.add(dataRow);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                DBPool.releaseConnection(cn, stmt, rs);
            } catch (Exception e) {
                System.out.println("Khong the dong ket noi hien tai");
                e.printStackTrace();
            }
        }
        System.out.println("values:" + vtData);
        return vtData;

    }
}
