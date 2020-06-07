package com.example.inhacsecapstone.serverconnect;

public class HttpConnection {
    private static HttpConnection instance = new HttpConnection();
    private static final String portNumber = "5000";
    private static final String ipv4Address = "59.17.224.156";

    public static HttpConnection getInstance() {
        return instance;
    }

    public String getUrl(String url) {
        return "http://" + ipv4Address + ":" + portNumber + "/"+url;
    }
}
