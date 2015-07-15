package cis350.blanket;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tejas Narayan on 4/15/2015.
 * The adapter that is used for the Newsfeed cards that appear on the newsfeed screen of the app
 */
public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.DonationViewHolder> {

    List<Donation> filtered;

    public NewsFeedAdapter(List<Donation> in) {
        filtered = in;
    }

    @Override
    public int getItemCount() {
        return filtered.size();
    }

    // Bind the donation data to the view
    @Override
    public void onBindViewHolder(DonationViewHolder donationViewHolder, int i) {

        Donation donation = filtered.get(i);

        donationViewHolder.donationRecipientName.append(" " + (donation.getRecipientName()));
        donationViewHolder.donationLocation.append(" " + donation.getLocation().getProvider());
        donationViewHolder.donationDate.append(" " + donation.getDateString());
        donationViewHolder.donationItem.append(" " + donation.getItem());
        donationViewHolder.donorName.append(" " + donation.getDonorName());
    }

    @Override
    public DonationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.donation_card_news_feed, viewGroup, false);

        return new DonationViewHolder(itemView);
    }

    public static class DonationViewHolder extends RecyclerView.ViewHolder {

        TextView donorName;
        TextView donationRecipientName;
        TextView donationLocation;
        TextView donationDate;
        TextView donationItem;


        public DonationViewHolder(View v) {
            super(v);
            donorName = (TextView) v.findViewById(R.id.donorName);
            donationRecipientName = (TextView) v.findViewById(R.id.recipientName);
            donationItem = (TextView) v.findViewById(R.id.donation_item);
            donationDate = (TextView) v.findViewById(R.id.donation_date);
            donationLocation = (TextView) v.findViewById(R.id.donation_location);

        }
    }


    public void addDonation(Donation d) {
        // Add a donation to the front of the list
        StreetChangeApplication.getInstance().donationList.add(0, d);
    }


}
