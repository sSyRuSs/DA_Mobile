package com.example.manager.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.DecimalFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.manager.Interface.IimageClickListenner;
import com.example.appbanhangonline.R;
import com.example.manager.model.EventBus.SumMonnyEvent;
import com.example.manager.model.GioHangModel;
import com.example.manager.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    public GioHangAdapter(Context context, List<GioHangModel> gioHangModelList) {
        this.context = context;
        this.gioHangModelList = gioHangModelList;
    }

    Context context;
    List<GioHangModel> gioHangModelList;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHangModel gioHangModel = gioHangModelList.get(position);
        holder.item_giohang_tensp.setText(gioHangModel.getTensp());
        holder.item_giohang_soluong.setText(gioHangModel.getSoluong()+ "");
        if (gioHangModel.getHinhsp().contains("http")){
            Glide.with(context).load(gioHangModel.getHinhsp()).into(holder.item_giohang_image);
        }else{
            String hinhanh = Utils.BASE_URL+"images/"+gioHangModel.getHinhsp();
            Glide.with(context).load(hinhanh).into(holder.item_giohang_image);
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.item_giohang_gia.setText(decimalFormat.format((gioHangModel.getGiasp())));
        long gia = gioHangModel.getSoluong() * gioHangModel.getGiasp();
        holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Utils.mangmuahang.add(gioHangModel);
                    EventBus.getDefault().postSticky(new SumMonnyEvent());
                }else {
                    for ( int i = 0; i < Utils.mangmuahang.size();i ++){
                        if (Utils.mangmuahang.get(i).getIdsp() == gioHangModel.getIdsp()){
                            Utils.mangmuahang.remove(i);
                            EventBus.getDefault().postSticky(new SumMonnyEvent());

                        }
                    }
                }
            }
        });
        holder.setListenner(new IimageClickListenner() {
            @Override
            public void onImageClick(View view, int pos, int giatri) {
                if (giatri == 1){
                    if (gioHangModelList.get(pos).getSoluong() > 1){
                        int soluongmoi = gioHangModelList.get(pos).getSoluong() - 1;
                        gioHangModelList.get(pos).setSoluong(soluongmoi);
                        holder.item_giohang_soluong.setText(gioHangModelList.get(pos).getSoluong()+ "");
                        long gia = gioHangModelList.get(pos).getSoluong() * gioHangModelList.get(pos).getGiasp();
                        holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
                        EventBus.getDefault().postSticky(new SumMonnyEvent());
                    }else if(gioHangModelList.get(pos).getSoluong() == 1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xóa sản phẩm ra khỏi giỏ hàng?");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.manggiohang.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new SumMonnyEvent());
                            }
                        });
                        builder.setNegativeButton("Huy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }

                }else if (giatri == 2){
                    if (gioHangModelList.get(pos).getSoluong() < 10){
                        int soluongmoi = gioHangModelList.get(pos).getSoluong() + 1;
                        gioHangModelList.get(pos).setSoluong(soluongmoi);
                    }
                    if (gioHangModelList.get(pos).getSoluong() == 10){
                        Toast.makeText(context, "BẠN ĐÃ THÊN QUÁ SỐ LƯỢNG CỬA HÀNG CHO PHÉP MUA ONLINE!", Toast.LENGTH_SHORT).show();
                    }
                    holder.item_giohang_soluong.setText(gioHangModelList.get(pos).getSoluong()+ "");
                    long gia = gioHangModelList.get(pos).getSoluong() * gioHangModelList.get(pos).getGiasp();
                    holder.item_giohang_giasp2.setText(decimalFormat.format(gia));
                    EventBus.getDefault().postSticky(new SumMonnyEvent());

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return gioHangModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView item_giohang_image,item_giohang_cong,item_giohang_tru;
        TextView item_giohang_tensp,item_giohang_gia,item_giohang_soluong,item_giohang_giasp2;
        IimageClickListenner listenner;
        CheckBox checkBox;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_giohang_image = itemView.findViewById(R.id.item_giohang_image);
            item_giohang_tensp = itemView.findViewById(R.id.item_giohang_tensp);
            item_giohang_gia = itemView.findViewById(R.id.item_giohang_gia);
            item_giohang_soluong = itemView.findViewById(R.id.item_giohang_soluong);
            item_giohang_giasp2 = itemView.findViewById(R.id.item_giohang_giasp2);
            item_giohang_cong = itemView.findViewById(R.id.item_giohang_cong);
            item_giohang_tru = itemView.findViewById(R.id.item_giohang_tru);
            checkBox = itemView.findViewById(R.id.item_giohang_check);
            item_giohang_cong.setOnClickListener(this);
            item_giohang_tru.setOnClickListener(this);


        }

        public void setListenner(IimageClickListenner listenner) {
            this.listenner = listenner;
        }

        @Override
        public void onClick(View v) {
            if ( v == item_giohang_tru)
            {
                listenner.onImageClick(v , getAdapterPosition(),1);
            }else if ( v == item_giohang_cong ){
                listenner.onImageClick(v,getAdapterPosition(),2);

            }
        }
    }
}
