package com.example.inhacsecapstone.serverconnect;
import okhttp3.OkHttpClient;

public class HttpConnection {
    private OkHttpClient client;
    private static HttpConnection instance = new HttpConnection();
    private static final String portNumber = "5000";
    private static final String ipv4Address = "192.168.25.39";
//    private static final String ipv4Address = "192.168.0.13";

    public static HttpConnection getInstance() {
        return instance;
    }
    private HttpConnection(){ this.client = new OkHttpClient(); }
    public String getUrl(String url) {
        return "http://" + ipv4Address + ":" + portNumber + "/"+url;
    }
}
