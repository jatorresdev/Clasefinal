package com.example.aprendiz.salesapp.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.aprendiz.salesapp.MainActivity;
import com.example.aprendiz.salesapp.R;
import com.example.aprendiz.salesapp.clients.SalesAPI;
import com.example.aprendiz.salesapp.models.PublicationData;
import com.example.aprendiz.salesapp.services.PublicationService;
import com.example.aprendiz.salesapp.utils.ImagesUtils;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PublicationCreate.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublicationCreate#//newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublicationCreate extends Fragment {

    CreatePublicationTask mCreatePublicationTask = null;

    private EditText mTitle;
    private EditText mCity;
    private EditText mDescription;
    private ImageView mPhoto;

    private View mProgressView;
    private View mCreateFormView;

    private Bitmap bitmap;
    private Uri filePath;
    private int PICK_IMAGE_REQUEST = 1;

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
        mDescription = (EditText) view.findViewById(R.id.description);
        mPhoto = (ImageView) view.findViewById(R.id.image);

        Button mImageButton = (Button) view.findViewById(R.id.btn_image);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (shouldAskPermission()) {
                    String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE"};
                    int permsRequestCode = 200;
                    requestPermissions(perms, permsRequestCode);
                } else {
                    showFileChooser();
                }
            }
        });

        Button btnCreatePublication = (Button) view.findViewById(R.id.btn_create_publication);
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

    private void createPublication() {
        mTitle.setError(null);
        mCity.setError(null);
        mDescription.setError(null);

        String title = mTitle.getText().toString();
        String city = mCity.getText().toString();
        String description = mDescription.getText().toString();

        boolean cancel = false;
        View focusedView = null;

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
            showProgress(true);
            mCreatePublicationTask = new CreatePublicationTask(title, city, description, filePath);
            mCreatePublicationTask.createPublication();
            clearFields();
        }
    }


    private void clearFields() {
        mTitle.setText("");
        mCity.setText("");
        mDescription.setText("");
        mPhoto.setImageResource(0);
        filePath = null;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            try {
                //Getting the Bitmap
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                //Setting the Bitmap to ImageView
                mPhoto.setImageBitmap(bitmap);

                // Get real path to make File
                filePath = Uri.parse(ImagesUtils.getPath(getActivity().getContentResolver(), data.getData()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showFileChooser();
            } else {
                Toast.makeText(getActivity(), "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class CreatePublicationTask {

        private final String mTitle;
        private final String mCity;
        private final String mDescription;
        private final Uri mPhoto;

        public CreatePublicationTask(String title, String city, String description, Uri photo) {
            mTitle = title;
            mCity = city;
            mDescription = description;
            mPhoto = photo;
        }

        public void createPublication() {
            RequestBody rbPhoto = null;
            MultipartBody.Part rbpPhoto = null;

            if (mPhoto != null && !mPhoto.toString().isEmpty()) {
                File file = new File(mPhoto.toString());

                rbPhoto = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                rbpPhoto =
                        MultipartBody.Part.createFormData("photo", file.getName(), rbPhoto);
            }

            RequestBody rbTitle = RequestBody.create(MediaType.parse("multipart/form-data"), mTitle);
            RequestBody rbDescription = RequestBody.create(MediaType.parse("multipart/form-data"), mDescription);
            RequestBody rbCity = RequestBody.create(MediaType.parse("multipart/form-data"), mCity);


            PublicationService publicationService = SalesAPI.createService(PublicationService.class,
                    ((MainActivity) getActivity()).loggedInUserEmail, ((MainActivity) getActivity()).loggedInUserPassword);

            Call<PublicationData> callCreatePublication = publicationService.insertPublication(rbTitle,
                    rbDescription, rbCity, rbpPhoto);
            callCreatePublication.enqueue(new Callback<PublicationData>() {
                @Override
                public void onResponse(Call<PublicationData> call, Response<PublicationData> response) {
                    showProgress(false);

                    if (response.isSuccessful()) {
                        Toast.makeText(getActivity(), "Publicación creada "
                                + response.body().getData().getTitle(), Toast.LENGTH_LONG).show();
                    } else {
                        mCreatePublicationTask = null;
                        Toast.makeText(getActivity(), "Ha ocurrido un error al intentar crear la publicación", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<PublicationData> call, Throwable t) {
                    mCreatePublicationTask = null;
                    showProgress(false);

                    Toast.makeText(getActivity(), "Ha ocurrido un error al intentar realizar la publicación", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleccione la imagen"), PICK_IMAGE_REQUEST);
    }

    private boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

}
