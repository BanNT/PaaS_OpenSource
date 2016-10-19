/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import model.Register;
import model.RegisterModel;
import itplus.util.MessageUtil;
import itplus.util.ValidatorUtil;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author BanNT
 */
public class RegisterBean extends MessageUtil {

    private Register register, rowSelected;
    private String forcus = "fullname";
    private RegisterModel regModel;
    private ArrayList<Register> arr, listSelected;
    private boolean checkEnable = true;

    public RegisterBean() {
        register = new Register();
        regModel = new RegisterModel();
//        getAllData();
//        checkStatusButton();
    }

 

    public void add() {
        if (validateData()) {
            int id;
            try {
                id = regModel.addRegister(register);
                register.setId(id);
                //add to arraylist
//                arr.add(register);
                addSuccessMessage("Resigter Success.");
                register = new Register();
                //focus vao firstname de nguoi dung co the nhap tiep
                forcus = "fullname";
//                checkStatusButton();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
    }

//xu ly ham khi nhan nut add

    public void addPress() {
        //xoa trang du lieu tren giao dien nhap lieu
        register = new Register();
        //focus tro chuot vao ptu dau tien
        forcus = "firstname";
    }

    public boolean validateData() {
        if (!ValidatorUtil.isEmail(register.getEmail())) {
            addErrorMessage("Email must have abc@domainname format!");
            forcus = "email";
            return false;
        }
        return true;
    }

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public String getForcus() {
        return forcus;
    }

    public void setForcus(String forcus) {
        this.forcus = forcus;
    }

    public ArrayList<Register> getArr() {
        return arr;
    }

    public void setArr(ArrayList<Register> arr) {
        this.arr = arr;
    }

    public Register getRowSelected() {
        return rowSelected;
    }

    public void setRowSelected(Register rowSelected) {
        this.rowSelected = rowSelected;
    }

    public ArrayList<Register> getListSelected() {
        return listSelected;
    }

    public void setListSelected(ArrayList<Register> listSelected) {
        this.listSelected = listSelected;
    }

    public boolean isCheckEnable() {
        return checkEnable;
    }

    public void setCheckEnable(boolean checkEnable) {
        this.checkEnable = checkEnable;
    }
}
