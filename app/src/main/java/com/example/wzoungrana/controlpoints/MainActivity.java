package com.example.wzoungrana.controlpoints;

import android.content.Intent;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.example.wzoungrana.controlpoints.service.Session;

public class MainActivity extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {

    private static boolean isConnected = false;
    private QRCodeReaderView qrCodeReaderView;
    private RelativeLayout layoutqrcode;
    private ImageView scanImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Session.init(this);
        if (!isConnected) {
            // redirect to LoginActivity
            Session.deconnexion();
        } else {
            scanImg = findViewById(R.id.scan_img);
            qrCodeReaderView = findViewById(R.id.qrdecoderviewpaye);
            layoutqrcode = findViewById(R.id.layoutqrcode);

            // Parametrage du scanner
            qrCodeReaderView.setOnQRCodeReadListener(this);
            qrCodeReaderView.setQRDecodingEnabled(false);
            qrCodeReaderView.setAutofocusInterval(2000L);
            qrCodeReaderView.setTorchEnabled(true);
            qrCodeReaderView.setFrontCamera();
            qrCodeReaderView.setBackCamera();

            //Set dynamic content
            scanImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layoutqrcode.setVisibility(View.VISIBLE);
                    scanImg.setVisibility(View.INVISIBLE);
                    qrCodeReaderView.setQRDecodingEnabled(true);
                }
            });
        }
    }

    public static void setIsConnected(boolean isConnected) {
        MainActivity.isConnected = isConnected;
    }

    @Override
    public void onQRCodeRead(String qrCode, PointF[] points) {
        onPreExecute();
        System.out.println("+++++++++++++++qrString:: " + qrCode);

        if ( qrCode != null ) {
            qrCodeReaderView.setQRDecodingEnabled(false);

        } else {
            Toast.makeText(getApplicationContext(), "QR Code Read Failed !", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }


    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }

    protected void onPreExecute() {
        onPostExecute();
        //mDialog = ProgressDialog.show(this, "", this.getString(R.string.wait_please), true);
    }

    @Override
    protected void onDestroy() {
        onPostExecute();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent qr1 =  new Intent(this, LoginActivity.class);
        startActivity(qr1);
    }

    protected void onPostExecute() {
        /*try {
            if ((mDialog != null) && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        } catch (Exception ex) {
            Log.e("EXCEPTION", ex.getMessage(), ex);
        }*/
    }
}
