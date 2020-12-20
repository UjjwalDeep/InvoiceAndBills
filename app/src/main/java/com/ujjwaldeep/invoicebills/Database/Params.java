package com.ujjwaldeep.invoicebills.Database;

public class Params {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "items-db";
    public static final String TABLE_NAME = "items_table";

    //keys jo table ke column header banenge
    //7 keys + 1 ID
    public static final String KEY_ID ="keyId";
    public static final String ITEM_NAME = "itemName";
    public static final String QUANTITY = "quantity";
    public static final String UNIT_COST = "unitCost";
    public static final String HSN = "hsn";
    public static final String SGST = "sgst";
    public static final String  CGST = "cgst";
    public static final String CESS = "cess";
}
