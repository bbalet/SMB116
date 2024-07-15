package balet.benjamin.cinephoria;

import android.os.Bundle;
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
import balet.benjamin.cinephoria.model.TheaterResponse;
import balet.benjamin.cinephoria.model.TheaterListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListTheatersActivity extends AppCompatActivity {

    private Button cmdCloseListTheatersActivity;
    private ListView lstTheaters;
    private TheaterAdapter adapter;
    private CinephoriaAPI apiInterface;
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_theaters);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cmdCloseListTheatersActivity = findViewById(R.id.cmdCloseListTheatersActivity);
        lstTheaters = findViewById(R.id.lstIssues);
        cmdCloseListTheatersActivity.setOnClickListener(v -> finish());
        apiInterface = APIClient.getClient().create(CinephoriaAPI.class);
        getTheaters();
    }

    /**
     * Appeler l'API pour récupérer les cinémas (cette partie est publique)
     * @param token
     */
    private void getTheaters() {
        Call<TheaterListResponse> call = apiInterface.getTheaters();
        call.enqueue(new Callback<TheaterListResponse>() {
            @Override
            public void onResponse(Call<TheaterListResponse> call, Response<TheaterListResponse> response) {
                List<TheaterResponse> theaterResponses = response.body().getTheaters();
                adapter = new TheaterAdapter(ListTheatersActivity.this, theaterResponses);
                lstTheaters.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<TheaterListResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }
}