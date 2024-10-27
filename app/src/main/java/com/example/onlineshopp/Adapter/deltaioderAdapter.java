package com.example.onlineshopp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.onlineshopp.Object.cartItem;
import com.example.onlineshopp.R;

import java.util.List;

public class deltaioderAdapter extends ArrayAdapter<cartItem> {


    private Context context;
    private List<cartItem> cartItemList;

    public deltaioderAdapter(Context context, List<cartItem> cartItemList) {
        super(context,0, cartItemList);
        this.context=context;
        this.cartItemList=cartItemList;

    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.itemorderdetail, parent, false);
        }

        // Ánh xạ các thành phần trong layout item_cart.xml
        TextView itemName = convertView.findViewById(R.id.textViewTitleas);
        TextView itemQuantity = convertView.findViewById(R.id.textView17ss);
        TextView itemPrice = convertView.findViewById(R.id.textViewCostas);
        TextView itemtotal= convertView.findViewById(R.id.textView18ss);
        ImageView img=convertView.findViewById(R.id.imageViewas);
        if (itemName != null && itemQuantity != null && itemPrice != null) {
            // Lấy dữ liệu từ cartItemList tại vị trí hiện tại
            cartItem cartItem = cartItemList.get(position);

        itemName.setText(cartItem.getItem().getTitle());
        itemQuantity.setText("Số lượng: " + cartItem.getQuantity());
        itemPrice.setText("Giá: " + (cartItem.getItem().getPrice()) + " đ");
        itemtotal.setText( (cartItem.getItem().getPrice()*cartItem.getQuantity()) + " đ");
            Glide.with(convertView.getContext()).load(cartItem.getItem().getPicURL()).into(img);
        } else {
            Log.e("CartItemAdapter", "Failed to find views by ID in item_cart layout.");
        }

        return convertView;
    }
}
