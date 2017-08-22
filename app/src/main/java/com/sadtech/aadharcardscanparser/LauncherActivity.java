package com.sadtech.aadharcardscanparser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;

import java.util.ArrayList;

/**
 * Created by ISRO on 12/9/2016.
 */
public class LauncherActivity extends Activity {

    private static final int SCAN_AADHAR_CARD = 1001;
    Activity activity;
    Button btnScan;
    LinearLayout llAadhar;
    TextView txtName;
    TextView txtGender;
    TextView txtYOB;
    TextView txtAddress;
    TextView txtNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_launcher);
        initUI();

    }

    private void initUI() {
        btnScan = (Button) findViewById(R.id.button);
        llAadhar = (LinearLayout) findViewById(R.id.ll_aadhar);
        txtName = (TextView) findViewById(R.id.name);
        txtGender = (TextView) findViewById(R.id.gender);
        txtYOB = (TextView) findViewById(R.id.yob);
        txtAddress = (TextView) findViewById(R.id.address);
        txtNumber = (TextView) findViewById(R.id.number);
        llAadhar.setVisibility(View.INVISIBLE);
        initListener();
    }

    private void initListener() {
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llAadhar.setVisibility(View.INVISIBLE);
                loadAadharScanner();
            }
        });
    }

    public void loadAadharScanner() {
        ArrayList<String> formatList = new ArrayList<>();
        formatList.add(BarcodeFormat.QR_CODE.toString());
        Intent intent = new Intent(activity, ScannerActivity.class);
        intent.putExtra("FORMATS", formatList);
        intent.putExtra("FLASH", false);
        intent.putExtra("AUTO_FOCUS", true);
        activity.startActivityForResult(intent, SCAN_AADHAR_CARD);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SCAN_AADHAR_CARD) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("AadharCardFragment", "onActivityResult: " + data.getStringExtra("aadhar_xml"));
                AadharDetail aadharDetail = AadharDAO.getAadharDetailFromXML(data.getStringExtra("aadhar_xml"));
                txtName.setText("Name: "+aadharDetail.getName());
                txtGender.setText("Gender: "+aadharDetail.getGender());
                txtAddress.setText("Address: "+aadharDetail.getAddress());
                txtYOB.setText("DOB: "+aadharDetail.getDob());
                txtNumber.setText("Aadhar Number: "+aadharDetail.getAadharNumber());
                llAadhar.setVisibility(View.VISIBLE);
                //Do whatever with the POJO received above.
                //For eg. bindDataToForm(aadharDetail);
            }
        }
    }
}
