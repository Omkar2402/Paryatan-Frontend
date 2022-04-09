package com.example.sihfrontend.streaming.getStreams;

public class StreamDetails {

    private String monument_name;
    private String date;
    private String time;
    private String resource_uri;

    public StreamDetails() {
    }

    public StreamDetails(String monument_name, String date, String time, String resource_uri) {
        this.monument_name = monument_name;
        this.date = date;
        this.time = time;
        this.resource_uri = resource_uri;
    }

    public String getMonument_name() {
        return monument_name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getResource_uri() {
        return resource_uri;
    }

    public void setMonument_name(String monument_name) {
        this.monument_name = monument_name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setResource_uri(String resource_uri) {
        this.resource_uri = resource_uri;
    }

    @Override
    public String toString() {
        return "StreamDetails{" +
                "monument_name='" + monument_name + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", resource_uri='" + resource_uri + '\'' +
                '}';
    }
}
