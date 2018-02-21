package fabiohideki.com.megagenerator.adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fabiohideki.com.megagenerator.R;
import fabiohideki.com.megagenerator.model.LotteryPlace;

/**
 * Created by fabio.lagoa on 20/02/2018.
 */

public class LotteryNearbyAdapter extends RecyclerView.Adapter<LotteryNearbyAdapter.LotteryNearbyHolder> {

    private List<LotteryPlace> lotteryResults;
    private Location currentLocation;
    private Context context;
    private int lastPosition = -1;

    public LotteryNearbyAdapter(List<LotteryPlace> lotteryResults, Context context, Location currentLocation) {
        this.lotteryResults = lotteryResults;
        this.currentLocation = currentLocation;
        this.context = context;
    }

    @Override
    public LotteryNearbyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new LotteryNearbyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place_lottery, parent, false));

    }

    @Override
    public void onBindViewHolder(LotteryNearbyHolder holder, int position) {

        if (lotteryResults != null && lotteryResults.size() > 0) {

            LotteryPlace lotteryPlace = lotteryResults.get(position);
            holder.mLotteryTitle.setText(lotteryPlace.getName());
            holder.mLotteryAddress.setText(lotteryPlace.getVicinity());

            Location lotteryLocation = new Location("lotteryLocation");
            lotteryLocation.setLatitude(lotteryPlace.getGeometry().getLocation().getLat());
            lotteryLocation.setLongitude(lotteryPlace.getGeometry().getLocation().getLat());

            Log.d("Fabio", "LotteryNearbyAdapter onBindViewHolder: lotteryLocation:" + lotteryPlace.getGeometry().getLocation().getLat() + "," + lotteryPlace.getGeometry().getLocation().getLng());
            Log.d("Fabio", "LotteryNearbyAdapter onBindViewHolder: MyLocation:" + currentLocation.getLatitude() + "," + currentLocation.getLongitude());

            float[] results = new float[1];
            Location.distanceBetween(currentLocation.getLatitude(), currentLocation.getLongitude(), lotteryPlace.getGeometry().getLocation().getLat(), lotteryPlace.getGeometry().getLocation().getLng(), results);

            float distance = results[0];
            Log.d("LotteryNearbyAdapter", "onBindViewHolder: distance:" + distance);

            if (distance > 1000) {
                holder.mLotteryDistance.setText(new DecimalFormat("##.##").format((distance / 1000)) + " " + context.getString(R.string.km_label));
            } else {
                holder.mLotteryDistance.setText(new DecimalFormat("##.##").format(distance) + " " + context.getString(R.string.meters_label));
            }

            setAnimation(holder.itemView, position);
        }

    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return lotteryResults != null ? lotteryResults.size() : 0;
    }


    protected class LotteryNearbyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_lottery_title)
        TextView mLotteryTitle;

        @BindView(R.id.tv_lottery_address)
        TextView mLotteryAddress;

        @BindView(R.id.tv_lottery_distance)
        TextView mLotteryDistance;

        @BindView(R.id.iv_maps_link)
        ImageView mIvMapLink;


        public LotteryNearbyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mIvMapLink.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            LotteryPlace lotteryPlace = lotteryResults.get(getAdapterPosition());

            double lat = lotteryPlace.getGeometry().getLocation().getLat(), lon = lotteryPlace.getGeometry().getLocation().getLng();

            Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lon + "?q=" + Uri.encode(lotteryPlace.getName() + ", " + lotteryPlace.getVicinity()));

            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            // Make the Intent explicit by setting the Google Maps package
            mapIntent.setPackage("com.google.android.apps.maps");
            // Attempt to start an activity that can handle the Intent
            context.startActivity(mapIntent);

        }
    }
}
