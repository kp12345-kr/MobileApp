package com.example.onestopwellbeing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class HotYogaDetails extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CAMERA = 1;
    TextView scanResultsTV;
    Button scanButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotyoga);
        scanButton = findViewById(R.id.scan_button);
        scanResultsTV = findViewById(R.id.QR_Results);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(HotYogaDetails.this,
                                new String[]{Manifest.permission.CAMERA},
                                PERMISSION_REQUEST_CAMERA);
                    } else {
                        initQRCodeScanner();
                    }
                } else {
                    initQRCodeScanner();
                }
            }
        });
    }

    private void initQRCodeScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setOrientationLocked(true);
        integrator.setPrompt("Scan a QR Code");
        integrator.initiateScan();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initQRCodeScanner();
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show();
            } else {
                String scannedContent = result.getContents();
                if (scannedContent.startsWith("http://") || scannedContent.startsWith("https://")) {
                    if (scannedContent.toLowerCase().endsWith(".png") || scannedContent.toLowerCase().endsWith(".svg")) {
                        openURLWithConfirmation(scannedContent);
                    } else {
                        displayURL(scannedContent);
                    }
                } else {
                    scanResultsTV.setText("Scanned: " + scannedContent);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void openURLWithConfirmation(final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to open this URL?")
                .setPositiveButton("Open", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(browserIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void displayURL(final String url) {
        scanResultsTV.setText("Scanned URL: " + url);
        scanResultsTV.setPaintFlags(scanResultsTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        scanResultsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanResultsTV.setPaintFlags(scanResultsTV.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                openURLWithConfirmation(url);
            }
        });
    }
}