package app.preplotus.fragments;

import static android.app.Activity.RESULT_OK;
import static app.preplotus.utilities.Constants.USER_ID;
import static app.preplotus.utilities.Constants.USER_IMAGE;
import static app.preplotus.utilities.Constants.USER_NAME;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import app.preplotus.R;

import app.preplotus.activities.MainActivity;
import app.preplotus.activities.SettingsActivity;
import app.preplotus.model.LoginSignupResponse;
import app.preplotus.model.LoginUserData;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    @BindView(R.id.etName)
    AppCompatEditText etName;
    @BindView(R.id.etEmail)
    AppCompatEditText etEmail;
    @BindView(R.id.etPhoneNumber)
    AppCompatEditText etPhoneNumber;
    @BindView(R.id.imageUser)
    ImageView imageUser;

    private static final int PICK_IMAGE_REQUEST = 1;

    private File actualImage;

    private View view;
    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profilefragment3no, container, false);
        ButterKnife.bind(this, view);

        init();

        return view;
    }

    private void init() {

        mContext = getActivity();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext,ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);


        if (Utils.isNetworkAvailableShowToast(mContext)) {
            fetchProfileDetails();
        }

    }

    @OnClick(R.id.imgSettings)
    public void openSettings() {

        Intent in = new Intent(mContext, SettingsActivity.class);
        startActivity(in);

    }

    private void fetchProfileDetails() {
        if (!pd.isShowing()){
            pd.show();
        }
        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));

        apiInterface.apiFetchProfile(params).enqueue(new Callback<LoginSignupResponse>() {
            @Override
            public void onResponse(Call<LoginSignupResponse> call, Response<LoginSignupResponse> response) {
                if (pd.isShowing()){
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        LoginSignupResponse callback = response.body();
                        if (callback.getStatus().equals("success")) {
                            LoginUserData data = callback.getData();
                            Utils.setPrefData(USER_IMAGE, data.getUserImage(), mContext);
                            etName.setText(data.getName());
                            etEmail.setText(data.getEmail());
                            etPhoneNumber.setText(data.getPhone());

                            try {
                                if (data.getUserImage() != null && data.getUserImage().trim().length() > 0) {
                                    Glide.with(mContext).load(data.getUserImage()).placeholder(R.drawable.profile).into(imageUser);
                                }
                            } catch (Exception e) {
                            }

                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<LoginSignupResponse> call, Throwable t) {
                if (pd.isShowing()){
                    pd.cancel();
                }
            }
        });
    }

    @OnClick(R.id.btnUpdateProfile)
    public void onUpdateProfile() {

        if (Utils.isNetworkAvailableShowToast(mContext)) {
            if (Utils.isNullE(etName)) {
                Utils.showToast(mContext, getResources().getString(R.string.name_msg));
            } else if ((Utils.valE(etPhoneNumber)).trim().length() < 10) {
                Utils.showToast(mContext, getResources().getString(R.string.valid_phone_msg));
            } else {
                apiUpdateProfile();
            }
        }
    }

    private void apiUpdateProfile() {
        MultipartBody.Part body2 = null;
        Map<String, RequestBody> params = new HashMap<>();
        RequestBody userName = RequestBody.create(MultipartBody.FORM, Utils.valE(etName));
        params.put("name", userName);
        RequestBody userNumber = RequestBody.create(MultipartBody.FORM, Utils.valE(etPhoneNumber));
        params.put("phone", userNumber);
        RequestBody userId = RequestBody.create(MultipartBody.FORM, Utils.getPrefData(USER_ID, mContext));
        params.put("userid", userId);

        if (actualImage!=null){
            RequestBody requestFile2 = RequestBody.create(MediaType.parse("multipart/form-data"), actualImage);
            body2 = MultipartBody.Part.createFormData("image", actualImage.getName(), requestFile2);
        }
        if (!pd.isShowing()){
            pd.show();
        }
        apiInterface.updateProfile(params,body2).enqueue(new Callback<LoginSignupResponse>() {
            @Override
            public void onResponse(Call<LoginSignupResponse> call, Response<LoginSignupResponse> response) {
                if (pd.isShowing()){
                    pd.cancel();
                }

                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body()!=null) {
                        LoginSignupResponse callback = response.body();
                        Utils.showToast(mContext,callback.getMessage());
                        Utils.setPrefData(USER_NAME, Utils.valE(etName), mContext);

                        if (actualImage!=null){

                            Utils.setPrefData(USER_IMAGE, callback.getData().getUserImage(), mContext);
                        }
                        ((MainActivity)getActivity()).setUserData();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LoginSignupResponse> call, Throwable t) {

                if (pd.isShowing()){
                    pd.cancel();
                }
            }
        });

    }

    @OnClick(R.id.rlProfile)
    public void openGallery(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Choose Image"),
                PICK_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(mContext, getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                Bitmap bm = BitmapFactory.decodeStream(inputStream);
                actualImage = getFile(bm);

                Glide.with(mContext).load(BitmapFactory.decodeFile(actualImage.getAbsolutePath())).into(imageUser);
            } catch (Exception e) {
                Toast.makeText(mContext, getResources().getString(R.string.something_wrong_msg), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }

    private File getFile(Bitmap bitmap) {
        File imageFile = new File(mContext.getFilesDir() + "capture.jpg");
        try {
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            return imageFile;
        } catch (Exception e) {
        }
        return null;
    }

}