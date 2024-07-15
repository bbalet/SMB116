package balet.benjamin.cinephoria;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import balet.benjamin.cinephoria.api.JWTTokenUtils;

public class MainActivity extends AppCompatActivity {

    private Button cmdTheatersAroundMe, cmdScanTicket, cmdMyTickets, cmdLogin, cmdLogout, cmdClose;
    private TextView txtUserInfo;
    private SharedPreferences sharedPreferences;
    String role = "ROLE_USER";

    /**
     * Permet d'ouvrir la page de login et de récupérer les infos de l'utilisateur
     */
    ActivityResultLauncher<Intent> ActivityResultLogin = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Si on retourne un RESULT_OK c'est que l'utilisateur est connecté
                    // On récupère ses infos dont son rôle
                    restoreUserInfo();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtUserInfo = (TextView) findViewById(R.id.txtUserInfo);
        cmdTheatersAroundMe = (Button) findViewById(R.id.cmdTheatersAroundMe);
        cmdScanTicket = (Button) findViewById(R.id.cmdScanTicket);
        cmdMyTickets = (Button) findViewById(R.id.cmdMyTickets);
        cmdLogin = (Button) findViewById(R.id.cmdLogin);
        cmdLogout = (Button) findViewById(R.id.cmdLogout);
        cmdClose = (Button) findViewById(R.id.cmdClose);
        cmdTheatersAroundMe.setOnClickListener(this::onClickOpenTheatersAround);
        cmdScanTicket.setOnClickListener(this::onClickOpenScanTicket);
        cmdMyTickets.setOnClickListener(this::onClickOpenMyTickets);
        cmdLogin.setOnClickListener(this::onClickLogin);
        cmdLogout.setOnClickListener(this::onClickLogout);
        cmdClose.setOnClickListener(this::onClickClose);
        sharedPreferences = this.getSharedPreferences("cinephoriaPrefs", Context.MODE_PRIVATE);
        restoreUserInfo();
    }

    /**
     * Récupérer les infos de l'utilisateur depuis le token (s'il est valide et qu'il n'a pas expiré)
     * et les shared preferences
     */
    private void restoreUserInfo() {
        // Récupérer les infos de l'utilisateur depuis le token s'il existe et qu'il n'a pas expiré
        String token = sharedPreferences.getString("token", null);
        if (!JWTTokenUtils.isTokenExpired(token)) {
            //Contexte : l'utilisateur est connecté
            this.role = sharedPreferences.getString("role", null);
            String firstname = sharedPreferences.getString("firstname", null);
            String lastname = sharedPreferences.getString("lastname", null);
            txtUserInfo.setText("Bonjour " + firstname + " " + lastname + " !");
            cmdLogin.setVisibility(View.INVISIBLE);
            cmdLogout.setVisibility(View.VISIBLE);
            cmdMyTickets.setVisibility(View.VISIBLE);
            if (this.role.equals("ROLE_EMPLOYEE")) {
                cmdScanTicket.setVisibility(View.VISIBLE);
                cmdMyTickets.setText("Gérer les incidents");
            } else {
                cmdScanTicket.setVisibility(View.INVISIBLE);
                cmdMyTickets.setText("Mes tickets");
            }
        } else {
            //Contexte : l'utilisateur est déconnecté
            cmdScanTicket.setVisibility(View.INVISIBLE);
            cmdMyTickets.setVisibility(View.INVISIBLE);
            cmdLogin.setVisibility(View.VISIBLE);
            cmdLogout.setVisibility(View.INVISIBLE);
            txtUserInfo.setText("Veuillez vous connecter pour plus de fonctionnalités");
        }
    }

    /**
     * Ouvrir l'activité listant les cinémas autour de l'utilisateur
     * @param v
     */
    public void onClickOpenTheatersAround(View v) {
        Intent intent = new Intent(MainActivity.this, TheatersAroundActivity.class);
        MainActivity.this.startActivity(intent);
    }

    /**
     * Ouvrir l'activité de scan d'un ticket
     * @param v
     */
    public void onClickOpenScanTicket(View v) {
        Intent intent = new Intent(MainActivity.this, ScanTicketActivity.class);
        MainActivity.this.startActivity(intent);
    }

    /**
     * Ouvrir l'activité listant les tickets de l'utilisateur ou les incidents
     * @param v
     */
    public void onClickOpenMyTickets(View v) {
        if (this.role.equals("ROLE_EMPLOYEE")) {
            Intent intent = new Intent(MainActivity.this, ListTheatersActivity.class);
            MainActivity.this.startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, MyTicketsActivity.class);
            MainActivity.this.startActivity(intent);
        }
    }

    /**
     * Ouvrir l'activité de login
     * @param v
     */
    public void onClickLogin(View v) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        ActivityResultLogin.launch(intent);
    }

    /**
     * Déconnecter l'utilisateur (en fait supprimer le token JWT et les infos)
     * @param v
     */
    public void onClickLogout(View v) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("token");
        editor.remove("role");
        editor.remove("firstname");
        editor.remove("lastname");
        editor.commit();
        restoreUserInfo();
    }

    /**
     * Fermer l'application
     * @param v
     */
    public void onClickClose(View v) {
        finish();
    }
}