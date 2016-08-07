package com.proj.iptools.whois;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.proj.iptools.R;

import org.apache.commons.net.WhoisClient;

import java.io.IOException;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class AsyncTask_Whois extends AsyncTask<String, String, String> {

    private View WhoisView;
    private TextView whois_Show;
    private String target;
    private String result;
    private ProgressDialog progress;
    private static Pattern pattern;
    private Matcher matcher;

    // regex whois parser
    private static final String WHOIS_SERVER_PATTERN = "Whois Server:\\s(.*)";
    static {
        pattern = Pattern.compile(WHOIS_SERVER_PATTERN);
    }


    public AsyncTask_Whois(View WhoisView, String target) {
        this.WhoisView = WhoisView;
        this.target = target;
        whois_Show = (TextView)WhoisView.findViewById(R.id.whois_Show);
    }

    // example google.com
    public void getWhois(String domainName) {

        StringBuilder result = new StringBuilder("");

        WhoisClient whois = new WhoisClient();
        try {

            whois.connect(WhoisClient.DEFAULT_HOST);

            String whoisData1 = whois.query("=" + domainName);

            // append first result
            result.append(whoisData1);
            whois.disconnect();

            // get the google.com whois server - whois.markmonitor.com
            String whoisServerUrl = getWhoisServer(whoisData1);
            if (!whoisServerUrl.equals("")) {
                // whois -h whois.markmonitor.com google.com
                String whoisData2 = queryWithWhoisServer(domainName, whoisServerUrl);
                // append 2nd result
                result.append(whoisData2);
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String last_result =  result.toString();
        last_result = last_result.substring(last_result.indexOf("https://www.icann.org/resources/pages/epp-status-codes-2014-06-16-en.") + 69).trim();
        this.result = last_result;
    }

    private String queryWithWhoisServer(String domainName, String whoisServer) {

        String result = "";
        WhoisClient whois = new WhoisClient();
        try {

            whois.connect(whoisServer);
            result = whois.query(domainName);
            whois.disconnect();

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }

    private String getWhoisServer(String whois) {

        String result = "";

        matcher = pattern.matcher(whois);

        // get last whois server
        while (matcher.find()) {
            result = matcher.group(1);
        }
        return result;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        whois_Show.setText("");
        progress = new ProgressDialog(WhoisView.getContext());
        progress.setMessage("Loading data");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progress.show();
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            getWhois(target);
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }

    protected void onProgressUpdate(String... values) {
    }

    @Override
    protected void onPostExecute(String unused) {
        whois_Show.setText("" + this.result + "");
        progress.dismiss();
    }
}
