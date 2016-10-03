package com.example.aprendiz.salesapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aprendiz.salesapp.R;
import com.example.aprendiz.salesapp.adapters.PublicationRecyclerViewAdapter;
import com.example.aprendiz.salesapp.clients.SalesAPI;
import com.example.aprendiz.salesapp.models.Commentary;
import com.example.aprendiz.salesapp.models.CommentaryData;
import com.example.aprendiz.salesapp.models.CommentaryDataList;
import com.example.aprendiz.salesapp.models.Publication;
import com.example.aprendiz.salesapp.models.PublicationData;
import com.example.aprendiz.salesapp.models.PublicationDataList;
import com.example.aprendiz.salesapp.services.CommentaryService;
import com.example.aprendiz.salesapp.services.PublicationService;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PublicationDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublicationDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublicationDetailFragment extends Fragment {

    private static final String ARG_ID_PUBLICATION = "idPublication";
    private String idPublication;

    private TextView mTitle;
    private TextView mCity;
    private TextView mDescription;
    private ImageView mPhoto;
    private ListView  listVista;
    Button tbnBacktoList,btnEdiPublication,btnAddCommentary;
    //EnviarDatos EM;
    Activity activity;



    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param //param1 Parameter 1.
     * @param //param2 Parameter 2.
     * @return A new instance of fragment PublicationDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PublicationDetailFragment newInstance(String idPublication) {
        PublicationDetailFragment fragment = new PublicationDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID_PUBLICATION, idPublication);
        fragment.setArguments(args);
        return fragment;
    }


    public PublicationDetailFragment() {
        // Required empty public constructor
    }

   /* public interface EnviarDatos {

        public void enviarInformacion(String title);
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idPublication = getArguments().getString(ARG_ID_PUBLICATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_publication_detail, container, false);

        tbnBacktoList=(Button)view.findViewById(R.id.btnBack);
        btnEdiPublication=(Button)view.findViewById(R.id.btnEditPublication);
        btnAddCommentary=(Button)view.findViewById(R.id.btnNewCommentary);

        //mTitle=(TextView)view.findViewById(R.id.edTitle);


        tbnBacktoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity=getActivity();

                PublicationFragment publicationFragment= new PublicationFragment();
                FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, publicationFragment);
                fragmentTransaction.commit();
            }
        });
        btnEdiPublication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity=getActivity();
               // PublicationUpdate publicationUpdate=new PublicationUpdate();
                PublicationUpdate publicationUpdate = PublicationUpdate.newInstance(idPublication);
                FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, publicationUpdate);
                fragmentTransaction.commit();


            }
        });

        btnAddCommentary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity=getActivity();

                RegisterCommentaryFragment registerCommentaryFragment = new RegisterCommentaryFragment();
                FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main,registerCommentaryFragment );
                fragmentTransaction.commit();
            }

        });


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitle = (TextView) view.findViewById(R.id.title);
        mCity = (TextView) view.findViewById(R.id.city);
        mDescription = (TextView) view.findViewById(R.id.description);
        mPhoto = (ImageView) view.findViewById(R.id.image);
        listVista=(ListView)view.findViewById(R.id.listCommentary);

        getPublicationId(idPublication);
        getCommentaryPublicationId(idPublication,view.getContext());
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

    public void getPublicationId(String id) {
        PublicationService publicationShowService = SalesAPI.createService(PublicationService.class);

        Call<PublicationData> callPublicationShow = publicationShowService.getPublicationById(id);
        callPublicationShow.enqueue(new Callback<PublicationData>() {
            @Override
            public void onResponse(Call<PublicationData> call, Response<PublicationData> response) {
                if (response.isSuccessful()) {
                    Publication publication = response.body().getData();

                    mTitle.setText(publication.getTitle());
                    mCity.setText("Ciudad: " + publication.getCity());
                    mDescription.setText("Descripción: " + publication.getDescription());

                    if (!publication.getPhoto().isEmpty()) {
                        Picasso.with(getContext())
                                .load(publication.getPhoto())
                                .into(mPhoto);
                    }

                } else {
                    Toast.makeText(getActivity(), "Ha ocurrido un error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PublicationData> call, Throwable t) {
                Toast.makeText(getActivity(), "Ha ocurrido un error", Toast.LENGTH_LONG).show();
            }
        });


    }

    public void getCommentaryPublicationId(final String id, final Context context) {

        CommentaryService commentaryShowService = SalesAPI.createService(CommentaryService.class);
        Call<ResponseBody> callCommentaryShow = commentaryShowService.getCommentaryForId(id);
        callCommentaryShow.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    try {

                        Commentary comentaryObj;

                        ArrayList <Commentary> listado;
                        CommentaryDataList commentaryDataList = gson.fromJson(response.body().string(), CommentaryDataList.class);
                        List<Commentary> commentaries = commentaryDataList.getData();
                        List<String> datos = new ArrayList<String>();
                        for (int i=0;i<commentaries.size();i++) {

                            comentaryObj=commentaries.get(i);
                            //listVista.setContentDescription(comentaryObj.getMessage());
                            datos.add(i,comentaryObj.getMessage());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_expandable_list_item_1,datos);

                        /*listado=new ArrayList<Commentary>(commentaries);*/
                        listVista.setAdapter(adapter);


                        //recyclerView.setAdapter(new PublicationRecyclerViewAdapter(publications, mListener));

                    } catch (IOException e) {
                        Toast.makeText(getActivity(), "Ha ocurrido un error obteniendo las publicaciones", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "Ha ocurrido un error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(), "Ha ocurrido un error", Toast.LENGTH_LONG).show();
            }
        });


    }
}
