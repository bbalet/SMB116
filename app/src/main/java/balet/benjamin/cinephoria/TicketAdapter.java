package balet.benjamin.cinephoria;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import balet.benjamin.cinephoria.model.TicketResponse;

public class TicketAdapter extends BaseAdapter {
    private Context context;
    private List<TicketResponse> tickets;

    public TicketAdapter(Context context, List<TicketResponse> tickets) {
        this.context = context;
        this.tickets = tickets;
    }

    @Override
    public int getCount() {
        return tickets.size();
    }

    @Override
    public Object getItem(int position) {
        return tickets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_ticket, parent, false);
        }
        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        TextView startTimeTextView = convertView.findViewById(R.id.startTimeTextView);
        TicketResponse ticket = tickets.get(position);
        titleTextView.setText(ticket.getMovieTitle());
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE d MMMM yyyy", Locale.FRENCH);
        String formattedDate = sdf.format(ticket.getStartDate());
        startTimeTextView.setText(formattedDate);

        // Gestion du clic sur l'élément de la liste
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ShowTicketQRCodeActivity.class);
            intent.putExtra("ticketId", ticket.getTicketId());
            context.startActivity(intent);
        });

        return convertView;
    }
}
