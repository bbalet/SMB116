package balet.benjamin.cinephoria;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import balet.benjamin.cinephoria.model.TheaterResponse;

public class TheaterAdapter extends BaseAdapter {
    private Context context;
    private List<TheaterResponse> theaterResponses;

    public TheaterAdapter(Context context, List<TheaterResponse> theaterResponses) {
        this.context = context;
        this.theaterResponses = theaterResponses;
    }

    @Override
    public int getCount() {
        return theaterResponses.size();
    }

    @Override
    public Object getItem(int position) {
        return theaterResponses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_theater, parent, false);
        }
        TextView txtItemTheaterCity = convertView.findViewById(R.id.txtItemIssueTitle);
        TheaterResponse theaterResponse = theaterResponses.get(position);
        txtItemTheaterCity.setText(theaterResponse.getCity());

        // Gestion du clic sur l'élément de la liste
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ListRoomsActivity.class);
            intent.putExtra("theaterId", theaterResponse.getTheaterId());
            context.startActivity(intent);
        });

        return convertView;
    }
}
