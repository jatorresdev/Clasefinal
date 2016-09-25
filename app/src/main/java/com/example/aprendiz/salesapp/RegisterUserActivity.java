package com.example.aprendiz.salesapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aprendiz.salesapp.clients.SalesAPI;
import com.example.aprendiz.salesapp.models.User;
import com.example.aprendiz.salesapp.models.UserData;
import com.example.aprendiz.salesapp.services.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUserActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);


        // Set up the login form.
        mNameView = (EditText) findViewById(R.id.name);
        mLastNameView = (EditText) findViewById(R.id.last_name);
        mCellphoneView = (EditText) findViewById(R.id.cellphone);
        mTelephoneView = (EditText) findViewById(R.id.telephone);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mSignUpButton = (Button) findViewById(R.id.register_sign_up);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mRegisterFormView = findViewById(R.id.register_user_form);
        mProgressView = findViewById(R.id.register_progress);
    }

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
            Call<UserData> callRegisterUser = userRegisterService.createUser(user);
            callRegisterUser.enqueue(new Callback<UserData>() {
                @Override
                public void onResponse(Call<UserData> call, Response<UserData> response) {
                    showProgress(false);
                    if (response.isSuccessful()) {
                        User userResponse = response.body().getData();

                        Toast.makeText(RegisterUserActivity.this, "Usuario registrado exitosamente! " + userResponse.getFullName(), Toast.LENGTH_LONG).show();

                    } else {
                        mRegisterTask = null;
                        Toast.makeText(RegisterUserActivity.this, "El correo ingresado ya esta en uso", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<UserData> call, Throwable t) {
                    mRegisterTask = null;
                    showProgress(false);

                    Toast.makeText(RegisterUserActivity.this, "Ha ocurrido un error al intentar realizar el registro", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
