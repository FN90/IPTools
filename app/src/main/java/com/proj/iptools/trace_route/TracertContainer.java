package com.proj.iptools.trace_route;

import java.io.Serializable;

/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class TracertContainer implements Serializable {

    private static final long serialVersionUID = 1034744411998219581L;

    private String hostname;
    private String ip;
    private float ms;
    private boolean isSuccessful;

    public TracertContainer(String hostname, String ip, float ms, boolean isSuccessful) {
        this.hostname = hostname;
        this.ip = ip;
        this.ms = ms;
        this.isSuccessful = isSuccessful;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public float getMs() {
        return ms;
    }

    public void setMs(float ms) {
        this.ms = ms;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    @Override
    public String toString() {
        return "Traceroute : \nHostname : " + hostname + "\nip : " + ip + "\nMilliseconds : " + ms;
    }
}
