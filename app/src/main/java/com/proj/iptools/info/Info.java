package com.proj.iptools.info;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;

import org.json.JSONObject;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class Info {

    private Context context;
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;
    private DhcpInfo dhcpInfo;
    /* general_IP_Information */
    private String external_ip;
    private String isp;
    private String organization;

    /*geolocation_Information*/
    private String country;
    private String state_region;
    private String city;
    private double latitude;
    private double longitude;

    /*wifi_information*/
    private String wireless_network_name; //ESSID
    private int channel;
    private String security;
    private int frequency;
    private int speed;
    private String connection_type;
    private String signal_strength;
    private String mac_address_access_point; //BSSID
    private String subnet_mask;
    private String default_gateway;
    private String dns_1;
    private String dns_2;
    private String server_address;

    private List<ScanResult> scanResults;
    private static HashMap<Integer, Integer> channel_Frequencies;
    static
    {
        //* reference : http://www.radio-electronics.com/info/wireless/wi-fi/80211-channels-number-frequencies-bandwidth.php
        channel_Frequencies = new HashMap<Integer, Integer>();
        channel_Frequencies.put(2412, 1);
        channel_Frequencies.put(2417, 2);
        channel_Frequencies.put(2422, 3);
        channel_Frequencies.put(2427, 4);
        channel_Frequencies.put(2432, 5);
        channel_Frequencies.put(2437, 6);
        channel_Frequencies.put(2442, 7);
        channel_Frequencies.put(2447, 8);
        channel_Frequencies.put(2452, 9);
        channel_Frequencies.put(2457, 10);
        channel_Frequencies.put(2462, 11);
        channel_Frequencies.put(2467, 12);
        channel_Frequencies.put(2472, 13);
        channel_Frequencies.put(2484, 14);
    }
    public static final String WPA = "WPA";
    public static final String WEP = "WEP";
    public static final String WPA2 = "WPA2";
    public static final String OPEN = "Open";

    private Info_Fragment info_fragment;
    private String[] wifiObject;
    private String[] result;
    public volatile boolean parsingComplete = true;

    public Info(Context context, WifiManager wifiManager, WifiInfo wifiInfo, DhcpInfo dhcpInfo) {
        this.external_ip = "None detected";
        this.isp = "None detected";
        this.organization = "None detected";
        this.country = "None detected";
        this.state_region = "None detected";
        this.city = "None detected";
        this.latitude = 0.0;
        this.longitude = 0.0;
        this.wireless_network_name = "None detected";
        this.security = "None detected";
        this.channel = 0;
        this.speed = 0;
        this.connection_type = "None detected";
        this.signal_strength = "None detected";
        this.frequency = 0;
        this.mac_address_access_point = "None detected";
        this.subnet_mask = "None detected";
        this.default_gateway = "None detected";
        this.dns_1 = "None detected";
        this.dns_2 = "None detected";
        this.server_address = "None detected";
        info_fragment = new Info_Fragment();
        this.wifiManager = wifiManager;
        this.wifiInfo = wifiInfo;
        this.dhcpInfo = dhcpInfo;
        this.context = context;
    }

    public String getExternal_ip() {
        return external_ip;
    }

    public void setExternal_ip(String external_ip) {
        this.external_ip = external_ip;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState_region() {
        return state_region;
    }

    public void setState_region(String state_region) {
        this.state_region = state_region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getWireless_network_name() {
        return wireless_network_name;
    }

    public void setWireless_network_name(String wireless_network_name) {
        this.wireless_network_name = wireless_network_name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getConnection_type() {
        return connection_type;
    }

    public void setConnection_type(String connection_type) {
        this.connection_type = connection_type;
    }

    public String getSignal_strength() {
        return signal_strength;
    }

    public void setSignal_strength(String signal_strength) {
        this.signal_strength = signal_strength;
    }

    public String getMac_address_access_point() {
        return mac_address_access_point;
    }

    public void setMac_address_access_point(String mac_address_access_point) {
        this.mac_address_access_point = mac_address_access_point;
    }

    public String getSubnet_mask() {
        return subnet_mask;
    }

    public void setSubnet_mask(String subnet_mask) {
        this.subnet_mask = subnet_mask;
    }

    public String getDefault_gateway() {
        return default_gateway;
    }

    public void setDefault_gateway(String default_gateway) {
        this.default_gateway = default_gateway;
    }

    public String getDns_1() {
        return dns_1;
    }

    public void setDns_1(String dns_1) {
        this.dns_1 = dns_1;
    }

    public String getDns_2() {
        return dns_2;
    }

    public void setDns_2(String dns_2) {
        this.dns_2 = dns_2;
    }

    public String getServer_address() {
        return server_address;
    }

    public void setServer_address(String server_address) {
        this.server_address = server_address;
    }

    /**
     * Check if there is any connectivity to a Wifi network
     * @param context
     * @return
     */
    public boolean isConnectedWifi(Context context){
        NetworkInfo info = info_fragment.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Check if there is any connectivity to a mobile network
     * @param context
     * @return
     */
    public boolean isConnectedMobile(Context context){
        NetworkInfo info = info_fragment.getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Check if there is fast connectivity
     * @param context
     * @return
     */
    public boolean isConnectedFast(Context context){
        NetworkInfo info = info_fragment.getNetworkInfo(context);
        return (info != null && info.isConnected() && isConnectionFast(info.getType(), info.getSubtype()));
    }

    /**
     * Check if the connection is fast
     * @param type
     * @param subType
     * @return
     */
    public static boolean isConnectionFast(int type, int subType){
        if(type == ConnectivityManager.TYPE_WIFI){
            return true;
        } else if(type == ConnectivityManager.TYPE_MOBILE){
            switch(subType){
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps
			/*
			 * Above API level 7, make sure to set android:targetSdkVersion
			 * to appropriate level to use these
			 */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    /**
     * get channel from frequency
     * @param frequency
     * @return
     */
    public static int getValueChannel(int frequency) {

        int channel = 0;
        Iterator it = channel_Frequencies.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            if (entry.getKey().toString().equalsIgnoreCase(String.valueOf(frequency))) {
                channel = Integer.parseInt(entry.getValue().toString());
            }
        }
        return channel;
    }

    /**
     * get secure of wifi
     * WEP, WAP, WAP2, Open
     * @param capabilities
     * @return
     */
    public String getEncryption(String capabilities) {
        String encryption = "Unknown";
        final String[] securityModes = { WEP, WPA, WPA2 };

        for (int i = securityModes.length - 1; i >= 0; i--) {
            if (capabilities.toLowerCase().contains(WEP.toLowerCase())) {
                encryption = WEP;
            } else if (capabilities.toLowerCase().contains(WPA2.toLowerCase())) {
                encryption = WPA2;
            } else if (capabilities.toLowerCase().contains(WPA.toLowerCase())) {
                encryption = WPA;
            } else
                encryption = OPEN;
        }
        return encryption;
    }

    /**
     * check signal level wifi
     * @param signal
     * @return
     */
    public String getSignalLevelString(int signal) {
        String str_signal = "";

        if (signal > 0 && signal < 1) {
            str_signal = "Poor";
        } else if (signal >= 1 && signal < 2) {
            str_signal = "Fair";
        } else if (signal >= 2 && signal < 3) {
            str_signal = " Average";
        } else if (signal >= 3 && signal < 4) {
            str_signal = "Good";
        } else {
            str_signal = "Excellent";
        }

        return  str_signal;
    }

    static String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @SuppressLint("NewApi")
    public void parserJsonData(String in) {
        try {

            JSONObject reader = new JSONObject(in);

            setExternal_ip(reader.getString("query"));
            setIsp(reader.getString("isp"));
            setOrganization(reader.getString("org"));
            setCountry(reader.getString("country"));
            setState_region(reader.getString("regionName"));
            setCity(reader.getString("city"));
            setLatitude(Double.parseDouble(reader.getString("lat")));
            setLongitude(Double.parseDouble(reader.getString("lon")));

            parsingComplete = false;

            //get current address;


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void setGeolocationData(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    URL url = new URL("http://ip-api.com/json");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    // Starts the query
                    conn.connect();
                    InputStream stream = conn.getInputStream();

                    String data = convertStreamToString(stream);

                    parserJsonData(data);
                    stream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public void setWifiData(){
        setWireless_network_name(wifiInfo.getSSID().substring(wifiInfo.getSSID().indexOf('"') + 1, wifiInfo.getSSID().lastIndexOf('"')));
        scanResults = wifiManager.getScanResults();
        int numberOfLevels=5;//use for signal strenght
        for (int i = 0; i < scanResults.size(); i ++) {
            if (scanResults.get(i).toString().contains(getWireless_network_name())) {
                setFrequency(scanResults.get(i).frequency);
                setSecurity(getEncryption(scanResults.get(i).capabilities));
                setSignal_strength(getSignalLevelString(WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels)));
                break;
            }
        }

        setSpeed(wifiInfo.getLinkSpeed());
        //setSignal_strength(String.valueOf(WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 5)));
        setChannel(getValueChannel(getFrequency()));
        setMac_address_access_point(wifiInfo.getBSSID());
        setSubnet_mask(Formatter.formatIpAddress(dhcpInfo.netmask));
        setDefault_gateway(Formatter.formatIpAddress(dhcpInfo.gateway));
        setDns_1(Formatter.formatIpAddress(dhcpInfo.dns1));
        setDns_2(Formatter.formatIpAddress(dhcpInfo.dns2));
        setServer_address(Formatter.formatIpAddress(dhcpInfo.serverAddress));

        if (isConnectedWifi(context)) {
            setConnection_type("WIFI");
        } else if (isConnectedMobile(context)) {
            setConnection_type("MOBILE");
        } else {

        }
    }
}