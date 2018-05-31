package com.example.coamaster.coamasteruser;

public class Cart {

    String menu;
    int price;

    public Cart(String menu, int price) {

        this.menu = menu;
        this.price = price;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}