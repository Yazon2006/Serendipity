package com.example.bel.softwarefactory.api;

import com.example.bel.softwarefactory.entities.AudioRecordEntity;
import com.example.bel.softwarefactory.entities.ChangePasswordRequest;
import com.example.bel.softwarefactory.entities.ChangeUserData;
import com.example.bel.softwarefactory.entities.LoginRequest;
import com.example.bel.softwarefactory.entities.PasswordRequest;
import com.example.bel.softwarefactory.entities.RegisterRequest;
import com.example.bel.softwarefactory.entities.ResultEntity;
import com.example.bel.softwarefactory.entities.UserEntity;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

public interface RestApi {

    @POST("upload_file.php")
    Observable<ResponseBody> upload(@Body RequestBody file);

    @GET("get_all_audio_list.php")
    Observable<List<AudioRecordEntity>> getAudioRecordsList();

    @POST("Register.php")
    Observable<ResultEntity> registerUser(@Body RegisterRequest registerRequest);

    @POST("FetchUserData.php")
    Observable<UserEntity> logInUser (@Body LoginRequest loginRequest);

    @POST("ChangePassword.php")
    Observable<ResultEntity> changePassword (@Body ChangePasswordRequest changePasswordRequest);

    @POST("RequestPassword.php")
    Observable<ResultEntity> requestPassword (@Body PasswordRequest passwordRequest);

    @POST("ChangeUserData.php")
    Observable<ResultEntity> changeUserData (@Body ChangeUserData changeUserData);

}
