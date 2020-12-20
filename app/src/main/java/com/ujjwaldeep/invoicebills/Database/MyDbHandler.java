package com.ujjwaldeep.invoicebills.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ujjwaldeep.invoicebills.Database.Params.CESS;
import static com.ujjwaldeep.invoicebills.Database.Params.CGST;
import static com.ujjwaldeep.invoicebills.Database.Params.DB_NAME;
import static com.ujjwaldeep.invoicebills.Database.Params.DB_VERSION;
import static com.ujjwaldeep.invoicebills.Database.Params.HSN;
import static com.ujjwaldeep.invoicebills.Database.Params.ITEM_NAME;
import static com.ujjwaldeep.invoicebills.Database.Params.KEY_ID;
import static com.ujjwaldeep.invoicebills.Database.Params.QUANTITY;
import static com.ujjwaldeep.invoicebills.Database.Params.SGST;
import static com.ujjwaldeep.invoicebills.Database.Params.TABLE_NAME;
import static com.ujjwaldeep.invoicebills.Database.Params.UNIT_COST;

public class MyDbHandler extends SQLiteOpenHelper {


    public MyDbHandler(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String create = "CREATE TABLE " + TABLE_NAME +"("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + ITEM_NAME + " TEXT, "
                + QUANTITY + " TEXT, "
                + UNIT_COST + " TEXT, "
                + HSN + " TEXT, "
                + SGST + " TEXT, "
                + CGST + " INT, "
                + CESS + " TEXT "
                + ")";
        db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public void addItemInDb(MyClass myClass){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEM_NAME,myClass.getItemName());
        values.put(QUANTITY,myClass.getQuantity());
        values.put(UNIT_COST,myClass.getUnitCost());
        values.put(HSN,myClass.getHsn());
        values.put(SGST,myClass.getSgst());
        values.put(CGST,myClass.getCgst());
        values.put(CESS,myClass.getCess());

        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public List<MyClass> getItemFromDb(){
        List <MyClass> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT * FROM " + TABLE_NAME ;
        Cursor cursor = db.rawQuery(select,null);

        if(cursor.moveToFirst()){
            do{
                MyClass myClass = new MyClass();
                myClass.setId(cursor.getInt(0));
                myClass.setItemName(cursor.getString(1));
                myClass.setQuantity(cursor.getString(2));
                myClass.setUnitCost(cursor.getString(3));
                myClass.setHsn(cursor.getString(4));
                myClass.setSgst(cursor.getString(5));
                myClass.setCgst(cursor.getString(6));
                myClass.setCess(cursor.getString(7));
                itemList.add(myClass);
            }while (cursor.moveToNext());

        }
        db.close();
        return itemList;
    }

    public void deleteItemFromDb(String tableName , int id){
        SQLiteDatabase db = this.getWritableDatabase() ;
        db.delete(tableName,KEY_ID + "=?",new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAllItemsFromDb(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName,null,null);
        db.close();
    }

    public void updateDb(MyClass myClass){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "+TABLE_NAME+" SET " +ITEM_NAME+" = '"+myClass.getItemName()+"' , "+QUANTITY+" = '"+myClass.getQuantity()
        +"' , "+UNIT_COST+" = '"+myClass.getUnitCost()+"' , "+HSN+" = '"+myClass.getHsn()+"' , "+SGST+" = '"+myClass.getSgst()
                        +"' , "+CGST+" = '"+myClass.getCgst()+"' , "+CESS+" = '"+myClass.getCess()+"' WHERE "+KEY_ID+" = "+myClass.getId()
        );
        Log.d("databaseUpdate","UPDATE "+TABLE_NAME+" SET " +ITEM_NAME+" = '"+myClass.getItemName()+"' , "+QUANTITY+" = '"+myClass.getQuantity()
                +"' , "+UNIT_COST+" = '"+myClass.getUnitCost()+"' , "+HSN+" = '"+myClass.getHsn()+"' , "+SGST+" = '"+myClass.getSgst()
                +"' , "+CGST+" = '"+myClass.getCgst()+"' , "+CESS+" = '"+myClass.getCess()+"' WHERE "+KEY_ID+" = "+myClass.getId());

        db.close();

    }

    public MyClass getSingleItem(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE "+KEY_ID+"="+id;
        Cursor cursor = db.rawQuery(query,null);
        MyClass myClass = new MyClass();

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            myClass.setItemName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
            myClass.setQuantity(cursor.getString(cursor.getColumnIndex(QUANTITY)));
            myClass.setUnitCost(cursor.getString(cursor.getColumnIndex(UNIT_COST)));
            myClass.setHsn(cursor.getString(cursor.getColumnIndex(HSN)));
            myClass.setSgst(cursor.getString(cursor.getColumnIndex(SGST)));
            myClass.setCgst(cursor.getString(cursor.getColumnIndex(CGST)));
            myClass.setCess(cursor.getString(cursor.getColumnIndex(CESS)));

        }
        db.close();
        return myClass;
    }

}
