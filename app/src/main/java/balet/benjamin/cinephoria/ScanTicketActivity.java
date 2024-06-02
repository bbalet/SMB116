package balet.benjamin.cinephoria;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Activité pour le scan du QR Code
 */
public class ScanTicketActivity extends AppCompatActivity {

    private EditText txtQRCodeStatus;
    private Button cmdScanQRCode, cmdCloseQRCodeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scan_ticket);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.txtQRCodeStatus = findViewById(R.id.txtQRCodeStatus);
        this.cmdCloseQRCodeActivity = findViewById(R.id.cmdCloseQRCodeActivity);
        this.cmdScanQRCode = findViewById(R.id.cmdScanQRCode);
        this.cmdScanQRCode.setOnClickListener(this::onClickScanQRCode);
        this.cmdCloseQRCodeActivity.setOnClickListener(this::onClickClose);

    }

    /**
     * Méthode appelée lors du click sur le bouton Scan QR Code
     * @param v
     */
    public void onClickScanQRCode(View v) {
        IntentIntegrator integrator = new IntentIntegrator(ScanTicketActivity.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scaner le ticket");
        integrator.setCameraId(0);  // Utiliser la caméra arrière
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    /**
     * Méthode appelée après le scan du QR Code
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                // QR code content is available
                this.txtQRCodeStatus.setText("QR Code Content: " + result.getContents());
                // You can now use the result.getContents() to process the QR code data
            } else {
                // QR code scan failed
                Toast.makeText(this, "Scan Failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Méthode appelée lors du click sur le bouton Close
     * @param v
     */
    public void onClickClose(View v) {
        finish();
    }
}