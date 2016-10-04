package com.example.aprendiz.salesapp.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aprendiz.salesapp.MainActivity;
import com.example.aprendiz.salesapp.R;
import com.example.aprendiz.salesapp.clients.SalesAPI;
import com.example.aprendiz.salesapp.models.Commentary;
import com.example.aprendiz.salesapp.models.CommentaryData;
import com.example.aprendiz.salesapp.services.CommentaryService;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UpdateCommentaryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UpdateCommentaryFragment#//newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateCommentaryFragment extends Fragment {

    private CommentaryUpdateTask mRegisterTask = null;
    private static final String ARG_ID_PUBLICATION = "idPublication";
    private static final String ARG_ID_COMMENTARY = "idCommentary";

    private EditText mCommentaryMessage;

    private View mImageProgress;
    private View mProgressBar;
    private View mProgressView;
    private View mRegisterFormView;

    private String idPublication;
    private String idCommentary;

    private Button btnUpdateCommentary;
    private Button btnCancelUpdateCommentary;
    private Button btnDeleteUpdateCommentary;

    Activity activity;

    private OnFragmentInteractionListener mListener;

    public UpdateCommentaryFragment() {
        // Required empty public constructor
    }

    public static UpdateCommentaryFragment newInstance(String idPublication, String idCommentary) {
        UpdateCommentaryFragment fragment = new UpdateCommentaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID_PUBLICATION, idPublication);
        args.putString(ARG_ID_COMMENTARY, idCommentary);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idPublication = getArguments().getString(ARG_ID_PUBLICATION);
            idCommentary = getArguments().getString(ARG_ID_COMMENTARY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_commentary, container, false);

        btnCancelUpdateCommentary = (Button) view.findViewById(R.id.CancelUpdateCommentary);
        btnCancelUpdateCommentary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity = getActivity();

                PublicationDetailFragment publicationDetailFragment = PublicationDetailFragment.newInstance(idPublication);
                FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, publicationDetailFragment);
                fragmentTransaction.commit();
            }
        });

        btnDeleteUpdateCommentary = (Button) view.findViewById(R.id.deleteCommentary);
        btnDeleteUpdateCommentary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                deleteCommentary();
            }
        });

        btnUpdateCommentary = (Button) view.findViewById(R.id.updateCommentary);

        btnUpdateCommentary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCommentaryMessage = (EditText) view.findViewById(R.id.message);
        mRegisterFormView = view.findViewById(R.id.update_commentary_form);
        mProgressView = view.findViewById(R.id.login_progress);
        mImageProgress = view.findViewById(R.id.image_proggress);
        mProgressBar = view.findViewById(R.id.progress_bar);

        getCommentId(idPublication, idCommentary);
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

    public void getCommentId(String idPublication, String id) {
        CommentaryService commentaryService = SalesAPI.createService(CommentaryService.class);

        Call<CommentaryData> callCommentaryShow = commentaryService.getCommentaryById(idPublication, id);
        callCommentaryShow.enqueue(new Callback<CommentaryData>() {
            @Override
            public void onResponse(Call<CommentaryData> call, Response<CommentaryData> response) {
                if (response.isSuccessful()) {
                    Commentary commentary = response.body().getData();

                    mCommentaryMessage.setText(commentary.getMessage());
                } else {
                    Toast.makeText(getActivity(), "Ha ocurrido un error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CommentaryData> call, Throwable t) {
                Toast.makeText(getActivity(), "Ha ocurrido un error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void deleteCommentary() {
        CommentaryService commentaryService = SalesAPI.createService(CommentaryService.class,
                ((MainActivity) getActivity()).loggedInUserEmail, ((MainActivity) getActivity()).loggedInUserPassword);

        Call<ResponseBody> callPublicationShow = commentaryService.deleteCommentary(idPublication, idCommentary);
        callPublicationShow.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                showProgress(false);

                if (response.isSuccessful()) {
                    activity = getActivity();

                    PublicationDetailFragment publicationDetailFragment = PublicationDetailFragment.newInstance(idPublication);
                    FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.content_main, publicationDetailFragment);
                    fragmentTransaction.commit();

                    Toast.makeText(getActivity(), "Comentario eliminado de forma exitosa", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getActivity(), "Ha ocurrido un error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showProgress(false);
                Toast.makeText(getActivity(), "Ha ocurrido un error elimiando la publicación", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void attemptRegister() {
        mCommentaryMessage.setError(null);

        String message = mCommentaryMessage.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(message)) {
            mCommentaryMessage.setError(getString(R.string.register_error_field_required));
            focusView = mCommentaryMessage;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user register attempt.
            clearFields();
            showProgress(true);
            mRegisterTask = new CommentaryUpdateTask(idPublication, idCommentary, message);
            mRegisterTask.updateCommentary();
        }
    }

    private void clearFields() {
        mCommentaryMessage.setText("");
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mImageProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
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
            mImageProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class CommentaryUpdateTask {
        private final String mIdPublication;
        private final String mIdCommentary;
        private final String mMessage;

        public CommentaryUpdateTask(String idPublication, String idCommentary, String message) {
            this.mIdPublication = idPublication;
            this.mIdCommentary = idCommentary;
            this.mMessage = message;
        }

        public void updateCommentary() {
            CommentaryService commentaryServices = SalesAPI.createService(CommentaryService.class,
                    ((MainActivity) getActivity()).loggedInUserEmail, ((MainActivity) getActivity()).loggedInUserPassword);

            Call<ResponseBody> callUpdateCommentary = commentaryServices.updateCommentary(mIdPublication, mMessage, mIdCommentary);
            callUpdateCommentary.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    showProgress(false);
                    if (response.isSuccessful()) {
                        Gson gson = new Gson();

                        try {
                            CommentaryData commentaryDataResponse = gson.fromJson(response.body().string(), CommentaryData.class);
                            Toast.makeText(getActivity(), "Comentario editado exitosamente! "
                                    + commentaryDataResponse.getData().getMessage(), Toast.LENGTH_LONG).show();

                            activity = getActivity();

                            PublicationDetailFragment publicationDetailFragment = PublicationDetailFragment.newInstance(mIdPublication);
                            FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.content_main, publicationDetailFragment);
                            fragmentTransaction.commit();

                        } catch (IOException e) {
                            Toast.makeText(getActivity(), "Ha ocurrido un error al intentar realizar el registro", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        mRegisterTask = null;
                        Toast.makeText(getActivity(), "Ha ocurrido un error al intentar realizar el registro", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    mRegisterTask = null;
                    showProgress(false);

                    Toast.makeText(getActivity(), "Ha ocurrido un error al intentar realizar el registro", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
