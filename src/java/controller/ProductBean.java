/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.PGroup;
import entity.Product;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.MessageUtil;
import model.ProductModel;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Administrator
 */
public class ProductBean {

    private ArrayList<PGroup> arr;
    private ArrayList<Product> arrProducts;
    private Product product;
    private ProductModel pmodel;

    public ProductBean() throws ParseException {
        product = new Product();
        arr = new ArrayList<>();
        pmodel = new ProductModel();
        //khoi tao du lieu gia
        PGroup p1 = new PGroup(1, "Dien Tu");
        PGroup p2 = new PGroup(2, "Dien Lanh");
        PGroup p3 = new PGroup(3, "Thoi trang");
        arr.add(p1);
        arr.add(p2);
        arr.add(p3);
        //
        arrProducts = new ArrayList<>();
        try {
            arrProducts = pmodel.getAllProduct();
            if(arrProducts.size()>0){
                product = arrProducts.get(0);
            }
        } catch (Exception ex) {
            Logger.getLogger(ProductBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//them moi product

    public void addProduct() {
        //validate du lieu nguoi dung
        if (validatedata()) {
            try {
                //them moi san pham o day
                pmodel.addProduct(product);
            } catch (Exception ex) {
                MessageUtil.errorMessage(ex.getMessage());
            }
            //Goi phuong thuc them phia model
            MessageUtil.successMessage("Product added!");
            //xoa trang du lieu tren giao dien them moi nhanh san pham khac
            product = new Product();
        }
    }

    public boolean validatedata() {
        if (product.getName().equals("")) {
            //thong bao loi buoc phai nhap
            MessageUtil.errorMessage("Product name must input!");
            return false;
        } else if (product.getPrice() == 0) {
            MessageUtil.errorMessage("Product Price must input!");
            return false;
        }
        return true;
    }
//xoa san pham
    public void remove(){
        //xoa khoi csdl: tu lam
        //xoa tren giao dien
        arrProducts.remove(product);
        MessageUtil.successMessage("Product deleted!");
    }
    
    public void resetData() {
        //code here
        product = new Product();
    }

    public ArrayList<PGroup> getArr() {
        return arr;
    }

    public void setArr(ArrayList<PGroup> arr) {
        this.arr = arr;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ArrayList<Product> getArrProducts() {
        return arrProducts;
    }

    public void setArrProducts(ArrayList<Product> arrProducts) {
        this.arrProducts = arrProducts;
    }

}
