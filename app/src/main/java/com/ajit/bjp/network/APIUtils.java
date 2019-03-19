package com.ajit.bjp.network;

public class APIUtils {


    public static final String BASE_URL = "https://spreadsheets.google.com";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

}
