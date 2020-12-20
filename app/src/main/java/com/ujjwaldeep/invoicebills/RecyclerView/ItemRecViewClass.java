package com.ujjwaldeep.invoicebills.RecyclerView;

public class ItemRecViewClass {
    private String productDescription,amount,sgst,cgst,cess,hsn;

    public ItemRecViewClass(){
    }

    public ItemRecViewClass(String productDescription, String amount, String sgst, String cgst, String cess, String hsn) {
        this.productDescription = productDescription;
        this.amount = amount;
        this.sgst = sgst;
        this.cgst = cgst;
        this.cess = cess;
        this.hsn = hsn;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }
}
