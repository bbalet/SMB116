package balet.benjamin.cinephoria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button cmdTheatersAroundMe, cmdScanTicket, cmdClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cmdTheatersAroundMe = (Button) findViewById(R.id.cmdTheatersAroundMe);
        cmdScanTicket = (Button) findViewById(R.id.cmdScanTicket);
        cmdClose = (Button) findViewById(R.id.cmdClose);

        cmdTheatersAroundMe.setOnClickListener(this::onClickOpenTheatersAround);
        cmdScanTicket.setOnClickListener(this::onClickOpenScanTicket);
        cmdClose.setOnClickListener(this::onClickClose);

    }

    public void onClickOpenTheatersAround(View v) {
        Intent intent = new Intent(MainActivity.this, TheatersAroundActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void onClickOpenScanTicket(View v) {
        Intent intent = new Intent(MainActivity.this, ScanTicketActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void onClickClose(View v) {
        finish();
    }
}