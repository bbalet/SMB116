package balet.benjamin.cinephoria;

import android.content.Context;
import android.content.SharedPreferences;
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
import balet.benjamin.cinephoria.model.RoomListResponse;
import balet.benjamin.cinephoria.model.RoomResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListRoomsActivity extends AppCompatActivity {
    private Button cmdCloseListTRoomsActivity;
    private ListView lstRooms;
    private RoomAdapter adapter;
    private CinephoriaAPI apiInterface;
    private final String TAG = this.getClass().getSimpleName();
    private SharedPreferences sharedPreferences;
    private int theaterId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_room);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        apiInterface = APIClient.getClient().create(CinephoriaAPI.class);
        this.cmdCloseListTRoomsActivity = findViewById(R.id.cmdCloseListTRoomsActivity);
        cmdCloseListTRoomsActivity.setOnClickListener(v -> finish());
        this.lstRooms = findViewById(R.id.lstRooms);
        this.theaterId = getIntent().getIntExtra("theaterId", -1);
        getRooms();
    }

    private void getRooms() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("cinephoriaPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        Call<RoomListResponse> call = apiInterface.getRooms("Bearer " + token, this.theaterId);
        call.enqueue(new Callback<RoomListResponse>() {
            @Override
            public void onResponse(Call<RoomListResponse> call, Response<RoomListResponse> response) {
                List<RoomResponse> roomsResponses = response.body().getRooms();
                adapter = new RoomAdapter(ListRoomsActivity.this, roomsResponses);
                lstRooms.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<RoomListResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }
}