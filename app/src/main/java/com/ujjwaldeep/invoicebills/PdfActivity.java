package com.ujjwaldeep.invoicebills;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ujjwaldeep.invoicebills.Database.MyClass;
import com.ujjwaldeep.invoicebills.Database.MyDbHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PdfActivity extends AppCompatActivity {
    private String companyDetails,clientDetails,invoiceNo,dueDate,notes,terms;
    private String [] companyArray;
    private String [] clientArray;
    private SharedPreferences sharedPreferences;
    private PdfDocument.Page currentPage ;
    private String subTotal,totalCgst,totalSgst,totalCess,grandTotal;
    private ImageView pdfView;
    private int num;
    private String string;
    private Button shareBtn , emailBtn ;
    private File shareFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        getAllData();

        PdfDocument myPdfDocument = new PdfDocument();
        Paint myPaint = new Paint();
        PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo.Builder(250,400,1).create();
        PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);
        Canvas canvas = myPage1.getCanvas();

        myPaint.setTextSize(12.0f);

        myPaint.setTypeface(Typeface.DEFAULT_BOLD);
        Typeface font = ResourcesCompat.getFont(this,R.font.helvetica);
        myPaint.setTypeface(font);
        canvas.drawText("Invoice",163,40,myPaint);

        myPaint.setTypeface(Typeface.DEFAULT);
        myPaint.setTextSize(4.0f);

        myPaint.setTextScaleX(1.4f);

        canvas.drawText("Invoice No.# "+invoiceNo,164,83,myPaint);
        canvas.drawText("Due Date : "+dueDate,164,89,myPaint);

        //Typeface font = Typeface.createFromAsset(this.getAssets(),"/font/helvetica.otf");



        myPaint.setTypeface(ResourcesCompat.getFont(this,R.font.lato_regular));
        myPaint.setTextSize(4.0f);
        myPaint.setTypeface(font);
        myPaint.setColor(Color.BLACK);
        myPaint.setTextScaleX(1.4f);
        myPaint.setTypeface(Typeface.DEFAULT_BOLD);
        canvas.drawText(companyArray[0],10,35,myPaint);
        myPaint.setTypeface(Typeface.DEFAULT);
        myPaint.setColor(Color.GRAY);
        myPaint.setTypeface(ResourcesCompat.getFont(this,R.font.lato_regular));
        canvas.drawText(companyArray[1],10,39,myPaint);
        canvas.drawText(companyArray[2],10,43,myPaint);
        canvas.drawText(companyArray[3],10,47,myPaint);
        canvas.drawText(companyArray[4],10,51,myPaint);
        canvas.drawText(companyArray[5],10,55,myPaint);
        canvas.drawText(companyArray[6],10,59,myPaint);
        canvas.drawText(companyArray[7],10,63,myPaint);

        myPaint.setColor(Color.BLACK);
        myPaint.setTextScaleX(1.4f);
        myPaint.setTypeface(Typeface.DEFAULT_BOLD);
        canvas.drawText("Bill To",10,83,myPaint);
        myPaint.setTypeface(Typeface.DEFAULT);
        myPaint.setColor(Color.GRAY);
        canvas.drawText(clientArray[0],10,87,myPaint);
        canvas.drawText(clientArray[1],10,91,myPaint);
        canvas.drawText(clientArray[2],10,95,myPaint);
        canvas.drawText(clientArray[3],10,99,myPaint);
        canvas.drawText(clientArray[4],10,103,myPaint);
        canvas.drawText(clientArray[5],10,107,myPaint);
        canvas.drawText(clientArray[6],10,111,myPaint);
        canvas.drawText(clientArray[7],10,115,myPaint);

        int y =115;
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setColor(Color.DKGRAY);
        canvas.drawRect(10,y+20,myPageInfo1.getPageWidth()-10,y+35,myPaint);
        myPaint.setColor(Color.WHITE);
        canvas.drawText("Item Description",15,y+30,myPaint);
        canvas.drawText("Qty",100,y+30,myPaint);
        canvas.drawText("Rate",120,y+30,myPaint);
        canvas.drawText("SGST",145,y+30,myPaint);
        canvas.drawText("CGST",170,y+30,myPaint);
        canvas.drawText("Cess",195,y+30,myPaint);
        canvas.drawText("Amount",215,y+30,myPaint);
        myPaint.setColor(Color.BLACK);

        List<MyClass> itemList = new ArrayList<>();
        MyDbHandler db = new MyDbHandler(this);
        itemList = db.getItemFromDb();
        y+=50;
        currentPage = myPage1;
        canvas = currentPage.getCanvas();
        for(MyClass myClass:itemList){
            float a = Float.parseFloat(myClass.getQuantity() )* Float.parseFloat(myClass.getUnitCost());
            String amount = Float.toString(a);
            canvas.drawText(myClass.getItemName(),17,y,myPaint);
            canvas.drawText(myClass.getQuantity(),102,y,myPaint);
            canvas.drawText(myClass.getUnitCost(),122,y,myPaint);
            canvas.drawText(myClass.getSgst(),147,y,myPaint);
            canvas.drawText(myClass.getCgst(),172,y,myPaint);
            canvas.drawText(myClass.getCess(),197,y,myPaint);
            canvas.drawText(amount,217,y,myPaint);
            myPaint.setColor(Color.GRAY);
            canvas.drawLine(15,y+5,myPageInfo1.getPageWidth()-20,y+5,myPaint);
            myPaint.setColor(Color.BLACK);
            y+=20;

            if(y>=370){
                y=20;
                myPdfDocument.finishPage(currentPage);
                currentPage = myPdfDocument.startPage(myPageInfo1);
                canvas = currentPage.getCanvas();
            }

        }
        y+=20;
        canvas.drawText("Subtotal",195,y,myPaint);
        canvas.drawText(subTotal,217,y,myPaint);
        y+=10;
        if(y>=370){
            y=20;
            myPdfDocument.finishPage(currentPage);
            currentPage = myPdfDocument.startPage(myPageInfo1);
            canvas = currentPage.getCanvas();
        }

        canvas.drawText("SGST",195,y,myPaint);
        canvas.drawText(totalSgst,217,y,myPaint);
        y+=10;
        if(y>=370){
            y=20;
            myPdfDocument.finishPage(currentPage);
            currentPage = myPdfDocument.startPage(myPageInfo1);
            canvas = currentPage.getCanvas();
        }

        canvas.drawText("CGST",195,y,myPaint);
        canvas.drawText(totalCgst,217,y,myPaint);
        y+=10;
        if(y>=370){
            y=20;
            myPdfDocument.finishPage(currentPage);
            currentPage = myPdfDocument.startPage(myPageInfo1);
            canvas = currentPage.getCanvas();
        }

        canvas.drawText("Cess",195,y,myPaint);
        canvas.drawText(totalCess,217,y,myPaint);
        y+=10;
        if(y>=370){
            y=20;
            myPdfDocument.finishPage(currentPage);
            currentPage = myPdfDocument.startPage(myPageInfo1);
            canvas = currentPage.getCanvas();
        }

        canvas.drawText("Total",195,y,myPaint);
        myPaint.setTypeface(Typeface.DEFAULT_BOLD);
        canvas.drawText(grandTotal,217,y,myPaint);
        myPaint.setTypeface(Typeface.DEFAULT);
        y+=10;
        if(y>=370){
            y=20;
            myPdfDocument.finishPage(currentPage);
            currentPage = myPdfDocument.startPage(myPageInfo1);
            canvas = currentPage.getCanvas();
        }

        canvas.drawText("Notes",10,y,myPaint);
        y+=10;
        if(y>=370){
            y=20;
            myPdfDocument.finishPage(currentPage);
            currentPage = myPdfDocument.startPage(myPageInfo1);
            canvas = currentPage.getCanvas();
        }
        for(String line:notes.split("\n")) {
            canvas.drawText(line, 10, y, myPaint);
            y += myPaint.descent() - myPaint.ascent();
        }
        y+=20;
        if(y>=370){
            y=20;
            myPdfDocument.finishPage(currentPage);
            currentPage = myPdfDocument.startPage(myPageInfo1);
            canvas = currentPage.getCanvas();
        }
        canvas.drawText("Terms and Conditions",10,y,myPaint);
        y+=10;
        if(y>=370){
            y=20;
            myPdfDocument.finishPage(currentPage);
            currentPage = myPdfDocument.startPage(myPageInfo1);
            canvas = currentPage.getCanvas();
        }
        for(String line:terms.split("\n")) {
            canvas.drawText(line, 10, y, myPaint);
            y += myPaint.descent() - myPaint.ascent();
        }

        myPdfDocument.finishPage(currentPage);

        try {
            saveInFile(myPdfDocument);
            Toast.makeText(this, "Pdf created check storage/invoiceandbils", Toast.LENGTH_SHORT).show();
            //showPdf();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error occurred in pdf creation", Toast.LENGTH_SHORT).show();
        }

    }

    private void showPdf() {
//        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/invoiceBills/secondpdf.pdf");

        try {
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(shareFile,ParcelFileDescriptor.MODE_READ_ONLY));
            Bitmap bitmap,overlayBitmap;
            overlayBitmap = Bitmap.createBitmap(250,getResources().getDisplayMetrics().heightPixels,Bitmap.Config.ARGB_8888);

            final int pageCount = renderer.getPageCount();
            for(int i=0;i<pageCount;i++){
                PdfRenderer.Page page = renderer.openPage(i);
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;
                bitmap = Bitmap.createBitmap(250,height,Bitmap.Config.ARGB_8888);
                page.render(bitmap,null,null,PdfRenderer.Page.RENDER_MODE_FOR_PRINT);
              
                    // merging bitmaps
                   Canvas canvas = new Canvas(overlayBitmap);
                   canvas.drawBitmap(overlayBitmap,new Matrix(),null);
                   canvas.drawBitmap(bitmap,0,0,null);
               
                page.close();
            }
            pdfView.setImageBitmap(overlayBitmap);
            renderer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void saveInFile(PdfDocument myPdfDocument) throws IOException {
        File file = new File(Environment.getExternalStorageDirectory()+"/invoiceBills");
        file.mkdirs();
        Random random  = new Random();
        num = random.nextInt(1000000);
        string = "/Bill-" + num +".pdf";
        myPdfDocument.writeTo(new FileOutputStream(file+string));
        myPdfDocument.close();
        shareFile = new File(file+string);
    }

    private void getAllData() {
      //  pdfView = findViewById(R.id.pdfView);
        shareBtn = findViewById(R.id.shareBtn);
        emailBtn = findViewById(R.id.eMailBtn);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        companyDetails = sharedPreferences.getString("fullCompanyDetails","noData");
        clientDetails = sharedPreferences.getString("fullClientDetails","noData");

        companyArray = new String[]{sharedPreferences.getString("yourCompanyName","noData"),
                sharedPreferences.getString("yourGstin","noData"),
                sharedPreferences.getString("yourName","noData"),
                sharedPreferences.getString("yourCompanyAddress","noData"),
                sharedPreferences.getString("yourCity","noData"),
                sharedPreferences.getString("yourCountry","noData"),
                sharedPreferences.getString("yourState","noData"),
                sharedPreferences.getString("yourZipCode","noData")};

        clientArray = new String[]{sharedPreferences.getString("companyName",""),
                sharedPreferences.getString("gstin",""),
                sharedPreferences.getString("billTo",""),
                sharedPreferences.getString("companyAddress",""),
                sharedPreferences.getString("city",""),
                sharedPreferences.getString("chooseState",""),
                sharedPreferences.getString("country",""),
                sharedPreferences.getString("zipCode","")};

        String [] array ;
        array = getIntent().getStringArrayExtra("array");
        invoiceNo = array[0];
        dueDate = array[1];
        notes = array[2];
        terms = array[3];
       // Log.d("sharedPref","this "+companyDetails+" \n"+clientDetails);
        subTotal = sharedPreferences.getString("subTotal","noData");
        totalSgst = sharedPreferences.getString("totalSgst","noData");
        totalCgst = sharedPreferences.getString("totalCgst","noData");
        totalCess = sharedPreferences.getString("totalCess","noData");
        grandTotal = sharedPreferences.getString("grandTotal","noData");

    }

    public void share(View view) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", shareFile);
                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.setType("application/pdf");
                share.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                share.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(share , "Share via"));
            }else {

                Uri uri = Uri.fromFile(shareFile);
                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.setType("application/pdf");
                share.putExtra(Intent.EXTRA_STREAM,uri);
                startActivity(Intent.createChooser(share , "Share via"));

            }

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
    }

    public void email(View view) {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", shareFile);
                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.putExtra(Intent.EXTRA_SUBJECT, "Invoice");
                share.setPackage("com.google.android.gm");
                startActivity(share);
            }else{
                Uri uri = Uri.fromFile(shareFile);
                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_STREAM, uri);
                share.putExtra(Intent.EXTRA_SUBJECT, "Invoice");
                share.setPackage("com.google.android.gm");
                startActivity(share);
            }
        }catch (Exception e){
            Log.d("emailShareError", e.toString());
            Toast.makeText(this, "Gmail app not found", Toast.LENGTH_SHORT).show();
        }

    }

    public void viewPdf(View view) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                Uri uri = FileProvider.getUriForFile(this,getPackageName() + ".provider" , shareFile);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri,"application/pdf");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            }else {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(shareFile), "application/pdf");
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show();
        }
    }
}