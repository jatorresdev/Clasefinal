package com.example.aprendiz.salesapp.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aprendiz.salesapp.R;
import com.example.aprendiz.salesapp.clients.SalesAPI;
import com.example.aprendiz.salesapp.models.Publication;
import com.example.aprendiz.salesapp.models.UserData;
import com.example.aprendiz.salesapp.services.PublicationService;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PublicationCreate.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublicationCreate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublicationCreate extends Fragment {

    CreatePublicationTask mCreatePublicationTask = null;

    private EditText mTitle;
    private EditText mCity;
    private EditText mDescription;
    private EditText mPhoto;

    private View mProgressView;
    private View mCreateFormView;

    private OnFragmentInteractionListener mListener;

    public PublicationCreate() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_publication_create, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitle = (EditText) view.findViewById(R.id.title);
        mCity = (EditText) view.findViewById(R.id.city);
        mDescription = (EditText)view.findViewById(R.id.description);
        mPhoto = null;
        Button btnCreatePublication = (Button)view.findViewById(R.id.btn_create_publication);
        btnCreatePublication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPublication();
            }
        });

        mCreateFormView = view.findViewById(R.id.create_publication_form);
        mProgressView = view.findViewById(R.id.create_progress);

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

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void createPublication(){
        mTitle.setError(null);
        mCity.setError(null);
        mDescription.setError(null);

        String title = mTitle.getText().toString();
        String city = mCity.getText().toString();
        String description = mDescription.getText().toString();
        String photo = null;

        boolean cancel = false;
        View focusedView = null;

        if (TextUtils.isEmpty(title)){
            mTitle.setError("El titulo esta vacio");
            focusedView = mTitle;
            cancel = true;
        }else{
            cancel = false;
        }
        if (TextUtils.isEmpty(city)){
            mTitle.setError("La ciudad esta vacia");
            focusedView = mCity;
            cancel = true;
        }else{
            cancel = false;
        }
        if (TextUtils.isEmpty(description)){
            mTitle.setError("La descripcion esta vacia");
            focusedView = mDescription;
            cancel = true;
        }else{
            cancel = false;
        }

        if(cancel){
            focusedView.requestFocus();
        }else{
            clearFields();
            mCreatePublicationTask = new CreatePublicationTask(title,description,city,photo);
            mCreatePublicationTask.createPublication();
        }
    }


    private void clearFields (){
        mTitle.setText("");
        mCity.setText("");
        mDescription.setText("");
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mCreateFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mCreateFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mCreateFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mCreateFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class CreatePublicationTask {

        private final String mTitle;
        private final String mCity;
        private final String mDescription;
        private final String mPhoto;

        public CreatePublicationTask(String title, String city, String description,String photo) {
            mTitle = title;
            mCity = city;
            mDescription = description;
            mPhoto = photo;

        }

        public void createPublication(){
            Publication publication = new Publication(mTitle,mDescription,mCity,mPhoto,null);

            PublicationService publicationService = SalesAPI.createService(PublicationService.class,"bsmunoz5@misena.edu.co","11111");
            Call<ResponseBody> callCreatePublication = publicationService.insertPublication(publication);
            callCreatePublication.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    int code = response.code();
                    showProgress(false);
                    if (code == 200) {
                        Gson gson = new Gson();

                        try {
                            UserData userDataResponse = gson.fromJson(response.body().string(), UserData.class);
                            Toast.makeText(getActivity(), "Publicacion guardada Exitosamente!"
                                    + userDataResponse.getData().getFullName(), Toast.LENGTH_LONG).show();

                        } catch (IOException e) {
                            Toast.makeText(getActivity(), "Ha ocurrido un error al intentar realizar la publicacion", Toast.LENGTH_LONG).show();
                        }
                    } else if (code == 500) {
                        try {
                            Log.d("Error", response.errorBody().string());
                            Toast.makeText(getActivity(), "El correo ingresado ya ha sido registrado", Toast.LENGTH_LONG).show();

                        } catch (IOException e) {
                            Toast.makeText(getActivity(), "Ha ocurrido un error al intentar realizar la publicacion", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        mCreatePublicationTask = null;
                        Toast.makeText(getActivity(), "Ha ocurrido un error al intentar realizar la publicacion", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    mCreatePublicationTask = null;
                    showProgress(false);

                    Toast.makeText(getActivity(), "Ha ocurrido un error al intentar realizar la publicacion", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}
