package com.example.aprendiz.clasefinal;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.aprendiz.salesapp.R;
import com.example.aprendiz.salesapp.adapters.*;
import com.example.aprendiz.salesapp.clients.PublishRestClient;
import com.example.aprendiz.salesapp.models.Publish;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SearchFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    private ListView publishList;
    ArrayList<Publish> noteArray;

    /*prueba comentario¨*/
    private void getPublish() {
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Accept", "application/json"));
        PublishRestClient.get(SearchFragment.this, "api/notes", headers.toArray(new Header[headers.size()]),
                null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        ArrayList<Publish> noteArray = new ArrayList<Publish>();


                        PublishAdapter noteAdapter= new PublishAdapter(SearchFragment.this, noteArray);

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                noteAdapter.add(new Publish(response.getJSONObject(i)));

                                System.out.println("johan" + response.getJSONObject(i).toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        publishList = (ListView)getView().findViewById(R.id.list_publish);
                        publishList.setAdapter(noteAdapter);
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getPublish();

        System.out.println("johan");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        publishList = (ListView)getView().findViewById(R.id.list_publish);

        //publishList.setAdapter(new PublishAdapter(this));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


/*
    class PublishAdapter  extends ArrayAdapter<Publish> {

        Activity context;

        public PublishAdapter(Fragment context) {
            super(context.getActivity(), R.layout.fragment_search, noteArray);

            this.context = context.getActivity();
        }

        private class ViewHolder {
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
                convertView = inflater.inflate(R.layout.fragment_search, parent, false);


                viewHolder.Title = (TextView) convertView.findViewById(R.id.Title);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.Title.setText(publish.getTitle());
            return convertView;
        }



    }*/

}
