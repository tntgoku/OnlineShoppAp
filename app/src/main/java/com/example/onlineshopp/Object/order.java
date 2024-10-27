package com.example.onlineshopp.Object;

import java.io.Serializable;
import java.util.List;

public class order implements Serializable {
    private String idoder,idUser;
    private  int Amount,Total;
    private List<cartItem> cartItemList;
    private  String payment,orderstatus;
    private  String address,phone,time,name;

    public order(String idoder, String idUser, int amount, int total, List<cartItem> cartItemList, String payment,
                 String orderstatus, String address, String phone,String time) {
        this.idoder = idoder;
        this.idUser = idUser;
        Amount = amount;
        Total = total;
        this.cartItemList = cartItemList;
        this.payment = payment;
        this.orderstatus = orderstatus;
        this.address = address;
        this.phone = phone;
        this.time=time;
    }
    public order(String idoder, String idUser, int amount, int total, List<cartItem> cartItemList, String payment,
                 String orderstatus, String address, String phone,String time,String name){
        this.idoder = idoder;
        this.idUser = idUser;
        Amount = amount;
        Total = total;
        this.cartItemList = cartItemList;
        this.payment = payment;
        this.orderstatus = orderstatus;
        this.address = address;
        this.phone = phone;
        this.time=time;
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIdoder() {
        return idoder;
    }

    public void setIdoder(String idoder) {
        this.idoder = idoder;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public List<cartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<cartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
