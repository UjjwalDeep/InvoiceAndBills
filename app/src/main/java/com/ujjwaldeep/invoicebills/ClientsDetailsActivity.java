package com.ujjwaldeep.invoicebills;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class ClientsDetailsActivity extends AppCompatActivity {
    private ImageView backImgBtn,saveImgButton;
    private EditText companyName ,gstin,billTo,companyAddress,
                        city,country,zipCode,chooseState,placeOfSupply;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clients_details);
        setupViews();
        setupFields();

        backImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        saveImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePref();
                setResult(RESULT_OK);
                finish();
            }
        });

    }

    private void setupFields() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        companyName.setText(sharedPreferences.getString("companyName",""));
        gstin.setText(sharedPreferences.getString("gstin",""));
        billTo.setText(sharedPreferences.getString("billTo",""));
        companyAddress.setText(sharedPreferences.getString("companyAddress",""));
        city.setText(sharedPreferences.getString("city",""));
        country.setText(sharedPreferences.getString("country",""));
        zipCode.setText(sharedPreferences.getString("zipCode",""));
        chooseState.setText(sharedPreferences.getString("chooseState",""));
        placeOfSupply.setText(sharedPreferences.getString("placeOfSupply",""));
    }


    private void sharePref() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        editor.putString("companyName",companyName.getText().toString());
        editor.putString("gstin",gstin.getText().toString());
        editor.putString("billTo",billTo.getText().toString());
        editor.putString("companyAddress",companyAddress.getText().toString());
        editor.putString("city",city.getText().toString());
        editor.putString("country",country.getText().toString());
        editor.putString("zipCode",zipCode.getText().toString());
        editor.putString("chooseState",chooseState.getText().toString());
        editor.putString("placeOfSupply",placeOfSupply.getText().toString());

        editor.commit();
    }

    private void setupViews() {
        backImgBtn = findViewById(R.id.backImgBtn);
        saveImgButton = findViewById(R.id.saveImgBtn);
        companyName = findViewById(R.id.clientCompanyNameText);
        gstin = findViewById(R.id.gstin);
        billTo = findViewById(R.id.billTo);
        companyAddress = findViewById(R.id.companyAddress);
        city = findViewById(R.id.city);
        country = findViewById(R.id.country);
        zipCode = findViewById(R.id.zipCode);
        chooseState = findViewById(R.id.chooseState);
        placeOfSupply = findViewById(R.id.placeOfSupply);

    }
}