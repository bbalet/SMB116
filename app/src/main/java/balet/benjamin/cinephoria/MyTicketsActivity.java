package balet.benjamin.cinephoria;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import balet.benjamin.cinephoria.api.APIClient;
import balet.benjamin.cinephoria.api.CinephoriaAPI;
import balet.benjamin.cinephoria.model.TicketListResponse;
import balet.benjamin.cinephoria.model.TicketResponse;
import balet.benjamin.cinephoria.model.UserInfoResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTicketsActivity extends AppCompatActivity {

    private Button cmdCloseTicketsActivity;
    private ListView lstTickets;
    private TicketAdapter adapter;
    private CinephoriaAPI apiInterface;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_tickets);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cmdCloseTicketsActivity = findViewById(R.id.cmdCloseTicketsActivity);
        lstTickets = findViewById(R.id.lstTickets);
        cmdCloseTicketsActivity.setOnClickListener(v -> finish());
        apiInterface = APIClient.getClient().create(CinephoriaAPI.class);
        getMyTickets();
    }

    /**
     * Obtenir le token stocké dans les shared preferences et appeler l'API pour récupérer les tickets
     * @param token
     */
    private void getMyTickets() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("cinephoriaPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        Call<TicketListResponse> call = apiInterface.getTickets("Bearer " + token);
        call.enqueue(new Callback<TicketListResponse>() {
            @Override
            public void onResponse(Call<TicketListResponse> call, Response<TicketListResponse> response) {
                List<TicketResponse> tickets = response.body().getTickets();
                adapter = new TicketAdapter(getApplicationContext(), tickets);
                lstTickets.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<TicketListResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }
}