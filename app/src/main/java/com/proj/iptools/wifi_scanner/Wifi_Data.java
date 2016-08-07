package com.proj.iptools.wifi_scanner;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class Wifi_Data {
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

    /**
     * get channel from frequency
     * @param frequency
     * @return
     */
    public int getValueChannel(int frequency) {
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
}
