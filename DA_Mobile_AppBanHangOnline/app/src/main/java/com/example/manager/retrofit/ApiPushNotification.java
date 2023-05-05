package com.example.manager.retrofit;

import com.example.manager.model.NotiResponse;
import com.example.manager.model.NotiSenddata;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiPushNotification {
    @Headers(
            {
                "Content-Type: application/json",
                "Authorization: key = AAAAo0hM9n8:APA91bHnpd4dUiytDuCu7G6y4IcQ_HRU9CCFD-JMpmgahZteteh3g6r-lZKdXxk8gQ3j4UjFQ6pIB6WiQ_nJ27R9SPV96nZOcPbDOlHNGhcR-hxURcHU2CiIK2Y27pQyiiWbM9_xDEGE"
            }

    )
    @POST("fcm/send")
    Observable<NotiResponse> sendNotification(@Body NotiSenddata notiSenddata);
}
