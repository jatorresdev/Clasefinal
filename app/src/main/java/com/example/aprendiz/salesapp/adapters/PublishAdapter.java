package com.example.aprendiz.salesapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.aprendiz.salesapp.R;
import com.example.aprendiz.salesapp.models.Publish;

import java.util.List;

/**
 * Created by APRENDIZ on 21/09/2016.
 */
public class PublishAdapter  extends ArrayAdapter<Publish> {

    Activity context;

    public PublishAdapter(Fragment context,  List<Publish> objects) {
        super(context.getActivity(), R.layout.fragment_search, objects);

        this.context = context.getActivity();
    }

    private static class ViewHolder {
        TextView idPublish;
        TextView Title;
        TextView Photo;
        TextView City;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Publish publish = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.fragment_listar_fragment, parent, false);


            viewHolder.Title = (TextView) convertView.findViewById(R.id.LblDe);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.Title.setText(publish.getTitle());
        return convertView;
    }



}
