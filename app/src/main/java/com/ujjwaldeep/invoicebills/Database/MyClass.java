package com.ujjwaldeep.invoicebills.Database;

import java.io.Serializable;

public class MyClass implements Serializable {
    private String itemName,quantity,unitCost,hsn,sgst,cgst,cess;
    private int id;

    public MyClass(){
    }

    public MyClass(String itemName, String quantity, String unitCost, String hsn, String sgst, String cgst, String cess, int id) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitCost = unitCost;
        if(hsn.isEmpty())
            this.hsn = "0";
        else
        this.hsn = hsn;

        if(sgst.equals(""))
            this.sgst = "0";
        else
            this.sgst = sgst;

        if(cgst.equals(""))
            this.cgst = "0";
        else
            this.cgst = cgst;

        if(cess.equals(""))
            this.cess = "0";
        else
            this.cess = cess;

        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(String unitCost) {
        this.unitCost = unitCost;
    }

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getCess() {
        return cess;
    }

    public void setCess(String cess) {
        this.cess = cess;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

