package com.example.aprendiz.salesapp.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aprendiz.salesapp.R;
import com.example.aprendiz.salesapp.fragments.PublicationFragment.OnListFragmentInteractionListener;
import com.example.aprendiz.salesapp.models.Publication;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class PublicationRecyclerViewAdapter extends RecyclerView.Adapter<PublicationRecyclerViewAdapter.ViewHolder> {

    private final List<Publication> mValues;
    private final OnListFragmentInteractionListener mListener;

    public PublicationRecyclerViewAdapter(List<Publication> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_publication, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        // Proceso carga imagen

        if (!mValues.get(position).getPhoto().isEmpty()) {
            Picasso.with(holder.mImageView.getContext())
                    .load(mValues.get(position).getPhoto())
                    .into(holder.mImageView);
        }

        holder.mTitleView.setText(mValues.get(position).getTitle());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mTitleView;
        public Publication mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.image);
            mTitleView = (TextView) view.findViewById(R.id.title);
        }
    }
}
