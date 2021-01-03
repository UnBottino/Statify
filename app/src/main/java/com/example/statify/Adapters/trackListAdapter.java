package com.example.statify.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.statify.Model.Track;
import com.example.statify.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class trackListAdapter extends RecyclerView.Adapter<trackListAdapter.trackListItemHolder> implements Filterable {

    private List<Track> mTrackList;
    private List<Track> mTrackListFull;
    private LayoutInflater mInflater;

    @NonNull
    @Override
    public trackListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.track_list_item, parent, false);
        return new trackListItemHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(final trackListItemHolder holder, int position) {
        final String mTrackArtUrl = mTrackList.get(position).getTrackArtUrl();
        final String mTrackName = mTrackList.get(position).getTrackName();
        final String mTrackInfo = mTrackList.get(position).getTrackArtists();
        final int mTrackPlayCount = mTrackList.get(position).getPlayCount();

        holder.trackNameView.setText(mTrackName);
        holder.trackInfoView.setText(mTrackInfo);
        holder.trackPlayCountView.setText(String.valueOf(mTrackPlayCount));

        //Picasso
        Picasso.get()
            .load(Uri.parse(mTrackArtUrl))
            .into(holder.albumArtView);
    }

    @Override
    public int getItemCount() {
        return mTrackList.size();
    }

    public trackListAdapter(Context context, List<Track> trackList) {
        mInflater = LayoutInflater.from(context);
        mTrackListFull = new ArrayList<>(trackList);
        this.mTrackList = trackList;
    }

    static class trackListItemHolder extends RecyclerView.ViewHolder {
        final ImageView albumArtView;
        final TextView trackNameView;
        final TextView trackInfoView;
        final TextView trackPlayCountView;

        final trackListAdapter mAdapter;

        trackListItemHolder(View itemView, trackListAdapter adapter) {
            super(itemView);

            albumArtView = itemView.findViewById(R.id.albumArt);
            trackNameView = itemView.findViewById(R.id.trackName);
            trackInfoView = itemView.findViewById(R.id.trackInfo);
            trackPlayCountView = itemView.findViewById(R.id.trackPlayCount);

            this.mAdapter = adapter;
        }
    }

    @Override
    public Filter getFilter() {
        return trackFilter;
    }

    private Filter trackFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Track> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(mTrackListFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (Track t : mTrackListFull)
                {
                    if(t.getTrackName().toLowerCase().contains(filterPattern) ||
                    t.getTrackAlbumName().toLowerCase().contains(filterPattern) ||
                    t.getTrackArtists().toLowerCase().contains(filterPattern)){
                        filteredList.add(t);
                    }

                    if(filterPattern.contains("s")){
                        filterPattern = filterPattern.replace("s", "$");

                        if(t.getTrackName().toLowerCase().contains(filterPattern) ||
                                t.getTrackAlbumName().toLowerCase().contains(filterPattern) ||
                                t.getTrackArtists().toLowerCase().contains(filterPattern)){
                            filteredList.add(t);
                        }
                    }
                }

                Set<Track> set = new HashSet<>(filteredList);
                filteredList.clear();
                filteredList.addAll(set);
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mTrackList.clear();
            mTrackList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}