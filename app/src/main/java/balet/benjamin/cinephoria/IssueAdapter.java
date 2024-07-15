package balet.benjamin.cinephoria;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import balet.benjamin.cinephoria.model.IssueResponse;
import balet.benjamin.cinephoria.model.RoomResponse;

public class IssueAdapter extends BaseAdapter {
    private Context context;
    private List<IssueResponse> issueResponses;

    public IssueAdapter(Context context, List<IssueResponse> issueResponses) {
        this.context = context;
        this.issueResponses = issueResponses;
    }

    @Override
    public int getCount() {
        return issueResponses.size();
    }

    @Override
    public Object getItem(int position) {
        return issueResponses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_issue, parent, false);
        }
        TextView txtItemIssueTitle = convertView.findViewById(R.id.txtItemIssueTitle);
        IssueResponse issueResponse = issueResponses.get(position);
        txtItemIssueTitle.setText(issueResponse.getTitle());

        // Gestion du clic sur l'élément de la liste
        convertView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CrudIssueActivity.class);
            intent.putExtra("issueId", issueResponse.getIssueId());
            context.startActivity(intent);
        });

        return convertView;
    }
}
