package com.proj.iptools.dns_lookup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.proj.iptools.R;

import java.util.ArrayList;

/**
 * Created by Jin.
 * Date : 5/18/2015
 * Time : 5:56 PM
 * Version : 1.0
 */
public class AsyncTask_DNS_lookup extends AsyncTask<String, String, String> {
    private Activity activity;
    private TableLayout mainTable;
    private View DNS_Lookup_View;
    private String target;
    private String lookup_type;
    private ProgressDialog progress;
    private DNS_Lookup dns_lookup;
    public ArrayList<String> recordsValue;
    public int recordsTTL;

    public AsyncTask_DNS_lookup(View DNS_Lookup_View, String target, String lookup_type) {
        this.DNS_Lookup_View = DNS_Lookup_View;
        activity = (Activity)DNS_Lookup_View.getContext();
        mainTable = (TableLayout) DNS_Lookup_View.findViewById(R.id.mainTable);
        this.target = target;
        this.lookup_type = lookup_type;
        dns_lookup = new DNS_Lookup(target, lookup_type);
        recordsValue = new ArrayList<String>();
        mainTable.removeAllViews();
    }

    /**
     * Add data into table
     * @param recordsValue
     * @param recordsTTL
     */
    private void addRow(ArrayList<String> recordsValue, int recordsTTL) {
        String str_title = "";
        String str_title_left = "";
        String str_title_right = "TTL";

        if (lookup_type.equalsIgnoreCase("ANY")) {
            str_title = lookup_type + " result for google.com";
        } else {
            str_title = lookup_type + " records for google.com";
        }

        switch (lookup_type) {
            case "A":
            case "AAAA":
                str_title_left = "IP";
                break;
            case "NS":
            case "MX":
                str_title_left = "Host";
                break;
            case  "TXT":
                str_title_left = "Text";
                break;
        }
        // create row title for table

        TableRow tr_header_title = new TableRow(activity);
        tr_header_title.setId(0 + 1);
        tr_header_title.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        TextView name_title = new TextView(activity);
        name_title.setId(1 + 1);
        name_title.setText("" + str_title + "");
        name_title.setGravity(Gravity.CENTER);
        name_title.setTextColor(Color.parseColor("#0099cc"));
        name_title.setPadding(5, 10, 25, 5);
        tr_header_title.addView(name_title);// add the column to the table row here

        mainTable.addView(tr_header_title, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        //End title table

        // create row title for each row
        TableRow tr_header = new TableRow(activity);
        tr_header.setId(2 + 1);
        tr_header.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        TextView name_left = new TextView(activity);
        name_left.setId(3 + 1);
        name_left.setText("" + str_title_left + "");
        name_left.setTypeface(null, Typeface.BOLD);
        name_left.setTextColor(Color.parseColor("#0099cc"));
        name_left.setPadding(5, 10, 25, 5);
        tr_header.addView(name_left);// add the column to the table row here

        TextView name_right = new TextView(activity);
        name_right.setId(4 + 1);
        name_right.setText("" + str_title_right + "");
        name_right.setTypeface(null, Typeface.BOLD);
        name_right.setTextColor(Color.parseColor("#0099cc"));
        name_right.setPadding(5, 10, 25, 5);
        tr_header.addView(name_right); // add the column to the table row here

        mainTable.addView(tr_header, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        //End title row

        // Go through each item in the array
        for (int i = 0; i < recordsValue.size(); i++) {
            // Create a TableRow and give it an ID
            TableRow tr_body = new TableRow(activity);
            tr_body.setId(10 + 1);
            tr_body.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            TextView name_left_body = new TextView(activity);
            name_left_body.setId(20 + 1);
            name_left_body.setText("" + recordsValue.get(i).toString() +  "");
            name_left_body.setTextColor(Color.parseColor("#0099cc"));
            name_left_body.setPadding(5, 5, 25, 5);
            tr_body.addView(name_left_body);// add the column to the table row here

            TextView name_right_body = new TextView(activity);
            name_right_body.setId(30 + 1);
            name_right_body.setText("" + recordsTTL +  "");
            name_right_body.setTextColor(Color.parseColor("#0099cc"));
            name_right_body.setPadding(5, 5, 25, 5);
            tr_body.addView(name_right_body); // add the column to the table row here

            mainTable.addView(tr_body, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(DNS_Lookup_View.getContext());
        progress.setMessage("Loading data");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progress.show();
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            dns_lookup.getRecords();
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return null;
    }

    protected void onProgressUpdate(String... values) {
    }

    @Override
    protected void onPostExecute(String unused) {
        recordsValue = dns_lookup.recordsValue;
        recordsTTL = dns_lookup.recordsTTL;
        addRow(recordsValue, recordsTTL);
        progress.dismiss();
    }
}
