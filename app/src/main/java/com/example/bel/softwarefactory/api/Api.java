package com.example.bel.softwarefactory.api;

import com.example.bel.softwarefactory.entities.AudioRecordEntity;
import com.example.bel.softwarefactory.entities.ChangePasswordRequest;
import com.example.bel.softwarefactory.entities.ChangeUserData;
import com.example.bel.softwarefactory.entities.LoginRequest;
import com.example.bel.softwarefactory.entities.PasswordRequest;
import com.example.bel.softwarefactory.entities.RegisterRequest;
import com.example.bel.softwarefactory.entities.ResultEntity;
import com.example.bel.softwarefactory.entities.UserEntity;
import com.example.bel.softwarefactory.utils.AppConstants;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class Api {

    private final RestApi restApi;

    public Api() {
        restApi = initRestAdapter();
    }

    // private methods

    private RestApi initRestAdapter() {
        final CookieManager manager = new CookieManager();
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()));
        gsonBuilder.registerTypeAdapter(Date.class, (JsonSerializer<Date>) (src, typeOfSrc, context) -> src == null ? null : new JsonPrimitive(src.getTime()));

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(60, TimeUnit.SECONDS);

        httpClient.addInterceptor(logging);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient.build())
                .baseUrl(AppConstants.SERVER_ADDRESS)
                .build();

        return retrofit.create(RestApi.class);
    }

    public Observable<ResponseBody> upload(RequestBody file) {
        return restApi.upload(file);
    }

    public Observable<List<AudioRecordEntity>> getAudioRecordsList() {
        return restApi.getAudioRecordsList();
    }

    public Observable<ResultEntity> registerUser(RegisterRequest registerRequest) {
        return restApi.registerUser(registerRequest);
    }

    public Observable<UserEntity> logInUser (LoginRequest loginRequest) {
        return restApi.logInUser(loginRequest);
    }

    public Observable<ResultEntity> changePassword (ChangePasswordRequest changePasswordRequest) {
        return restApi.changePassword(changePasswordRequest);
    }

    public Observable<ResultEntity> requestPassword (PasswordRequest passwordRequest) {
        return restApi.requestPassword(passwordRequest);
    }

    public Observable<ResultEntity> changeUserData (ChangeUserData changeUserData) {
        return restApi.changeUserData(changeUserData);
    }
}