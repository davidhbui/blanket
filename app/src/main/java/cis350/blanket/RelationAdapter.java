package cis350.blanket;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by shivpatel on 4/16/15.
 */
public class RelationAdapter extends RecyclerView.Adapter<RelationAdapter.RelationViewHolder> {

    public RelationAdapter() {

    }

    @Override
    public int getItemCount() {
        return StreetChangeApplication.getInstance().relationList.size();
    }

    // Bind the donation data to the view
    @Override
    public void onBindViewHolder(RelationViewHolder relationViewHolder, int i) {

        Relation relation = StreetChangeApplication.getInstance().relationList.get(i);

        relationViewHolder.relationRecipientName.append(" " + (relation.getRecipient().getProductName()));
        relationViewHolder.relationAmount.append(relation.getAmountString());
        relationViewHolder.relationFreq.append(" " + relation.getFreq());
    }

    @Override
    public RelationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.relation_card, viewGroup, false);

        return new RelationViewHolder(itemView);
    }

    public static class RelationViewHolder extends RecyclerView.ViewHolder {

        TextView relationRecipientName;
        TextView relationAmount;
        TextView relationFreq;

        public RelationViewHolder(View v) {
            super(v);
            relationRecipientName = (TextView) v.findViewById(R.id.recipientName);
            relationAmount = (TextView) v.findViewById(R.id.listedAmount);
            relationFreq = (TextView) v.findViewById(R.id.listedFreq);

        }
    }

    public void addRelation(Relation d) {
        // Add a relation to the front of the list
        StreetChangeApplication.getInstance().relationList.add(0, d);
    }

}