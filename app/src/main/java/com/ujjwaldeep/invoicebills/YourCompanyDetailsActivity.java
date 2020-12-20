package com.ujjwaldeep.invoicebills;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class YourCompanyDetailsActivity extends AppCompatActivity {
    private ImageView backImgView,saveBtnImg;
    private EditText yourCompanyName,yourGstin,yourName,yourCompanyAddress,yourCity,yourCountry,yourState,yourZipCode;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_company_details);
        setupViews();
        setupFields();
        backImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        saveBtnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePref();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private void setupViews() {
        backImgView = findViewById(R.id.backImgBtn);
        saveBtnImg = findViewById(R.id.yourSaveBtnImg);
        yourCompanyName = findViewById(R.id.yourCompanyName);
        yourGstin = findViewById(R.id.yourGstin);
        yourName = findViewById(R.id.yourName);
        yourCompanyAddress = findViewById(R.id.yourCompanyAddress);
        yourCity = findViewById(R.id.yourCity);
        yourCountry = findViewById(R.id.yourCountry);
        yourState = findViewById(R.id.yourState);
        yourZipCode = findViewById(R.id.yourZipCode);
    }
    private void setupFields() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        yourCompanyName.setText(sharedPreferences.getString("yourCompanyName",""));
        yourGstin.setText(sharedPreferences.getString("yourGstin",""));
        yourName.setText(sharedPreferences.getString("yourName",""));
        yourCompanyAddress.setText(sharedPreferences.getString("yourCompanyAddress",""));
        yourCity.setText(sharedPreferences.getString("yourCity",""));
        yourCountry.setText(sharedPreferences.getString("yourCountry",""));
        yourState.setText(sharedPreferences.getString("yourState",""));
        yourZipCode.setText(sharedPreferences.getString("yourZipCode",""));

    }
    private void sharePref() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        editor.putString("yourCompanyName",yourCompanyName.getText().toString());
        editor.putString("yourGstin",yourGstin.getText().toString());
        editor.putString("yourName",yourName.getText().toString());
        editor.putString("yourCompanyAddress",yourCompanyAddress.getText().toString());
        editor.putString("yourCity",yourCity.getText().toString());
        editor.putString("yourCountry",yourCountry.getText().toString());
        editor.putString("yourZipCode",yourZipCode.getText().toString());
        editor.putString("yourState",yourState.getText().toString());


        editor.commit();
    }
}