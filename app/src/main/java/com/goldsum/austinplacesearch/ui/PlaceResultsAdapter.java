package com.goldsum.austinplacesearch.ui;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.goldsum.austinplacesearch.model.PlaceResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marshallgoldsum on 2/26/18.
 * Adapter controlling the list of cards in MainActivity
 */

class PlaceResultsAdapter extends RecyclerView.Adapter<PlaceResultViewHolder> {

    private ArrayList<PlaceResult> mPlaceResults;
    private PlaceResultViewHolder.PlaceCardActionListener mListener;

    public PlaceResultsAdapter(PlaceResultViewHolder.PlaceCardActionListener listener)
    {
        mPlaceResults = new ArrayList<>();
        mListener = listener;
    }

    @Override
    public PlaceResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return PlaceResultViewHolder.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(PlaceResultViewHolder holder, int position) {
        holder.bindModel(mPlaceResults.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mPlaceResults.size();
    }

    public void setPlaces(List<PlaceResult> placeResultList) {
        mPlaceResults.clear();
        mPlaceResults.addAll(placeResultList);
        notifyDataSetChanged();

        int i = 0;
        for (PlaceResult place :
                mPlaceResults) {
            i++;
            Log.d("placeAdapter: "+i, place.toString());
        }
    }

    public void updatePlace(PlaceResult placeResult) {
        for (int i = 0; i < mPlaceResults.size(); i++) {
            if (placeResult.getId().equals(mPlaceResults.get(i).getId())){
                mPlaceResults.set(i, placeResult);
                notifyItemChanged(i);
            }
        }
    }
}
