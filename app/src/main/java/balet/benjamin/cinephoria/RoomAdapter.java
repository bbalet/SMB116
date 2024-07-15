package balet.benjamin.cinephoria;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import balet.benjamin.cinephoria.model.RoomResponse;

public class RoomAdapter extends BaseAdapter {
    private Context context;
    private List<RoomResponse> roomResponses;

    public RoomAdapter(Context context, List<RoomResponse> roomResponses) {
        this.context = context;
        this.roomResponses = roomResponses;
    }

    @Override
    public int getCount() {
        return roomResponses.size();
    }

    @Override
    public Object getItem(int position) {
        return roomResponses.get(position);
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
        TextView txtItemRoomNumber = convertView.findViewById(R.id.txtItemIssueTitle);
        RoomResponse roomResponse = roomResponses.get(position);
        txtItemRoomNumber.setText(roomResponse.getNumber());

        // Gestion du clic sur l'élément de la liste
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CrudIssueActivity.class);
            intent.putExtra("roomId", roomResponse.getRoomId());
            context.startActivity(intent);
        });

        return convertView;
    }
}
