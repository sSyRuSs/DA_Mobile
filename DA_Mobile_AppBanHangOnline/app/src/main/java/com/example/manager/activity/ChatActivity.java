package com.example.manager.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.appbanhangonline.R;
import com.example.manager.adapter.ChatAdapter;
import com.example.manager.model.ChatMessage;
import com.example.manager.utils.Utils;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    int iduser;
    String iduser_str;
    RecyclerView recyclerView;
    ImageView imageSend;
    EditText editMess;
    FirebaseFirestore db;
    ChatAdapter adapter;
    List<ChatMessage> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        iduser = getIntent().getIntExtra("id",0);// id người nhận
        iduser_str = String.valueOf(iduser);
        initView();
        initControl();
        listenMess();
    }
    private void initControl() {
        imageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessToFireBase();
            }
        });
    }

    private void sendMessToFireBase() {
        String str_mess = editMess.getText().toString().trim();
        if (TextUtils.isEmpty(str_mess)){

        }else{
            HashMap<String,Object> message = new HashMap<>();
            message.put(Utils.SEND_ID,String.valueOf(Utils.user_current.getId()));
            message.put(Utils.RECEIVED_ID,iduser_str);
            message.put(Utils.MESS,str_mess);
            message.put(Utils.DATETIME,new Date());
            db.collection(Utils.PATH_CHAT).add(message);
            editMess.setText("");
        }
    }
    private void listenMess(){
        db.collection(Utils.PATH_CHAT)
                .whereEqualTo(Utils.SEND_ID, String.valueOf(Utils.user_current.getId()))
                .whereEqualTo(Utils.RECEIVED_ID,iduser_str)
                .addSnapshotListener(eventListener);
        db.collection(Utils.PATH_CHAT)
                .whereEqualTo(Utils.SEND_ID, iduser_str)
                .whereEqualTo(Utils.RECEIVED_ID,String.valueOf(Utils.user_current.getId()))
                .addSnapshotListener(eventListener);
    }
    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null){
            return;
        }
        if (value != null){
            int count = list.size();
            for ( DocumentChange documentChange : value.getDocumentChanges()){
                if ( documentChange.getType() == DocumentChange.Type.ADDED){
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.sendId = documentChange.getDocument().getString(Utils.SEND_ID);
                    chatMessage.receivededId = documentChange.getDocument().getString(Utils.RECEIVED_ID);
                    chatMessage.mess = documentChange.getDocument().getString(Utils.MESS);
                    chatMessage.dateObj = documentChange.getDocument().getDate(Utils.DATETIME);
                    chatMessage.datatime = format_date(documentChange.getDocument().getDate(Utils.DATETIME));
                    list.add(chatMessage);
                }
            }
            Collections.sort(list,(obj1, obj2)->obj1.dateObj.compareTo(obj2.dateObj));
            if ( count == 0){
                adapter.notifyDataSetChanged();
            }else{
                adapter.notifyItemRangeInserted(list.size(),list.size());
                recyclerView.smoothScrollToPosition(list.size()-1);
            }
        }

    };


    private String format_date(Date date){
        return new SimpleDateFormat("MMMM dd,yyyy- hh:mm a", Locale.getDefault()).format(date);
    }
    private void initView() {
        list = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerView_chat);
        imageSend = findViewById(R.id.img_chat);
        editMess = findViewById(R.id.edit_inputText);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ChatAdapter(getApplicationContext(),list,String.valueOf(Utils.user_current.getId()));
        recyclerView.setAdapter(adapter);


    }
}