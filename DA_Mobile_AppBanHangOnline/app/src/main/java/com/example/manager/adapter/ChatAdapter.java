package com.example.manager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhangonline.R;
import com.example.manager.model.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<ChatMessage> chatMessagesList;
    private String sendId;
    private static final int TYPE_SEND = 1;
    private static final int TYPE_RECEIVED = 2;

    public ChatAdapter(Context context, List<ChatMessage> chatMessagesList, String sendId) {
        this.context = context;
        this.chatMessagesList = chatMessagesList;
        this.sendId = sendId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_SEND){
            view = LayoutInflater.from(context).inflate(R.layout.item_sendmess,parent ,false);
            return new SendMessViewHolder(view);

        }else{
            view = LayoutInflater.from(context).inflate(R.layout.item_received,parent ,false);
            return new ReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if ( getItemViewType(position) == TYPE_SEND){
            ((SendMessViewHolder)holder).txtmess.setText(chatMessagesList.get(position).mess);
            ((SendMessViewHolder)holder).txttime.setText(chatMessagesList.get(position).datatime);
        }else {
            ((ReceivedViewHolder)holder).txtmess.setText(chatMessagesList.get(position).mess);
            ((ReceivedViewHolder)holder).txttime.setText(chatMessagesList.get(position).datatime);
        }

    }

    @Override
    public int getItemCount() {
        return chatMessagesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if ( chatMessagesList.get(position).sendId.equals(sendId)){
            return TYPE_SEND;
        }else {
            return TYPE_RECEIVED;
        }
    }

    class SendMessViewHolder extends RecyclerView.ViewHolder{
        TextView txtmess,txttime;
        public SendMessViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmess = itemView.findViewById(R.id.txtmess);
            txttime = itemView.findViewById(R.id.timsend_text);
        }
    }
    class ReceivedViewHolder extends RecyclerView.ViewHolder{
        TextView txtmess,txttime;
        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmess = itemView.findViewById(R.id.txtmess);
            txttime = itemView.findViewById(R.id.timepost_text);
        }
    }
}
