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
import com.example.aprendiz.salesapp.models.Publication;
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
 * {@link PublicationUpdate.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PublicationUpdate#//newInstance} factory method to
 * create an instance of this fragment.
 */
public class PublicationUpdate extends Fragment {

    private UpdatePublicationTask mUpdatePublicationTask = null;

    private EditText mIdPublication;
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

    public PublicationUpdate() {
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
        return inflater.inflate(R.layout.fragment_publication_update, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIdPublication = (EditText) view.findViewById(R.id.edIdPublication);
        mTitle = (EditText) view.findViewById(R.id.edTitle);
        mCity = (EditText) view.findViewById(R.id.edCity);
        mDescription = (EditText) view.findViewById(R.id.edDescription);
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

        Button btnUpdatePublication = (Button) view.findViewById(R.id.btn_update_publication);
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
     * <p/>
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

        String idPublication = mIdPublication.getText().toString();
        String title = mTitle.getText().toString();
        String city = mCity.getText().toString();
        String description = mDescription.getText().toString();

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
            showProgress(true);
            mUpdatePublicationTask = new UpdatePublicationTask(idPublication, title, city, description, filePath);
            mUpdatePublicationTask.updatePublication();
            clearFields();
        }
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

    private void clearFields() {
        mIdPublication.setText("");
        mTitle.setText("");
        mCity.setText("");
        mDescription.setText("");
        mPhoto.setImageResource(0);
        filePath = null;
    }

    public class UpdatePublicationTask {

        private final String mIdPublication;
        private final String mTitle;
        private final String mCity;
        private final String mDescription;
        private final Uri mPhoto;

        public UpdatePublicationTask(String IdPublication, String title, String city, String description, Uri photo) {
            mIdPublication = IdPublication;
            mTitle = title;
            mCity = city;
            mDescription = description;
            mPhoto = photo;
        }

        public void updatePublication() {
            RequestBody rbPhoto = null;
            MultipartBody.Part rbpPhoto = null;

            if (mPhoto != null && !mPhoto.toString().isEmpty()) {
                File file = new File(mPhoto.toString());

                rbPhoto = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                rbpPhoto =
                        MultipartBody.Part.createFormData("photo", file.getName(), rbPhoto);
            }

            RequestBody rbTitle = RequestBody.create(MediaType.parse("text/plain"), mTitle);
            RequestBody rbDescription = RequestBody.create(MediaType.parse("text/plain"), mDescription);
            RequestBody rbCity = RequestBody.create(MediaType.parse("text/plain"), mCity);
            RequestBody rbMethod = RequestBody.create(MediaType.parse("text/plain"), "PUT");

            PublicationService publicationService = SalesAPI.createService(PublicationService.class,
                    ((MainActivity) getActivity()).loggedInUserEmail, ((MainActivity) getActivity()).loggedInUserPassword);

            Call<PublicationData> callUpdatePublication = publicationService.updatePublication(mIdPublication,
                    rbTitle, rbDescription, rbCity, rbpPhoto, rbMethod);
            callUpdatePublication.enqueue(new Callback<PublicationData>() {

                @Override
                public void onResponse(Call<PublicationData> call, Response<PublicationData> response) {
                    showProgress(false);
                    if (response.isSuccessful()) {

                        Publication publication = response.body().getData();

                        Toast.makeText(getActivity(), "Publicacion editada exitosamente! "
                                + publication.getTitle(), Toast.LENGTH_LONG).show();

                    } else {
                        mUpdatePublicationTask = null;
                        Toast.makeText(getActivity(), "Ha ocurrido un error al intentar editar la publicación", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<PublicationData> call, Throwable t) {
                    mUpdatePublicationTask = null;
                    showProgress(false);
                    Toast.makeText(getActivity(), "Ha ocurrido un error al intentar editar la publicación", Toast.LENGTH_LONG).show();
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
