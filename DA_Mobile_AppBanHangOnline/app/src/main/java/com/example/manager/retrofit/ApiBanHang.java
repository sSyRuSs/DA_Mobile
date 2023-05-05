package com.example.manager.retrofit;

import com.example.manager.model.DonHangModel;
import com.example.manager.model.LoaiSpModel;
import com.example.manager.model.MasageModel;
import com.example.manager.model.SanPhamMoiModel;
import com.example.manager.model.ThongKeModel;
import com.example.manager.model.UserModel;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiBanHang {
    @GET("getloaisp.php")
    Observable<LoaiSpModel> getLoaiSp();
    @GET("getspmoi.php")
    Observable<SanPhamMoiModel> getSpMoi();


    @POST("chitiet.php")
    @FormUrlEncoded
   Observable<SanPhamMoiModel> getSanPham(
            @Field("page") int page,
            @Field("loai") int loai
    );
    @POST("dangki.php")
    @FormUrlEncoded
    Observable<UserModel> dangki(
                    @Field("email") String email,
                    @Field("pass") String pass,
                    @Field("username") String username,
                    @Field("phone") String phone,
                    @Field("uid") String uid

            );
    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangnhap(
            @Field("email") String email,
            @Field("pass") String pass
    );
    @POST("reset.php")
    @FormUrlEncoded
    Observable<UserModel> resetPass(
            @Field("email") String email
    );
    @POST("donhang.php")
    @FormUrlEncoded
    Observable<UserModel> createOder(
            @Field("email") String email,
            @Field("sdt") String sdt,
            @Field("tongtien") String tongtien,
            @Field("iduser") int id,
            @Field("diachi") String diachi,
            @Field("soluong") int soluong,
            @Field("chitiet") String chitiet
    );
    @POST("xemdonhang.php")
    @FormUrlEncoded
    Observable<DonHangModel> xemDonHang(
            @Field("iduser") int id
    );
    @POST("timkiem.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> search(
            @Field("search") String search
    );
    @POST("themsp.php")
    @FormUrlEncoded
    Observable<MasageModel> themsp(
            @Field("tensp") String tensp,
            @Field("gia") String gia,
            @Field("hinhanh") String hinhanh,
            @Field("mota") String phone,
            @Field("loai") int loai

    );
    @Multipart
    @POST("uploadimage.php")
    Call<MasageModel> uploadFile(@Part MultipartBody.Part file);
    @POST("xoasp.php")
    @FormUrlEncoded
    Observable<MasageModel> xoaSanPham(
            @Field("id") int id
    );
 @POST("suasp.php")
 @FormUrlEncoded
 Observable<MasageModel> suasp(
         @Field("tensp") String tensp,
         @Field("gia") String gia,
         @Field("hinhanh") String hinhanh,
         @Field("mota") String phone,
         @Field("loai") int idloai,
         @Field("id") int id

 );
 @POST("updatetoken.php")
 @FormUrlEncoded
 Observable<MasageModel> updatetoken(
         @Field("id") int id,
         @Field("token") String token

 );
 @POST("updatedialog.php")
 @FormUrlEncoded
 Observable<MasageModel> updateDialog(
         @Field("id") int id,
         @Field("trangthai") int trangthai

 );
 @POST("gettoken.php")
 @FormUrlEncoded
 Observable<UserModel> getToken(
         @Field("status") int status,
         @Field("iduser") int iduser

 );
 @GET("thongkesp.php")
 Observable<ThongKeModel> getThongkesp();

}
