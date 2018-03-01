package com.goldsum.austinplacesearch.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.goldsum.austinplacesearch.GlideApp;
import com.goldsum.austinplacesearch.R;
import com.goldsum.austinplacesearch.model.PlaceResult;

/**
 * Created by marshallgoldsum on 2/26/18.
 * The ViewHolder that constructs the individual cards shown in MainActivity
 */

public class PlaceResultViewHolder extends RecyclerView.ViewHolder{

    private TextView mName;
    private TextView mCategory;
    private ImageView mPlaceIcon;
    private TextView mDistanceToAustinCenter;
    private ImageView mFavoriteIcon;

    public interface PlaceCardActionListener{
        void onFavoriteClicked(String placeId);
    }

    public static PlaceResultViewHolder newInstance(ViewGroup parent)
    {
        return new PlaceResultViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.place_result_card, parent, false));
    }

    private PlaceResultViewHolder(View itemView) {
        super(itemView);
        mName = itemView.findViewById(R.id.place_name);
        mCategory = itemView.findViewById(R.id.place_category);
        mPlaceIcon = itemView.findViewById(R.id.place_icon);
        mFavoriteIcon = itemView.findViewById(R.id.favorite_icon);
        mDistanceToAustinCenter = itemView.findViewById(R.id.distance_to_austin_center);
    }

    private Context getContext()
    {
        return itemView.getContext();
    }

    public void bindModel(final PlaceResult place, final PlaceCardActionListener listener)
    {
        mName.setText(place.getName());
        GlideApp.with(getContext()).asBitmap().load(place.getIconUrl()).into(mPlaceIcon);
        mDistanceToAustinCenter.setText(getContext().getString(R.string.distance, place.getDistanceFromSearchPoint()));
        mCategory.setText(place.getCategory());

        itemView.setOnClickListener(v -> launchDetailActivity(place));

        mFavoriteIcon.setOnClickListener(v -> {
            if (listener != null){
                listener.onFavoriteClicked(place.getId());
            }
        });
        if (place.isFavorite()){
            mFavoriteIcon.setImageResource(R.drawable.ic_favorite);
        } else {
            mFavoriteIcon.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    private void launchDetailActivity(PlaceResult place) {
        Intent detailIntent = new Intent(getContext(), DetailsActivity.class);
        detailIntent.putExtra(DetailsActivity.PLACE_ID_EXTRA, place.getId());
        getContext().startActivity(detailIntent);
    }
}
