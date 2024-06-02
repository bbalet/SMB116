package balet.benjamin.cinephoria;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchTheatersTask extends AsyncTask<Double, Void, List<Theater>> {

    @Override
    protected List<Theater> doInBackground(Double... coordinates) {
        List<Theater> theaters = new ArrayList<>();
        try {
            double latitude = coordinates[0];
            double longitude = coordinates[1];
            URL url = new URL("https://cinephoria.jorani.org/api/theaters?latitude=" + latitude + "&longitude=" + longitude);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder jsonResult = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonResult.append(line);
            }
            reader.close();
            JSONArray jsonArray = new JSONArray(jsonResult.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Theater theater = new Theater(
                        jsonObject.getInt("id"),
                        jsonObject.getString("city"),
                        jsonObject.getDouble("latitude"),
                        jsonObject.getDouble("longitude")
                );
                theaters.add(theater);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return theaters;
    }

    @Override
    protected void onPostExecute(List<Theater> theaters) {
        super.onPostExecute(theaters);
        // Update the map with theater markers
    }
}
