package balet.benjamin.cinephoria;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import balet.benjamin.cinephoria.api.APIClient;
import balet.benjamin.cinephoria.api.CinephoriaAPI;
import balet.benjamin.cinephoria.model.TicketDetailResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activité pour le scan du QR Code
 */
public class ScanTicketActivity extends AppCompatActivity {

    private EditText txtQRCodeStatus;
    private Button cmdScanQRCode, cmdCloseQRCodeActivity;

    private ImageView imageView;
    private CinephoriaAPI apiInterface;


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
        this.imageView = findViewById(R.id.imageView);
        this.cmdScanQRCode.setOnClickListener(this::onClickScanQRCode);
        this.cmdCloseQRCodeActivity.setOnClickListener(v -> finish());
        apiInterface = APIClient.getClient().create(CinephoriaAPI.class);
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
        integrator.setBeepEnabled(true);
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
                try {
                    // QR code content est disponible ==> Parser le JSON
                    Gson gson = new Gson();
                    JsonObject obj = JsonParser.parseString(result.getContents()).getAsJsonObject();

                    // Extraire les valeurs
                    String ticketId = obj.get("ticketId").getAsString();
                    String movieTitle = obj.get("movieTitle").getAsString();
                    String day = obj.get("day").getAsString();
                    String roomNumber = obj.get("roomNumber").getAsString();
                    //String startDate = obj.get("startDate").getAsString();
                    JsonObject startDate = obj.getAsJsonObject("startDate");
                    Long timestamp = startDate.get("timestamp").getAsLong();
                    String firstName = obj.get("firstName").getAsString();
                    String lastName = obj.get("lastName").getAsString();
                    Integer nbTicketsInOrder = obj.get("nbTicketsInOrder").getAsInt();

                    // Parser la date sérialisée avec PHP et la mettre en Français
                    Instant instant = Instant.ofEpochSecond(timestamp);
                    ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy, HH:mm:ss", Locale.FRENCH);
                    String formattedDate = zonedDateTime.format(formatter);

                    // Vérifier avec l'API si le ticket est valide
                    checkTicketValidity(ticketId, timestamp);

                    this.txtQRCodeStatus.setText("Film: " + movieTitle + "\n");
                    this.txtQRCodeStatus.append("Date: " + formattedDate + "\n");
                    this.txtQRCodeStatus.append("Salle: " + roomNumber + "\n");
                    this.txtQRCodeStatus.append("Nombre de tickets: " + nbTicketsInOrder + "\n");
                    this.txtQRCodeStatus.append("Nom: " + firstName + " " + lastName + "\n");
                } catch (Exception e) {
                    imageView.setImageResource(R.drawable.failure);
                }

                // TODO : vérifier si on est connecté à internet
                // https://www.tutlane.com/tutorial/android/android-internet-connection-status-with-examples#:~:text=Check%20Internet%20Connection%20Status,connection%20is%20available%20or%20not.
            } else {
                // Echec du scan
                Toast.makeText(this, "Scan Failed", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Vérifie si le ticket est valide
     * @param ticketId Identifiant obsfuqué du ticket
     * @return
     */
    private void checkTicketValidity(String ticketId, Long timestamp) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("cinephoriaPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        Call<TicketDetailResponse> call = apiInterface.getTicket("Bearer " + token, ticketId);
        call.enqueue(new Callback<TicketDetailResponse>() {
            @Override
            public void onResponse(Call<TicketDetailResponse> call, Response<TicketDetailResponse> response) {
                boolean result = false;
                if (response.isSuccessful()) {
                    Date dateOfTicket = response.body().startDate;
                    Instant instant = dateOfTicket.toInstant();
                    result = (instant.getEpochSecond() == timestamp);
                }
                if (result) {
                    imageView.setImageResource(R.drawable.success);
                } else {
                    imageView.setImageResource(R.drawable.failure);
                }
            }

            @Override
            public void onFailure(Call<TicketDetailResponse> call, Throwable t) {
                call.cancel();
            }
        });

    }
}