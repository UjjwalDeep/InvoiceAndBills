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

public class UpdateItemData extends AppCompatActivity {
    private ImageView backImgBtn,save;
    private EditText itemName,quantity,unitCost,hsn,sgst,cgst,cess;
    private int id ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        setupView();
        setupData();

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
                    myClass.setId(id);
                    myClass.setItemName(itemName.getText().toString());
                    myClass.setQuantity(quantity.getText().toString());
                    myClass.setUnitCost(unitCost.getText().toString());
                    myClass.setHsn(hsn.getText().toString());
                    myClass.setSgst(sgst.getText().toString());
                    myClass.setCgst(cgst.getText().toString());
                    myClass.setCess(cess.getText().toString());

                    MyDbHandler db = new MyDbHandler(UpdateItemData.this);
                    db.updateDb(myClass);

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", myClass.getId());
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
    private void setupData(){
        id =  getIntent().getExtras().getInt("oldClassId");
        MyDbHandler db = new MyDbHandler(this);
        MyClass myClass = db.getSingleItem(id);
        itemName.setText(myClass.getItemName());
        quantity.setText(myClass.getQuantity());
        unitCost.setText(myClass.getUnitCost());
        hsn.setText(myClass.getHsn());
        sgst.setText(myClass.getSgst());
        cgst.setText(myClass.getCgst());
        cess.setText(myClass.getCess());
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