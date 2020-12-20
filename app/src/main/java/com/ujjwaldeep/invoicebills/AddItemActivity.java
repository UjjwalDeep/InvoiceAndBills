package com.ujjwaldeep.invoicebills;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.ujjwaldeep.invoicebills.Database.MyClass;
import com.ujjwaldeep.invoicebills.Database.MyDbHandler;

import java.io.Serializable;
import java.util.HashMap;

public class AddItemActivity extends AppCompatActivity {
    private ImageView backImgBtn,save;
    private EditText itemName,quantity,unitCost,hsn,sgst,cgst,cess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        setupView();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemName.getText().toString().equals(""))
                    itemName.setError("Item name can't be empty");
                else if(quantity.getText().toString().equals(""))
                    quantity.setError("Quantity can't be empty");
                else if(unitCost.getText().toString().equals(""))
                    unitCost.setError("Unit Cost can't be empty");
                else {
                    MyClass myClass = new MyClass();
                    myClass.setItemName(itemName.getText().toString());
                    myClass.setQuantity(quantity.getText().toString());
                    myClass.setUnitCost(unitCost.getText().toString());

                    if(!hsn.getText().toString().equals(""))
                    myClass.setHsn(hsn.getText().toString());
                    else
                        myClass.setHsn("0.00");

                    if(!cgst.getText().toString().equals(""))
                        myClass.setCgst(cgst.getText().toString());
                    else
                        myClass.setCgst("0.00");

                    if(!sgst.getText().toString().equals(""))
                        myClass.setSgst(sgst.getText().toString());
                    else
                        myClass.setSgst("0.00");

                    if(!cess.getText().toString().equals(""))
                        myClass.setCess(cess.getText().toString());
                    else
                        myClass.setCess("0.00");

                    MyDbHandler db = new MyDbHandler(AddItemActivity.this);
                    db.addItemInDb(myClass);

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", myClass);
                    setResult(RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
        backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        
    }

    private void setupView() {
        save = findViewById(R.id.saveBtnItemAdd);
        backImgBtn = findViewById(R.id.backImgBtn);
        itemName = findViewById(R.id.itemNameEt);
        quantity = findViewById(R.id.quantityEt);
        unitCost = findViewById(R.id.unitCostEt);
        hsn = findViewById(R.id.hsnEt);
        sgst = findViewById(R.id.sgstEt);
        cgst = findViewById(R.id.cgstEt);
        cess = findViewById(R.id.cessEt);
    }
}