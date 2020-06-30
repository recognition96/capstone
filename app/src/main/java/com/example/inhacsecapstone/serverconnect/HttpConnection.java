package com.example.inhacsecapstone.serverconnect;
import okhttp3.OkHttpClient;

public class HttpConnection {
    private static final String portNumber = "5000";
    private static final String ipv4Address = "192.168.25.39";
    private static HttpConnection instance = new HttpConnection();
    private OkHttpClient client;
//    private static final String ipv4Address = "192.168.0.13";

    private HttpConnection() {
        this.client = new OkHttpClient();
    }

    public static HttpConnection getInstance() {
        return instance;
    }

    public String getUrl(String url) {
        return "http://" + ipv4Address + ":" + portNumber + "/"+url;
//        return "https://3a313df8024e.ngrok.io/" + url ;
        }
}
