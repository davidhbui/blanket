package cis350.blanket;

import java.util.ArrayList;

/**
 * Created by aashishlalani on 4/2/15.
 */

public class fbPerson {

    private int _id;
    private String _giftBasket;
    private String _personName;
    private int _quantity;

    public fbPerson() {

    }

    public fbPerson(int id, String personName, int quantity, String giftBasket) {
        this._id = id;
        this._giftBasket = giftBasket;
        this._personName = personName;
        this._quantity = quantity;
    }

    public fbPerson(String productname, int quantity) {
        this._personName = productname;
        this._quantity = quantity;
    }

    public void setID(int id) {
        this._id = id;
    }

    public int getID() {
        return this._id;
    }

    public void setProductName(String productname) {
        this._personName = productname;
    }

    public String getProductName() {
        return this._personName;
    }

    public void setQuantity(int quantity) {
        this._quantity = quantity;
    }

    public int getQuantity() {
        return this._quantity;
    }
    /*
        public void addGift(String gift) {
            if (this._giftBasket.equals("")) {
                this._giftBasket = "gift";
            } else {
                this._giftBasket += ", " + gift;
            }
        }
        */
    public String get_giftBasket() {
        return _giftBasket;
    }
}
