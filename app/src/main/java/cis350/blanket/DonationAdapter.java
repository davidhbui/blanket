package cis350.blanket;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by davidhb on 4/14/2015.
 */
public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.DonationViewHolder> {

    private String recipientName;

    public DonationAdapter() {

    }

    @Override
    public int getItemCount() {
        return StreetChangeApplication.getInstance().donationList.size();
    }

    // Bind the donation data to the view
    @Override
    public void onBindViewHolder(DonationViewHolder donationViewHolder, int i) {

        Donation donation = StreetChangeApplication.getInstance().donationList.get(i);


        donationViewHolder.donationRecipientName.append(" " + donation.getRecipientName());
        donationViewHolder.donationAmount.append(donation.getAmountString());
        donationViewHolder.donationLocation.append(" " + donation.getLocation().getProvider());
        donationViewHolder.donationDate.append(" " + donation.getDateString());
        donationViewHolder.donationItem.append(" " + donation.getItem());
        donationViewHolder.donationRedeemed.append(" " + donation.getRedeemedString());
    }

    @Override
    public DonationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.donation_card, viewGroup, false);

        return new DonationViewHolder(itemView);
    }

    public static class DonationViewHolder extends RecyclerView.ViewHolder {

        TextView donationRecipientName;
        TextView donationAmount;
        TextView donationLocation;
        TextView donationDate;
        TextView donationItem;
        TextView donationRedeemed;


        public DonationViewHolder(View v) {
            super(v);
            donationRecipientName = (TextView) v.findViewById(R.id.recipientName);
            donationAmount = (TextView) v.findViewById(R.id.listedAmount);
            donationItem = (TextView) v.findViewById(R.id.donation_item);
            donationDate = (TextView) v.findViewById(R.id.donation_date);
            donationRedeemed = (TextView) v.findViewById(R.id.donation_redeemed);
            donationLocation = (TextView) v.findViewById(R.id.donation_location);

        }
    }


    public void addDonation(Donation d) {
        // Add a donation to the front of the list
        StreetChangeApplication.getInstance().donationList.add(0, d);
    }


}
