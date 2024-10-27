package com.example.onlineshopp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.onlineshopp.Object.ItemFood;
import com.example.onlineshopp.R;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class listviewAdapter extends ArrayAdapter<Bitmap> {
    private List<Bitmap> mlist;
    public listviewAdapter(@NonNull Context context, List<Bitmap> mlist) {
        super(context, 0,mlist);
        this.mlist=mlist;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.items1, parent, false);
        }

        // Lấy mục hiện tại từ danh sách

        // Liên kết dữ liệu với các thành phần trong layout
        TextView textView1 = convertView.findViewById(R.id.tvProductName);
        TextView textView2 = convertView.findViewById(R.id.tvProductPrice);
        TextView textView3 = convertView.findViewById(R.id.tvProductQuantity);
        ImageView img=convertView.findViewById(R.id.ivProductImage) ;
//        textView1.setText(currentItem.getTitle()); // Giả sử bạn có phương thức getName() trong ItemFood
//        textView2.setText(currentItem.getPrice()); // Giả sử bạn có phương thức getDescription() trong ItemFood
//        textView3.setText(currentItem.getSell()); // Giả sử bạn có phương thức getDescription() trong ItemFood
        img.setImageBitmap(mlist.get(position));
        return convertView;
    }

}
