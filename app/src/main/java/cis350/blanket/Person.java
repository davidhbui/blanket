package cis350.blanket;

import java.util.ArrayList;

/**
 * Created by aashishlalani on 4/2/15.
 */

public class Person {

    private int _id;
    private ArrayList<String> giftBasket;
    private String _productname;
    private int _quantity;

    public Person() {

    }

    public Person(int id, String productname, int quantity) {
        this._id = id;
        this.giftBasket = new ArrayList<String>();
        this._productname = productname;
        this._quantity = quantity;
    }

    public Person(String productname, int quantity) {
        this._productname = productname;
        this._quantity = quantity;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }

    public void setProductName(String productname) {
        this._productname = productname;
    }

    public String getProductName() {
        return this._productname;
    }

    public void setQuantity(int quantity) {
        this._quantity = quantity;
    }

    public int getQuantity() {
        return this._quantity;
    }

    public void addGift(String gift) {
        this.giftBasket.add(gift);
    }
}
