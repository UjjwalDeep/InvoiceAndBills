package com.ujjwaldeep.invoicebills;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ujjwaldeep.invoicebills.Database.MyClass;
import com.ujjwaldeep.invoicebills.Database.MyDbHandler;
import com.ujjwaldeep.invoicebills.RecyclerView.ItemsAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.ujjwaldeep.invoicebills.Database.Params.TABLE_NAME;

public class MainActivity extends AppCompatActivity {

   private Button btn , addItemBtn;
   private String[] informationArray = new String[] {"Name","Company Name","Address","Phone","Email"};
   private PdfDocument.Page currentPage;
   private CardView clientCard,companyCard;
   private SharedPreferences sharedPreferences;
   private TextView clientDetails,companyDetailsText;
   private EditText dueDate;
   private RecyclerView recyclerView;
   private RecyclerView.Adapter mAdapter;
   private List<MyClass> templist;
   public TextView subTotal,totalSgst,totalCgst,totalCess,grandTotal;
   private EditText invoiceNoEt,notesEt,termsEt;
   private ImageButton cross;
   private TextView itemCountText;
   private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViewId();
        permission();

        setupData(templist);
        itemCountString(mAdapter.getItemCount());
        //createPdf();
        sharePrefClient();
        sharePrefCompany();



        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                String format = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
                dueDate.setText(sdf.format(calendar.getTime()));

            }
        };
        clientCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(MainActivity.this,ClientsDetailsActivity.class),1);

            }
        });
        companyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this,YourCompanyDetailsActivity.class),2);
            }
        });
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this,AddItemActivity.class),3);
            }
        });
        dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.myColorAccent));
                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.myColorAccent));
                dueDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(calendar.getTime()));
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkDetails()) {
                    Intent intent = new Intent(MainActivity.this, PdfActivity.class);
                    intent.putExtra("array", new String[]{invoiceNoEt.getText().toString(), dueDate.getText().toString(),
                            notesEt.getText().toString(), termsEt.getText().toString()
                    });
                    startActivity(intent);
                }
            }
        });

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Do you want to clear all present data?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                clearPreviousData();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
                Button nButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                nButton.setTextColor(getResources().getColor(R.color.myColorAccent));

                Button pButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                pButton.setTextColor(getResources().getColor(R.color.colorBlack));



            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.credits:
                //Toast.makeText(MainActivity.this, "credits", Toast.LENGTH_SHORT).show();
                final SpannableString s = new SpannableString("Developed and Designed by :\n" +
                        "       UJJWAL DEEP\n" +
                        "Contact at :\n" +
                        " ujjwaldeeprock123@gmail.com\n" +
                        "Github :\n" +
                        " https://github.com/UjjwalDeep");

                Linkify.addLinks(s,Linkify.ALL);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(s)
                        .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
                Button nButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                nButton.setTextColor(getResources().getColor(R.color.colorBlack));
                ((TextView)alert.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
                return true;

            case R.id.rate:
                Toast.makeText(MainActivity.this, "coming soon", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.ads:
                Toast.makeText(MainActivity.this, "coming soon ", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.version:
                //Toast.makeText(MainActivity.this, "version", Toast.LENGTH_SHORT).show();
                showErrorDialog("Version : 1.0");
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //    private void setMenu() {
//
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.credits:
//                        Toast.makeText(MainActivity.this, "credits", Toast.LENGTH_SHORT).show();
//                        return true;
//                    case R.id.rate:
//                        Toast.makeText(MainActivity.this, "rate", Toast.LENGTH_SHORT).show();
//                        return true;
//                    case R.id.ads:
//                        Toast.makeText(MainActivity.this, "ads", Toast.LENGTH_SHORT).show();
//                        return true;
//                    case R.id.version:
//                        Toast.makeText(MainActivity.this, "version", Toast.LENGTH_SHORT).show();
//                        return true;
//
//                }
//                return false;
//            }
//        });
//    }

    private boolean checkDetails() {

        if(clientDetails.getText().toString().isEmpty()){
            showErrorDialog("Client's detail can't be empty");
            return false;
        }else if(companyDetailsText.getText().toString().isEmpty()){
            showErrorDialog("Company details can't be empty");
            return false;
        }else if(mAdapter.getItemCount() == 0){
            showErrorDialog("Please add atleast one item");
            return false;
        } else if(invoiceNoEt.toString().isEmpty()){
            showErrorDialog("Please enter invoice no.");
            return false;
        } if(dueDate.getText().toString().isEmpty()){
            showErrorDialog("Please enter due date");
            return false;
        }
        else {
            return true;
        }

    }

    private void showErrorDialog(String s ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(s)
                .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
        Button nButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nButton.setTextColor(getResources().getColor(R.color.colorBlack));

    }

    public void itemCountString(int n){
        String string;
        if(n!=1) {
            string = "You have " + n + " items";
        }else {
            string = "You have 1 item";
        }
        itemCountText.setText(string);


    }


    private void clearPreviousData() {
        MyDbHandler db = new MyDbHandler(this);
        db.deleteAllItemsFromDb(TABLE_NAME);
        List<MyClass> newlist = new ArrayList<>();
        RecyclerView.Adapter adapter = new ItemsAdapter(this,newlist);
        recyclerView.swapAdapter(adapter,false);
        adapter.notifyItemRangeRemoved(0,templist.size());
        adapter.notifyDataSetChanged();
        SharedPreferences.Editor editor = sharedPreferences.edit().clear();
        editor.apply();
        clientDetails.setText("");
        companyDetailsText.setText("");
        notesEt.setText("");
        termsEt.setText("");
        invoiceNoEt.setText("");
        dueDate.setText("");
        subTotal.setText("");
        totalSgst.setText("");
        totalCgst.setText("");
        totalCess.setText("");
        grandTotal.setText("");
    }

    public  void setupData(List<MyClass> newList) {
        if(!newList.isEmpty()){
            float subTotal=0f,sgst=0f,cgst=0f,cess=0f,grandtotal=0f; // initial values is zero
            for(MyClass newClass : newList){

                float currentAmount = Float.parseFloat(newClass.getQuantity())*Float.parseFloat(newClass.getUnitCost());
                subTotal += currentAmount;
                sgst+= Float.parseFloat(newClass.getSgst())*currentAmount/100 ;
                cgst+= Float.parseFloat(newClass.getCgst())*currentAmount/100 ;
                cess+= Float.parseFloat(newClass.getCess())*currentAmount/100 ;

            }
            grandtotal = subTotal+sgst+cgst+cess;

            this.subTotal.setText("₹ "+String.format("%.2f",subTotal));
            this.totalSgst.setText("₹ "+String.format("%.2f",sgst));
            this.totalCgst.setText("₹ "+String.format("%.2f",cgst));
            this.totalCess.setText("₹ "+String.format("%.2f",cess));
            this.grandTotal.setText("₹ "+String.format("%.2f",grandtotal));

        }
        else {
            this.subTotal.setText("");
            this.totalSgst.setText("");
            this.totalCgst.setText("");
            this.totalCess.setText("");
            this.grandTotal.setText("");

        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("subTotal",subTotal.getText().toString());
        editor.putString("totalSgst",totalSgst.getText().toString());
        editor.putString("totalCgst",totalCgst.getText().toString());
        editor.putString("totalCess",totalCess.getText().toString());
        editor.putString("grandTotal",grandTotal.getText().toString());
        editor.commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode == RESULT_OK){
                clientDetails.setText("");
                sharePrefClient();

            }

        }
        if(requestCode==2){
            if(resultCode == RESULT_OK){
                companyDetailsText.setText("");
                sharePrefCompany();

            }
        }
        if(requestCode == 3 ){
            if(resultCode == RESULT_OK){
                this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                MyClass myClass = (MyClass) data.getSerializableExtra("result");
                permaData();
                List<MyClass> newList = templist;
                mAdapter.notifyItemInserted(myClass.getId());
                RecyclerView.Adapter adapter = new ItemsAdapter(this,newList);
                recyclerView.swapAdapter(adapter,false);
                adapter.notifyItemInserted(myClass.getId());

                setupData(newList);
                itemCountString(adapter.getItemCount());

            }
        }
        if(requestCode == 4 && resultCode == RESULT_OK){
            int id = data.getExtras().getInt("result");
            permaData();
            List<MyClass> newList = templist;
            RecyclerView.Adapter adapter = new ItemsAdapter(this,newList);
            recyclerView.swapAdapter(adapter,false);
            adapter.notifyItemChanged(id);
            adapter.notifyDataSetChanged();
            setupData(newList);
           // itemCountString(adapter.getItemCount());
        }
    }

    private void permaData() {
        MyDbHandler dbHandler = new MyDbHandler(this);
        templist = dbHandler.getItemFromDb();
        mAdapter = new ItemsAdapter(this,templist);
        mAdapter.notifyDataSetChanged();
        recyclerView.invalidate();
    }

    private void sharePrefClient() {
        //client card---------------------------------------------------------------------------
        clientDetails.append(sharedPreferences.getString("companyName", ""));
        if (!sharedPreferences.getString("companyName", "").isEmpty())
            clientDetails.append("\n");
        clientDetails.append(sharedPreferences.getString("gstin", ""));
        if (!sharedPreferences.getString("gstin", "").isEmpty())
            clientDetails.append("\n");
        clientDetails.append(sharedPreferences.getString("billTo", ""));
        if (!sharedPreferences.getString("billTo", "").isEmpty())
            clientDetails.append("\n");
        clientDetails.append(sharedPreferences.getString("companyAddress", ""));
        if (!sharedPreferences.getString("companyAddress", "").isEmpty())
            clientDetails.append("\n");
        clientDetails.append(sharedPreferences.getString("city", ""));
        if (!sharedPreferences.getString("city", "").isEmpty())
            clientDetails.append("\n");
        clientDetails.append(sharedPreferences.getString("country", ""));
        if (!sharedPreferences.getString("country", "").isEmpty())
            clientDetails.append("\n");
        clientDetails.append(sharedPreferences.getString("zipCode", ""));
        if (!sharedPreferences.getString("zipCode", "").isEmpty())
            clientDetails.append("\n");
        clientDetails.append(sharedPreferences.getString("chooseState", ""));
        if (!sharedPreferences.getString("chooseState", "").isEmpty())
            clientDetails.append("\n");
        clientDetails.append(sharedPreferences.getString("placeOfSupply", ""));

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fullClientDetails",clientDetails.getText().toString());
        editor.commit();

    }
        private void sharePrefCompany(){
        //company card---------------------------------------------------------------------

        companyDetailsText.append(sharedPreferences.getString("yourCompanyName",""));
        if(!sharedPreferences.getString("yourCompanyName","").isEmpty())
            companyDetailsText.append("\n");
        companyDetailsText.append(sharedPreferences.getString("yourGstin",""));
        if(!sharedPreferences.getString("yourGstin","").isEmpty())
            companyDetailsText.append("\n");
        companyDetailsText.append(sharedPreferences.getString("yourName",""));
        if(!sharedPreferences.getString("yourName","").isEmpty())
            companyDetailsText.append("\n");
        companyDetailsText.append(sharedPreferences.getString("yourCompanyAddress",""));
        if(!sharedPreferences.getString("yourCompanyAddress","").isEmpty())
            companyDetailsText.append("\n");
        companyDetailsText.append(sharedPreferences.getString("yourCity",""));
        if(!sharedPreferences.getString("yourCity","").isEmpty())
            companyDetailsText.append("\n");
        companyDetailsText.append(sharedPreferences.getString("yourCountry",""));
        if(!sharedPreferences.getString("yourCountry","").isEmpty())
            companyDetailsText.append("\n");
        companyDetailsText.append(sharedPreferences.getString("yourZipCode",""));
        if(!sharedPreferences.getString("yourZipCode","").isEmpty())
            companyDetailsText.append("\n");
        companyDetailsText.append(sharedPreferences.getString("yourState",""));

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("fullCompanyDetails",companyDetailsText.getText().toString());
            editor.commit();
//        if(!sharedPreferences.getString("yourState","").isEmpty())
//            companyDetailsText.append("\n");
//        companyDetailsText.append(sharedPreferences.getString("placeOfSupply",""));
    }



    private void permission() {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != (PackageManager.PERMISSION_GRANTED) ){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PackageManager.PERMISSION_GRANTED);
        }
    }

    private void setViewId() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cross = findViewById(R.id.clearAll);
        btn = findViewById(R.id.createPdf);
        clientCard = findViewById(R.id.clientCard);
        companyCard = findViewById(R.id.companyCard);
        addItemBtn = findViewById(R.id.addItemBtn);
        clientDetails = findViewById(R.id.clientDetailsData);
        companyDetailsText = findViewById(R.id.companyDetailsText);

        invoiceNoEt = findViewById(R.id.invoiceNoEtText);
        dueDate = findViewById(R.id.dueDateDate);
        notesEt = findViewById(R.id.notesEt);
        termsEt = findViewById(R.id.termsEt);
        itemCountText = findViewById(R.id.noItemsText);


        subTotal = findViewById(R.id.subtotalInput);
        totalSgst = findViewById(R.id.totalSgstInput);
        totalCgst = findViewById(R.id.totalCgstInput);
        totalCess = findViewById(R.id.totalCessInput);
        grandTotal = findViewById(R.id.grandTotalInput);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        templist = new ArrayList<>();
        MyDbHandler db = new MyDbHandler(this);
        templist=db.getItemFromDb();
        recyclerView = findViewById(R.id.itemListLayout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ItemsAdapter(this,templist);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


}