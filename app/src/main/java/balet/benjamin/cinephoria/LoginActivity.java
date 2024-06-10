package balet.benjamin.cinephoria;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import balet.benjamin.cinephoria.model.LoginRequest;
import retrofit2.Response;
import retrofit2.Call;
import retrofit2.Callback;

import balet.benjamin.cinephoria.api.APIClient;
import balet.benjamin.cinephoria.api.CinephoriaAPI;
import balet.benjamin.cinephoria.model.LoginResponse;
import balet.benjamin.cinephoria.model.UserInfoResponse;


public class LoginActivity extends AppCompatActivity {

    private Button cmdConnect, cmdCancelLogin;
    private EditText txtLogin, txtPassword;
    private CinephoriaAPI apiInterface;

    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        cmdConnect = (Button) findViewById(R.id.cmdConnect);
        cmdCancelLogin = (Button) findViewById(R.id.cmdCloseShowQRActivity);
        txtLogin = findViewById(R.id.txtLogin);
        txtPassword = findViewById(R.id.txtPassword);
        cmdConnect.setOnClickListener(this::onClickConnect);
        cmdCancelLogin.setOnClickListener(this::onClickClose);
        // Initialiser l'API
        apiInterface = APIClient.getClient().create(CinephoriaAPI.class);
    }

    /**
     * Tenter de se connecter avec l'API, on doit obtenir un token de connexion en retour
     * @param v
     */
    public void onClickConnect(View v) {
        Log.i(TAG, "Tentative de connexion");
        LoginRequest request = new LoginRequest(txtLogin.getText().toString(), txtPassword.getText().toString());
        Call<LoginResponse> call = apiInterface.login(request);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.code() == 200 && response.body() != null) {
                    SharedPreferences prefs;
                    SharedPreferences.Editor edit;
                    prefs = LoginActivity.this.getSharedPreferences("cinephoriaPrefs", Context.MODE_PRIVATE);
                    edit = prefs.edit();
                    String token = response.body().token;
                    edit.putString("token", token);
                    Log.i(TAG, token);
                    edit.commit();
                    getUserInfo(token);
                } else {
                    Log.e(TAG, "Erreur de connexion avec le code " + String.valueOf(response.code()));
                    Toast.makeText(getApplicationContext(), "Erreur de connexion. Vérifiez que vos identifiants soient corrects", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }

    /**
     * Obtenir les infos utilisateurs depuis l'API
     * y compris son rôle
     */
    private void getUserInfo(String token) {
        Call<UserInfoResponse> call = apiInterface.getUserInfo("Bearer " + token);
        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                SharedPreferences prefs;
                SharedPreferences.Editor edit;
                prefs = LoginActivity.this.getSharedPreferences("cinephoriaPrefs", Context.MODE_PRIVATE);
                edit=prefs.edit();
                String firstname = response.body().firstname;
                String lastname = response.body().lastname;
                String role = response.body().role;
                edit.putString("firstname", firstname);
                edit.putString("lastname", lastname);
                edit.putString("role", role);
                Log.i(TAG, firstname + " " + lastname + " (" + role + ")");
                edit.commit();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }

    /**
     * Fermer la fermeture, abandonner la connexion
     * @param v
     */
    public void onClickClose(View v) {
        finish();
    }
}