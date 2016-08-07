package com.proj.iptools.trace_route;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.proj.iptools.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class AsyncTask_Trace_Route extends AsyncTask<String, String, String> {

    private View TraceView;
    private ListView tracer_show;
    private ArrayList<String> trace_scanner_Result;
    private ArrayAdapter<String> arrayAdapter = null;
    private String target;
    private String result;
    private ProgressDialog progress;

    //Tracert
    private List<TracertContainer> traces;
    private static final String PING = "PING";
    private static final String FROM_PING = "From";
    private static final String SMALL_FROM_PING = "from";
    private static final String PARENTHESE_OPEN_PING = "(";
    private static final String PARENTHESE_CLOSE_PING = ")";
    private static final String TIME_PING = "time=";
    private static final String EXCEED_PING = "exceed";
    private static final String UNREACHABLE_PING = "100%";

    private final int maxTtl = 40;
    private int ttl = 1;
    private String ipToPing;
    private float elapsedTime;

    public AsyncTask_Trace_Route(View TraceView, String target) {
        this.TraceView = TraceView;
        this.target = target;

        tracer_show = (ListView) TraceView.findViewById(R.id.tracer_show);
        trace_scanner_Result = new ArrayList<String>();
        arrayAdapter = new Trace_Route_Adapter(TraceView.getContext(), R.layout.port_scanner_list_item, trace_scanner_Result);
        tracer_show.setAdapter(arrayAdapter);
    }

    /**
     * Launches ping command
     *
     * @param url
     *            The url to ping
     * @return The ping string
     */
    @SuppressLint("NewApi")
    private String launchPing(String url) throws Exception {
        // Build ping command with parameters
        Process p;
        String command = "";

        String format = "ping -c 1 -t %d ";
        command = String.format(format, ttl);


        long startTime = System.nanoTime();
        // Launch command
        p = Runtime.getRuntime().exec(command + url);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

        // Construct the response from ping
        String s;
        String res = "";
        while ((s = stdInput.readLine()) != null) {
            res += s + "\n";
            if (s.contains(FROM_PING) || s.contains(SMALL_FROM_PING)) {
                // We store the elapsedTime when the line from ping comes
                elapsedTime = (System.nanoTime() - startTime) / 1000000.0f;
            }
        }

        p.destroy();

        if (res.equals("")) {
            throw new IllegalArgumentException();
        }

        // Store the wanted ip address to compare with ping result
        if (ttl == 1) {
            ipToPing = parseIpToPingFromPing(res);
        }

        return res;
    }

    /**
     * Gets the ip from the string returned by a ping
     *
     * @param ping
     *            The string returned by a ping command
     * @return The ip contained in the ping
     */
    private String parseIpFromPing(String ping) {
        String ip = "";
        if (ping.contains(FROM_PING)) {
            // Get ip when ttl exceeded
            int index = ping.indexOf(FROM_PING);

            ip = ping.substring(index + 5);
            if (ip.contains(PARENTHESE_OPEN_PING)) {
                // Get ip when in parenthese
                int indexOpen = ip.indexOf(PARENTHESE_OPEN_PING);
                int indexClose = ip.indexOf(PARENTHESE_CLOSE_PING);

                ip = ip.substring(indexOpen + 1, indexClose);
            } else {
                // Get ip when after from
                ip = ip.substring(0, ip.indexOf("\n"));
                if (ip.contains(":")) {
                    index = ip.indexOf(":");
                } else {
                    index = ip.indexOf(" ");
                }

                ip = ip.substring(0, index);
            }
        } else {
            // Get ip when ping succeeded
            int indexOpen = ping.indexOf(PARENTHESE_OPEN_PING);
            int indexClose = ping.indexOf(PARENTHESE_CLOSE_PING);

            ip = ping.substring(indexOpen + 1, indexClose);
        }

        return ip;
    }

    /**
     * Gets the final ip we want to ping (example: if user fullfilled google.fr, final ip could be 8.8.8.8)
     *
     * @param ping
     *            The string returned by a ping command
     * @return The ip contained in the ping
     */
    private String parseIpToPingFromPing(String ping) {
        String ip = "";
        if (ping.contains(PING)) {
            // Get ip when ping succeeded
            int indexOpen = ping.indexOf(PARENTHESE_OPEN_PING);
            int indexClose = ping.indexOf(PARENTHESE_CLOSE_PING);

            ip = ping.substring(indexOpen + 1, indexClose);
        }

        return ip;
    }

    /**
     * Gets the time from ping command (if there is)
     *
     * @param ping
     *            The string returned by a ping command
     * @return The time contained in the ping
     */
    private String parseTimeFromPing(String ping) {
        String time = "";
        if (ping.contains(TIME_PING)) {
            int index = ping.indexOf(TIME_PING);

            time = ping.substring(index + 5);
            index = time.indexOf(" ");
            time = time.substring(0, index);
        }

        return time;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(TraceView.getContext());
        progress.setMessage("Loading data");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progress.show();
    }

    @Override
    protected String doInBackground(String... urls) {
        String result = "";
        try {
            String res = launchPing(target);
            TracertContainer trace;

            if (res.contains(UNREACHABLE_PING) && !res.contains(EXCEED_PING)) {
                // Create the TracerouteContainer object when ping
                // failed
                trace = new TracertContainer("", parseIpFromPing(res), elapsedTime, false);
            } else {
                // Create the TracerouteContainer object when succeed
                trace = new TracertContainer("", parseIpFromPing(res), ttl == maxTtl ? Float.parseFloat(parseTimeFromPing(res)) : elapsedTime, true);
            }

            // Get the host name from ip (unix ping do not support
            // hostname resolving)
            InetAddress inetAddr = InetAddress.getByName(trace.getIp());
            String hostname = inetAddr.getHostName();
            trace.setHostname(hostname);

            result = "Host  : " + trace.getHostname() + "\n" +
                     "IP       : " + trace.getIp() + "\n" +
                     "Time  : " + trace.getMs();


            trace_scanner_Result.add(result);

        } catch (final Exception e) {
        }
        return null;
    }

    protected void onProgressUpdate(String... values) {
    }

    @Override
    protected void onPostExecute(String unused) {
        arrayAdapter.notifyDataSetChanged();
        progress.dismiss();
    }
}
