package com.example.manager.adapter;

import android.content.Context;
import android.icu.text.DecimalFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhangonline.R;
import com.example.manager.model.Item;
import com.example.manager.utils.Utils;

import java.util.List;

public class ChiTietAdapter extends RecyclerView.Adapter<ChiTietAdapter.MyViewHolder> {

    Context context;
    List<Item> itemList;

    public ChiTietAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chitietdonhang,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.txtten.setText("Tên sản phẩm: "+ item.getTenSp());
        holder.txtsoluong.setText("Số lượng: "+item.getSoluong());
        if (item.getHinhAnh().contains("http")){
            Glide.with(context).load(item.getHinhAnh()).into(holder.imagechitiet);
        }else{
            String hinhanh = Utils.BASE_URL+"images/"+item.getHinhAnh();
            Glide.with(context).load(hinhanh).into(holder.imagechitiet);
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtdiachi.setText("Tổng tiền: "+decimalFormat.format(Double.parseDouble(item.getGia()))+" đ");


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imagechitiet;
        TextView txtten,txtsoluong,txtdiachi;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imagechitiet = itemView.findViewById(R.id.item_imagechitiet);
            txtten = itemView.findViewById(R.id.item_tenspchitiet);
            txtsoluong = itemView.findViewById(R.id.item_soluongchitiet);
            imagechitiet = itemView.findViewById(R.id.item_imagechitiet);
            txtdiachi = itemView.findViewById(R.id.item_diachi);
        }
    }
}
