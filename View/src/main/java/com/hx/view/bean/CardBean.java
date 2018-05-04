package com.hx.view.bean;

/**
 * Created by wsq on 2016/5/6.
 */
public class CardBean {

    int id;

    String cardName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public CardBean(){

    }
    public CardBean(int id, String cardName){
        this.id = id;
        this.cardName = cardName;
    }
}
