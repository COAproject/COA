package com.example.coamaster.coamasteruser;

public class Order {

    String order;
    String condi;
    String date;

    public Order(String order, String condi, String date) {
        this.order = order;
        this.condi = condi;
        this.date = date;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getCondi() {
        return condi;
    }

    public void setCondi(String condi) {
        this.condi = condi;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
