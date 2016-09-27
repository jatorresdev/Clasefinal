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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.example.aprendiz.salesapp.models.User;
import com.example.aprendiz.salesapp.models.UserData;
import com.example.aprendiz.salesapp.services.UserService;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterUserFragment extends Fragment {
    private UserRegisterTask mRegisterTask = null;

    //UI references

    private EditText mNameView;
    private EditText mLastNameView;
    private EditText mCellphoneView;
    private EditText mTelephoneView;
    private EditText mEmailView;
    private EditText mPasswordView;

    private View mProgressView;
    private View mRegisterFormView;

    private OnFragmentInteractionListener mListener;

    public RegisterUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterUserFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static RegisterUserFragment newInstance(String param1, String param2) {
//        Log.d("Log", "RegisterUserFragment");
//        RegisterUserFragment fragment = new RegisterUserFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Log", "onCreate");
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Log", "onCreateView");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_user, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up the login form.
        mNameView = (EditText) view.findViewById(R.id.name);
        mLastNameView = (EditText) view.findViewById(R.id.last_name);
        mCellphoneView = (EditText) view.findViewById(R.id.cellphone);
        mTelephoneView = (EditText) view.findViewById(R.id.telephone);
        mEmailView = (EditText) view.findViewById(R.id.email);
        mPasswordView = (EditText) view.findViewById(R.id.password);

        Button mSignUpButton = (Button) view.findViewById(R.id.register_sign_up);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        Button mSignInButton = (Button) view.findViewById(R.id.register_sign_in);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goLogin();
            }
        });

        mRegisterFormView = view.findViewById(R.id.register_user_form);
        mProgressView = view.findViewById(R.id.register_progress);
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

    /**
     * Logica de registro
     */
    private void attemptRegister() {
        // Reset errors.
        mNameView.setError(null);
        mLastNameView.setError(null);
        mCellphoneView.setError(null);
        mTelephoneView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String name = mNameView.getText().toString();
        String lastName = mLastNameView.getText().toString();
        String cellphone = mCellphoneView.getText().toString();
        String telephone = mTelephoneView.getText().toString();
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.login_error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.login_error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.login_error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.login_error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid telephone, if the user entered one.
        if (!TextUtils.isEmpty(telephone) && !isNumberValid(telephone)) {
            mTelephoneView.setError(getString(R.string.register_error_invalid_telephone));
            focusView = mTelephoneView;
            cancel = true;
        }

        // Check for a valid cellphone, if the user entered one.
        if (TextUtils.isEmpty(cellphone)) {
            mCellphoneView.setError(getString(R.string.register_error_field_required));
            focusView = mCellphoneView;
            cancel = true;
        } else if (!isNumberValid(cellphone)) {
            mCellphoneView.setError(getString(R.string.register_error_invalid_cellphone));
            focusView = mCellphoneView;
            cancel = true;
        }

        // Check for a valid last name, if the user entered one.
        if (TextUtils.isEmpty(lastName)) {
            mLastNameView.setError(getString(R.string.register_error_field_required));
            focusView = mLastNameView;
            cancel = true;
        } else if (!isLastNameValid(lastName)) {
            mLastNameView.setError(getString(R.string.register_error_invalid_last_name));
            focusView = mLastNameView;
            cancel = true;
        }

        // Check for a valid name, if the user entered one.
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.register_error_field_required));
            focusView = mNameView;
            cancel = true;
        } else if (!isNameValid(name)) {
            mNameView.setError(getString(R.string.register_error_invalid_name));
            focusView = mNameView;
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
            mRegisterTask = new UserRegisterTask(name, lastName, cellphone, telephone, email, password);
            mRegisterTask.registerUser();
        }
    }

    private void goLogin() {
        LoginFragment loginFragment = new LoginFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_main, loginFragment);
        fragmentTransaction.commit();
    }

    private void clearFields() {
        mNameView.setText("");
        mLastNameView.setText("");
        mCellphoneView.setText("");
        mTelephoneView.setText("");
        mEmailView.setText("");
        mPasswordView.setText("");
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

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login task used to authenticate
     * the user.
     */
    public class UserRegisterTask {

        private final String mName;
        private final String mLastName;
        private final String mCellphone;
        private final String mTelephone;
        private final String mEmail;
        private final String mPassword;

        UserRegisterTask(String name, String lastName, String cellphone, String telephone, String email, String password) {
            mName = name;
            mLastName = lastName;
            mCellphone = cellphone;
            mTelephone = telephone;
            mEmail = email;
            mPassword = password;
        }

        public void registerUser() {
            User user = new User(mName, mLastName, mEmail, mCellphone, mTelephone, null, mPassword);

            UserService userRegisterService = SalesAPI.createService(UserService.class);
            Call<ResponseBody> callRegisterUser = userRegisterService.createUser(user);
            callRegisterUser.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    int code = response.code();
                    showProgress(false);
                    if (code == 200) {
                        Gson gson = new Gson();

                        try {
                            UserData userDataResponse = gson.fromJson(response.body().string(), UserData.class);
                            Toast.makeText(getActivity(), "Usuario registrado exitosamente! "
                                    + userDataResponse.getData().getFullName(), Toast.LENGTH_LONG).show();

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
