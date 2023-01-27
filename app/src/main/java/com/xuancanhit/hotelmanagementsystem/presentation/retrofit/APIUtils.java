package com.xuancanhit.hotelmanagementsystem.presentation.retrofit;

public class APIUtils {
    public static final String BASE_URL = "http://192.168.56.1/hotel/";

    //Get and sent data from server
    public static DataClient getData() {
        return RetrofitClient.getClient(BASE_URL).create(DataClient.class);
    }
}
