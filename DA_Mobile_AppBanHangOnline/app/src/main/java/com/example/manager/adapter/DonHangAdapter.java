package com.example.manager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhangonline.R;
import com.example.manager.Interface.ItemClickListener;
import com.example.manager.model.DonHang;
import com.example.manager.model.EventBus.DonHangEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<DonHang> listdonHang;

    public DonHangAdapter(Context context, List<DonHang> listdonHang) {
        this.context = context;
        this.listdonHang = listdonHang;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonHang donHang = listdonHang.get(position);
        holder.txtdonhang.setText("Đơn hàng: " + donHang.getId());
        holder.trangthai.setText("Trạng thái: "+trangThaiDonHang(donHang.getTrangthai()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.reChiciet.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(donHang.getItem().size());
        ChiTietAdapter chiTietAdapter = new ChiTietAdapter(context,donHang.getItem());
        holder.reChiciet.setLayoutManager(layoutManager);
        holder.reChiciet.setAdapter(chiTietAdapter);
        holder.reChiciet.setRecycledViewPool(viewPool);
        holder.setListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if ( isLongClick){
                    EventBus.getDefault().postSticky(new DonHangEvent(donHang));
                }
            }
        });
    }
    private String trangThaiDonHang(int status){
        String result = "";
        switch (status ){
            case 0:
                result = "Đơn hàng đang được sử lí";
                break;
            case 1:
                result = "Đơn hàng đã bị hủy";
                break;
            case 2:
                result = "Đơn hàng đang được đóng gói, chuẩn bị giao cho nhà vận chuyển";
                break;
            case 3:
                result = "Đơn hàng đã đến kho vận chuyển";
                break;
            case 4:
                result = "Shipper đã nhận hàng";
                break;
            case 5:
                result = "Đơn hàng đã được giao";
                break;
        }
        return result;
    }

    @Override
    public int getItemCount() {
        return listdonHang.size();
    }
    public  class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView txtdonhang,trangthai;
        RecyclerView reChiciet;
        ItemClickListener listener;

        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            txtdonhang = itemView.findViewById(R.id.iddonhang);
            trangthai = itemView.findViewById(R.id.trangthai);
            reChiciet = itemView.findViewById(R.id.recyclerView_chitiet);
            itemView.setOnLongClickListener(this);


        }

        public void setListener(ItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public boolean onLongClick(View v) {
            listener.onClick(v,getAdapterPosition(),true);
            return false;
        }
    }

}
