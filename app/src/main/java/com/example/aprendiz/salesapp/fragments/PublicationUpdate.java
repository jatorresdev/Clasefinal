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

import com.example.aprendiz.salesapp.MainActivity;
import com.example.aprendiz.salesapp.R;
import com.example.aprendiz.salesapp.clients.SalesAPI;
import com.example.aprendiz.salesapp.models.CommentaryData;
import com.example.aprendiz.salesapp.models.Publication;
import com.example.aprendiz.salesapp.models.PublicationData;
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
 * {@link PublicationUpdate.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublicationUpdate#//newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublicationUpdate extends Fragment {

    private UpdatePublicationTask mUpdatePublicationTask = null;

    private EditText  mIdPublication;
    private EditText mTitle;
    private EditText mCity;
    private EditText mDescription;
    private EditText mPhoto;

    private View mProgressView;
    private View mCreateFormView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PublicationUpdate() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param //param1 Parameter 1.
     * @param //param2 Parameter 2.
     * @return A new instance of fragment PublicationUpdate.
     */
    // TODO: Rename and change types and number of parameters
  /*  public static PublicationUpdate newInstance(String param1, String param2) {
        PublicationUpdate fragment = new PublicationUpdate();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_publication_update, container, false);
    }



    // TODO: Rename method, update argument and hook method into UI event
   /* public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIdPublication=(EditText)view.findViewById(R.id.edIdPublication);
        mTitle=(EditText)view.findViewById(R.id.edTitle);
        mCity=(EditText)view.findViewById(R.id.edCity);
        mDescription=(EditText)view.findViewById(R.id.edDescription);
        mPhoto=null;

        Button btnUpdatePublication=(Button)view.findViewById(R.id.btn_update_publication);
        btnUpdatePublication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePublication();
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
       // mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void updatePublication() {
        mIdPublication.setError(null);
        mTitle.setError(null);
        mCity.setError(null);
        mDescription.setError(null);

        String idPublication=mIdPublication.getText().toString();
        String title = mTitle.getText().toString();
        String city = mCity.getText().toString();
        String description = mDescription.getText().toString();
        String photo = null;

        boolean cancel = false;
        View focusedView = null;

        if (TextUtils.isEmpty(idPublication)) {
            mIdPublication.setError(getString(R.string.publication_error_field_required));
            focusedView = mIdPublication;
            cancel = true;
        }

        if (TextUtils.isEmpty(description)) {
            mDescription.setError(getString(R.string.publication_error_field_required));
            focusedView = mDescription;
            cancel = true;
        }

        if (TextUtils.isEmpty(city)) {
            mCity.setError(getString(R.string.publication_error_field_required));
            focusedView = mCity;
            cancel = true;
        }

        if (TextUtils.isEmpty(title)) {
            mTitle.setError(getString(R.string.publication_error_field_required));
            focusedView = mTitle;
            cancel = true;
        }

        if (cancel) {
            focusedView.requestFocus();
        } else {
            clearFields();
            mUpdatePublicationTask = new UpdatePublicationTask(idPublication,title, city, description, photo);
            mUpdatePublicationTask.updatePublication();
        }
    }

    private void clearFields() {
        mIdPublication.setText("");
        mTitle.setText("");
        mCity.setText("");
        mDescription.setText("");
    }

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

    public class UpdatePublicationTask {

        private final String mIdPublication;
        private final String mTitle;
        private final String mCity;
        private final String mDescription;
        private final String mPhoto;

        public UpdatePublicationTask(String IdPublication,String title, String city, String description, String photo) {
            mIdPublication=IdPublication;
            mTitle = title;
            mCity = city;
            mDescription = description;
            mPhoto = photo;
        }


        public void updatePublication() {
        Publication publication = new Publication(mIdPublication,mTitle, mDescription, mCity, mPhoto,null);

        PublicationService publicationService = SalesAPI.createService(PublicationService.class,
                ((MainActivity) getActivity()).loggedInUserEmail, ((MainActivity) getActivity()).loggedInUserPassword);

        Call<ResponseBody> callUpdatePublication = publicationService.updatePublication(mIdPublication,mTitle,mDescription,mCity,mPhoto);
            callUpdatePublication.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    int code = response.code();
                    showProgress(false);
                    if (code == 200) {
                        Gson gson = new Gson();

                        try {
                            CommentaryData commentaryDataResponse = gson.fromJson(response.body().string(), CommentaryData.class);
                            Toast.makeText(getActivity(), "Publicacion editado exitosamente! "
                                    + commentaryDataResponse.getData().getFullName(), Toast.LENGTH_LONG).show();

                        } catch (IOException e) {
                            Toast.makeText(getActivity(), "Ha ocurrido un error al intentar realizar el registro", Toast.LENGTH_LONG).show();
                        }
                    } else if (code == 500) {
                        try {
                            Log.d("Error", response.errorBody().string());
                            Toast.makeText(getActivity(), "El correo ingresado ya ha sido registrado", Toast.LENGTH_LONG).show();

                        } catch (IOException e) {
                            Toast.makeText(getActivity(), "Ha ocurrido un error al intentar realizar el registro", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        mUpdatePublicationTask = null;
                        Toast.makeText(getActivity(), "Ha ocurrido un error al intentar realizar el registro", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    mUpdatePublicationTask = null;
                    showProgress(false);

                    Toast.makeText(getActivity(), "Ha ocurrido un error al intentar realizar el registro", Toast.LENGTH_LONG).show();
                }

            });
        }
    }


}
