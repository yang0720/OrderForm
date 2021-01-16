package com.qingmaiding.orderform.shop;

public class EventMessage  {
    private String keyword;
    public EventMessage(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
