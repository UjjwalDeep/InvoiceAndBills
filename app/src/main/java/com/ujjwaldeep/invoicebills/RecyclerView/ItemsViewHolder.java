package com.ujjwaldeep.invoicebills.RecyclerView;

import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ujjwaldeep.invoicebills.R;

public class ItemsViewHolder extends RecyclerView.ViewHolder {
    public TextView itemName,amount,hsn,sgst,cgst,cess,id;
    public Button removeItemBtn;
    public LinearLayout linearLayout;
    public ItemsViewHolder(@NonNull View itemView) {
        super(itemView);
        itemName = itemView.findViewById(R.id.productDescriptionInput);
        amount = itemView.findViewById(R.id.amountInput);
        //hsn = itemView.findViewById(R.id.hsnInput);
        sgst = itemView.findViewById(R.id.sgstInput);
        cgst = itemView.findViewById(R.id.cgstInput);
        cess = itemView.findViewById(R.id.cessInput);
        id = itemView.findViewById(R.id.id);
        removeItemBtn = itemView.findViewById(R.id.removeItemBtn);
        linearLayout = itemView.findViewById(R.id.linearLayout);

    }
}
