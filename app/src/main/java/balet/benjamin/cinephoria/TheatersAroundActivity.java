package balet.benjamin.cinephoria;

import android.location.LocationRequest;
import android.os.Bundle;

import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import balet.benjamin.cinephoria.api.APIClient;
import balet.benjamin.cinephoria.model.TheaterResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import balet.benjamin.cinephoria.api.CinephoriaAPI;
import balet.benjamin.cinephoria.model.TheaterListResponse;

public class TheatersAroundActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Button cmdCloseTheatersAroundActivity;
    //private SupportMapFragment mapView;
    GoogleMap mapView;
    private FusedLocationProviderClient locationClient;
    private LocationRequest locationRequest;
    private CinephoriaAPI apiInterface;
    private final String TAG = this.getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_theaters_around);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        cmdCloseTheatersAroundActivity = (Button) findViewById(R.id.cmdCloseTheatersAroundActivity);
        cmdCloseTheatersAroundActivity.setOnClickListener(this::onClickClose);

        apiInterface = APIClient.getClient().create(CinephoriaAPI.class);

        locationClient = LocationServices.getFusedLocationProviderClient(this);
        locationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapView = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        getTheaters();
        //Par défaut, centrer sur Paris
        LatLng userLatLng = new LatLng(48.866667, 2.333333);
        mapView.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 11));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getDeviceLocation();
            }
        }
    }

    private void getDeviceLocation() {
        Task<Location> locationResult = locationClient.getLastLocation();
        locationResult.addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Log.i(TAG, "getDeviceLocation: Latitude=" + location.getLatitude() + " Longitude=" + location.getLongitude());
                    LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mapView.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 11));
                }
            }
        });
    }

    /**
     * Afficher tous les cinémas sur la carte (il n'y en a que quelques uns)
     */
    private void getTheaters() {
        Call<TheaterListResponse> call = apiInterface.getTheaters();
        call.enqueue(new Callback<TheaterListResponse>() {
            @Override
            public void onResponse(Call<TheaterListResponse> call, Response<TheaterListResponse> response) {
                List<TheaterResponse> theaterResponses = response.body().getTheaters();
                Log.i(TAG, "AddMarker: Nbe cinémas=" + theaterResponses.size());
                for (TheaterResponse theaterResponse : theaterResponses) {
                    LatLng position = new LatLng(theaterResponse.latitude, theaterResponse.longitude);
                    Log.i(TAG, "AddMarker: Latitude=" + theaterResponse.getLatitude() + " Longitude=" + theaterResponse.getLongitude());
                    mapView.addMarker(new MarkerOptions().position(position).title(theaterResponse.city));
                }
            }

            @Override
            public void onFailure(Call<TheaterListResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void onClickClose(View v) {
        finish();
    }
}