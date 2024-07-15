package balet.benjamin.cinephoria;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import balet.benjamin.cinephoria.api.APIClient;
import balet.benjamin.cinephoria.api.CinephoriaAPI;
import balet.benjamin.cinephoria.model.IssueRequest;
import balet.benjamin.cinephoria.model.IssueResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrudIssueActivity extends AppCompatActivity {

    private TextView txtIssueActivityTitle;
    private EditText txtTitle, txtDescription;
    private Spinner cboStatus;
    private Button cmdEditIssueAction, cmdCloseEditIssueActivity;
    private int roomId;
    private int issueId;
    private String ISSUE_FORM_MODE = "NEW";
    ArrayAdapter<CharSequence> adapter;
    private final String TAG = this.getClass().getSimpleName();
    private SharedPreferences sharedPreferences;
    private CinephoriaAPI apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crud_issue);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        this.txtIssueActivityTitle = findViewById(R.id.txtIssueActivityTitle);
        this.txtTitle = findViewById(R.id.txtTitle);
        this.txtDescription = findViewById(R.id.txtDescription);
        this.cboStatus = findViewById(R.id.cboStatus);
        this.cmdEditIssueAction = findViewById(R.id.cmdEditIssueAction);
        this.cmdCloseEditIssueActivity = findViewById(R.id.cmdCloseEditIssueActivity);
        this.cmdCloseEditIssueActivity.setOnClickListener(v -> finish());
        this.cmdEditIssueAction.setOnClickListener(this::onClickEditIssueAction);

        apiInterface = APIClient.getClient().create(CinephoriaAPI.class);

        // Charger le spinner avec les statuses (fichier XML statique)
        this.adapter = ArrayAdapter.createFromResource(
                this,
                R.array.issue_status_array,
                android.R.layout.simple_spinner_item
        );
        this.adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.cboStatus.setAdapter(adapter);

        // Récupérer l'identifiant de la salle et du ticket le cas échéant
        this.roomId = getIntent().getIntExtra("roomId", -1);
        this.issueId = getIntent().getIntExtra("issueId", -1);
        this.ISSUE_FORM_MODE = getIntent().getStringExtra("ISSUE_FORM_MODE");
        Log.i(TAG, "onCreate: issueId=" + issueId + " mode=" + ISSUE_FORM_MODE);
        if (this.ISSUE_FORM_MODE.equals("UPDATE")) {
            this.txtIssueActivityTitle.setText("Modifier un problème");
            this.cmdEditIssueAction.setText("Modifier");
            loadIssue();
        } else {
            this.txtIssueActivityTitle.setText("Nouveau problème");
            this.cmdEditIssueAction.setText("Créer");
        }
    }

    /**
     * Charger les informations d'un problème
     */
    private void loadIssue() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("cinephoriaPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        Call<IssueResponse> call = apiInterface.getIssue("Bearer " + token, this.issueId);
        call.enqueue(new Callback<IssueResponse>() {
            @Override
            public void onResponse(Call<IssueResponse> call, Response<IssueResponse> response) {
                IssueResponse issue = response.body();
                roomId = issue.getRoomId();
                txtTitle.setText(issue.getTitle());
                txtDescription.setText(issue.getDescription());
                cboStatus.setSelection(adapter.getPosition(issue.getStatus()));
            }

            @Override
            public void onFailure(Call<IssueResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }

    /**
     * Créer ou modifier un problème. Broadcaster le changement
     * @param v
     */
    public void onClickEditIssueAction(View v) {
        apiInterface = APIClient.getClient().create(CinephoriaAPI.class);
        SharedPreferences sharedPreferences = this.getSharedPreferences("cinephoriaPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        IssueRequest issueRequest = new IssueRequest();
        issueRequest.setRoomId(this.roomId);
        issueRequest.setTitle(this.txtTitle.getText().toString());
        issueRequest.setDescription(this.txtDescription.getText().toString());
        issueRequest.setStatus(this.cboStatus.getSelectedItem().toString());
        if (this.ISSUE_FORM_MODE.equals("NEW")) {
                Call<IssueResponse> call = apiInterface.createIssue("Bearer " + token, issueRequest);
                call.enqueue(new Callback<IssueResponse>() {
                    @Override
                    public void onResponse(Call<IssueResponse> call, Response<IssueResponse> response) {
                        IssueResponse issue = response.body();
                        Toast.makeText(getApplicationContext(), "Problème n° " + String.valueOf(issue.getIssueId()) + " créée avec succès.",
                                Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onFailure(Call<IssueResponse> call, Throwable t) {
                        call.cancel();
                    }
                });
        } else {
            issueRequest.setIssueId(this.issueId);
            Call<IssueResponse> call = apiInterface.updateIssue("Bearer " + token, this.roomId, issueRequest);
            call.enqueue(new Callback<IssueResponse>() {
                @Override
                public void onResponse(Call<IssueResponse> call, Response<IssueResponse> response) {
                    IssueResponse issue = response.body();
                    Toast.makeText(getApplicationContext(), "Problème n° " + String.valueOf(issue.getIssueId()) + " modifié avec succès.",
                            Toast.LENGTH_LONG).show();
                }
                @Override
                public void onFailure(Call<IssueResponse> call, Throwable t) {
                    call.cancel();
                }
            });
        }

        //Envoyer l'évènement de changement d'état
        Intent intent = new Intent("balet.benjamin.cinephoria.UPDATE_ISSUES_LIST");
        sendBroadcast(intent);
    }
}