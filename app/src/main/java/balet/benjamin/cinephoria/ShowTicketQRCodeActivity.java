package balet.benjamin.cinephoria;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.UnsupportedEncodingException;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import balet.benjamin.cinephoria.model.TicketDetailResponse;
import balet.benjamin.cinephoria.api.APIClient;
import balet.benjamin.cinephoria.api.CinephoriaAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowTicketQRCodeActivity extends AppCompatActivity {

    private Button cmdCloseShowQRActivity;
    private TextView txtTicketDetail;
    private ImageView imgQRCodeTicket;
    private CinephoriaAPI apiInterface;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_ticket_qrcode);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cmdCloseShowQRActivity = findViewById(R.id.cmdCloseShowQRActivity);
        txtTicketDetail = findViewById(R.id.txtTicketDetail);
        imgQRCodeTicket = findViewById(R.id.imgQRCodeTicket);
        cmdCloseShowQRActivity.setOnClickListener(v -> finish());
        // Créer le client API
        apiInterface = APIClient.getClient().create(CinephoriaAPI.class);
        String ticketId = getIntent().getStringExtra("ticketId");
        Log.i(TAG, "onCreate: " + ticketId);
        getTicketDetails(ticketId);
    }

    /**
     * Récupère les détails d'un ticket et affiche le QR code
     * @param ticketId
     */
    private void getTicketDetails(String ticketId) {
        SharedPreferences sharedPreferences = this.getSharedPreferences("cinephoriaPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        Call<TicketDetailResponse> call = apiInterface.getTicket("Bearer " + token, ticketId);
        call.enqueue(new Callback<TicketDetailResponse>() {
            @Override
            public void onResponse(Call<TicketDetailResponse> call, Response<TicketDetailResponse> response) {
                txtTicketDetail.setText(response.body().getMovieTitle() + " (" + response.body().getRoomNumber() + ")");
                // Enlever le début de la chaîne : data:image/svg+xml;base64,
                String svgBase64 = response.body().getQrCode().substring(26);
                try {
                    // Décoder la Base64, puis convertir la chaîne en UTF-8, et le QR code SVG en bitmap
                    byte[] decodedBytes = Base64.decode(svgBase64, Base64.DEFAULT);
                    String svgContent = new String(decodedBytes, "UTF-8");
                    SVG svg = SVG.getFromString(svgContent);
                    // Obtenir les dimensions de l'ImageView
                    int width = imgQRCodeTicket.getWidth();
                    svg.setDocumentWidth(width);
                    svg.setDocumentHeight(width);
                    // Afficher l'image du QR code
                    Bitmap bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    svg.renderToCanvas(canvas);
                    imgQRCodeTicket.setImageBitmap(bitmap);
                } catch (SVGParseException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TicketDetailResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }
}