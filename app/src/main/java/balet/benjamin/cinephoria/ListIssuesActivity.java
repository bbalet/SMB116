package balet.benjamin.cinephoria;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import balet.benjamin.cinephoria.model.IssueListResponse;
import balet.benjamin.cinephoria.model.IssueResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListIssuesActivity extends AppCompatActivity {

    private Button cmdAddNewIssue, cmdCloseListIssuesActivity;
    private ListView lstIssues;
    private IssueAdapter adapter;
    private CinephoriaAPI apiInterface;
    private final String TAG = this.getClass().getSimpleName();
    private SharedPreferences sharedPreferences;
    private int roomId;

    private BroadcastReceiver updateListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "updateListReceiver: Refresh liste des problèmes ");
            getIssues();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_issues);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        apiInterface = APIClient.getClient().create(CinephoriaAPI.class);
        this.cmdAddNewIssue = findViewById(R.id.cmdAddNewIssue);
        this.cmdCloseListIssuesActivity = findViewById(R.id.cmdCloseListIssuesActivity);
        cmdAddNewIssue.setOnClickListener(this::onClickAddNewIssueFromList);
        cmdCloseListIssuesActivity.setOnClickListener(v -> finish());
        this.lstIssues = findViewById(R.id.lstIssues);
        this.roomId = getIntent().getIntExtra("roomId", -1);
        getIssues();

        // Enregistrer le BroadcastReceiver
        IntentFilter filter = new IntentFilter("balet.benjamin.cinephoria.UPDATE_ISSUES_LIST");
        registerReceiver(updateListReceiver, filter);
    }

    public void onClickAddNewIssueFromList(View v) {
        Intent intent = new Intent(ListIssuesActivity.this, CrudIssueActivity.class);
        intent.putExtra("ISSUE_FORM_MODE", "NEW");
        intent.putExtra("roomId", roomId);
        ListIssuesActivity.this.startActivity(intent);
    }

    /**
     * Appeler l'API pour récupérer les problèmes liés à la salle
     * @param token
     */
    private void getIssues() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("cinephoriaPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        Call<IssueListResponse> call = apiInterface.getIssues("Bearer " + token, this.roomId);
        call.enqueue(new Callback<IssueListResponse>() {
            @Override
            public void onResponse(Call<IssueListResponse> call, Response<IssueListResponse> response) {
                List<IssueResponse> issueResponses = response.body().getIssues();
                adapter = new IssueAdapter(ListIssuesActivity.this, issueResponses);
                lstIssues.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<IssueListResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getIssues();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(updateListReceiver);
    }
}