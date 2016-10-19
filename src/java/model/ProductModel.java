/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.Product;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class ProductModel {
    public void addProduct(Product p) throws Exception{
        Connection con=null;
        PreparedStatement pr = null;

        try{
            String sql = "insert into Products values(?,?,?,?)";
            con = DBUtil.connectMysql();
            pr = con.prepareStatement(sql);
            //
            SimpleDateFormat sim = new SimpleDateFormat("dd/MM/yyyy");
            
            pr.setString(1, p.getName());
            pr.setDouble(2, p.getPrice());
            Date datecreate = new Date(p.getDatecreate().getTime());
            pr.setDate(3, datecreate);
            pr.setInt(4, p.getIdGroup());
            ///
             pr.executeUpdate();
        }catch(Exception ex){
            throw new Exception(ex.getMessage());
        }
    }
   public ArrayList<Product> getAllProduct() throws Exception{
      ArrayList<Product> value =  new ArrayList<>();
       Statement stmt = null;
       Connection conn = null;
       ResultSet rs = null;
       try{
           conn = DBUtil.connectMysql();
           stmt = conn.createStatement();
           String sql = "select * from products";
           rs = stmt.executeQuery(sql);
           while(rs.next()){
               Product p = new Product();
               p.setId(rs.getInt(1));
               p.setName(rs.getString(2));
               p.setPrice(rs.getDouble(3));
               p.setDatecreate(rs.getDate(4));
               p.setIdGroup(rs.getInt(5));
               value.add(p);
           }
       }catch(Exception ex){
           throw new Exception(ex.getMessage());
       }finally{
           stmt.close();
           rs.close();
           conn.close();
       }      
      return value;
   }
   public void deleteProduct(int id) throws Exception{
       PreparedStatement pr = null;
       Connection con = null;
       try{
           String sql = "delete Products where id = ?";
           con = DBUtil.connectMysql();
           pr = con.prepareStatement(sql);
           pr.setInt(1, id);
           pr.executeUpdate();
       }catch(Exception ex){
           throw new Exception(ex.getMessage());
       }finally{
           pr.close();
           con.close();
       }
   }
}
