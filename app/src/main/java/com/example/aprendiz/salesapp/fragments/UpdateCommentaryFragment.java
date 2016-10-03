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
import android.view.inputmethod.InputMethodManager;
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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   // private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

    private EditText EtIdPublication;
    private EditText EtIdCommentary;
    private EditText EtCommentaryMessage;

    private View mImageProgress;
    private View mProgressBar;
    private View mProgressView;
    private View mRegisterFormView;


    private OnFragmentInteractionListener mListener;

    public UpdateCommentaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param //param1 Parameter 1.
     * @param //param2 Parameter 2.
     * @return A new instance of fragment UpdateCommentaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    /*public static UpdateCommentaryFragment newInstance(String param1, String param2) {
        UpdateCommentaryFragment fragment = new UpdateCommentaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Log", "onCreate");
      /*  if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Log", "onCreateView");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_commentary, container, false);
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

       EtIdPublication=(EditText)view.findViewById(R.id.commentaryEtIdPublication);
         EtIdCommentary=(EditText)view.findViewById(R.id.commentaryEtIdCommentary);
         EtCommentaryMessage=(EditText)view.findViewById(R.id.commentaryEtMesage);
        final Button btnEditarComentar=(Button)view.findViewById(R.id.updateCommentary);

        btnEditarComentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm =  (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(btnEditarComentar.getWindowToken(), 0);
                attemptRegister();
            }
        });

        mRegisterFormView = view.findViewById(R.id.register_user_form);
        mProgressView = view.findViewById(R.id.login_progress);
        mImageProgress = view.findViewById(R.id.image_proggress);
        mProgressBar = view.findViewById(R.id.progress_bar);
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

    private void attemptRegister(){
        EtIdPublication.setError(null);
        EtIdCommentary.setError(null);
        EtCommentaryMessage.setError(null);

        String idPublication= EtIdPublication.getText().toString();
        String idCommentary=EtIdCommentary.getText().toString();
        String message=EtCommentaryMessage.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(idPublication)) {
            EtIdPublication.setError(getString(R.string.register_error_field_required));
            focusView = EtIdPublication;
            cancel = true;
        }

        if (TextUtils.isEmpty(message)) {
            EtCommentaryMessage.setError(getString(R.string.register_error_field_required));
            focusView = EtCommentaryMessage;
            cancel = true;
        }

        if (TextUtils.isEmpty(idCommentary)) {
            EtIdCommentary.setError(getString(R.string.register_error_field_required));
            focusView = EtIdCommentary;
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
            mRegisterTask = new CommentaryUpdateTask(idPublication,message,idCommentary);
            mRegisterTask.updateCommentary();
        }


    }

    private void clearFields() {
        EtIdPublication.setText("");
        EtCommentaryMessage.setText("");
        EtIdCommentary.setText("");
    }

    private boolean isNameValid(String name) {
        return name.length() > 4;
    }

    private boolean isLastNameValid(String lastName) {
        return lastName.length() > 4;
    }

    private boolean isNumberValid(String number) {
        return number.matches("\\d+(?:\\.\\d+)?") && number.length() <= 10;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
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

        public CommentaryUpdateTask(String mIdPublication,String mMessage, String mIdCommentary) {
            this.mIdPublication = mIdPublication;
            this.mIdCommentary = mIdCommentary;
            this.mMessage = mMessage;
        }

        public void updateCommentary() {
        Commentary commentary = new Commentary(mIdPublication,mMessage,mIdCommentary);


        CommentaryService commentaryServices = SalesAPI.createService(CommentaryService.class,
                ((MainActivity) getActivity()).loggedInUserEmail, ((MainActivity) getActivity()).loggedInUserPassword);
        Call<ResponseBody> callUpdateCommentary = commentaryServices.updateCommentary(mIdPublication,mMessage,mIdCommentary);
            callUpdateCommentary.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int code = response.code();
                showProgress(false);
                if (code == 200) {
                    Gson gson = new Gson();

                    try {
                        CommentaryData commentaryDataResponse = gson.fromJson(response.body().string(), CommentaryData.class);
                        Toast.makeText(getActivity(), "Comentario editado exitosamente! "
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
