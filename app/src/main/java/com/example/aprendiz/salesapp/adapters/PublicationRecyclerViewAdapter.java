package com.example.aprendiz.salesapp.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aprendiz.salesapp.R;
import com.example.aprendiz.salesapp.fragments.PublicationDetailFragment;
import com.example.aprendiz.salesapp.fragments.PublicationFragment.OnListFragmentInteractionListener;
import com.example.aprendiz.salesapp.models.Publication;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link //DummyItem} and makes a call to the
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);

        // Proceso carga imagen

        if (!mValues.get(position).getPhoto().isEmpty()) {
            Picasso.with(holder.mImageView.getContext())
                    .load(mValues.get(position).getPhoto())
                    .into(holder.mImageView);
        }

        holder.mTitleView.setText(mValues.get(position).getTitle());
        holder.mDescriptionContent.setText(mValues.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mDescriptionContent;
        public final TextView mTitleView;

        public Publication mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.image);
            mTitleView = (TextView) view.findViewById(R.id.title);
            mDescriptionContent = (TextView) view.findViewById(R.id.descriptionContent);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            PublicationDetailFragment publicationDetailFragment = PublicationDetailFragment.newInstance(mItem.getId());

            Context context = view.getContext();

            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_main, publicationDetailFragment);
            fragmentTransaction.commit();
        }
    }
}
