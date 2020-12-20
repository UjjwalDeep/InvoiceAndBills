package com.ujjwaldeep.invoicebills.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ujjwaldeep.invoicebills.Database.MyClass;
import com.ujjwaldeep.invoicebills.Database.MyDbHandler;
import com.ujjwaldeep.invoicebills.MainActivity;
import com.ujjwaldeep.invoicebills.R;
import com.ujjwaldeep.invoicebills.UpdateItemData;

import java.util.List;

import static com.ujjwaldeep.invoicebills.Database.Params.TABLE_NAME;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsViewHolder> {

    private Context context;
    private List<MyClass> itemsList;

    public ItemsAdapter(){
    }

    public ItemsAdapter(Context context, List<MyClass> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_description_layout,parent,false);
        return new ItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemsViewHolder holder, final int position) {

        float a = Float.parseFloat(itemsList.get(position).getQuantity() )* Float.parseFloat(itemsList.get(position).getUnitCost());
        String amount = Float.toString(a);
        holder.amount.setText("₹ "+amount);
        holder.itemName.setText(itemsList.get(position).getItemName());
        holder.sgst.setText("₹ "+getPercent(itemsList.get(position).getSgst(),a));
        holder.cgst.setText("₹ "+getPercent(itemsList.get(position).getCgst(),a));
        holder.cess.setText("₹ "+getPercent(itemsList.get(position).getCess(),a));
        holder.id.setText(Integer.toString(itemsList.get(position).getId()));
        //Toast.makeText(context, Integer.toString(itemsList.get(position).getId()), Toast.LENGTH_SHORT).show();

        holder.removeItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDbHandler db = new MyDbHandler(context);
//                int newPosition = itemsList.get(position).getId();
                db.deleteItemFromDb(TABLE_NAME,itemsList.get(position).getId());
                itemsList.remove(itemsList.get(position));
                notifyItemRemoved(position);
                if(itemsList.size()!=0)
                notifyItemRangeChanged(position,itemsList.size());
                notifyDataSetChanged();
                ((MainActivity)context).itemCountString(getItemCount());
                ((MainActivity)context).setupData(itemsList);

       //I don't know why but, somehow the above code worked like a charm :P
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UpdateItemData.class);
                intent.putExtra("oldClassId",itemsList.get(position).getId());
                ((Activity)context).startActivityForResult(intent,4);

            }
        });
    }

    private String getPercent(String string, float a) {
        if(string.equals(""))
            return Float.toString(0f);

        float temp = Float.parseFloat(string);
        float res = (temp*a)/100;
        return Float.toString(res);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}
