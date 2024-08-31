package app.preplotus.utilities;

import static app.preplotus.utilities.Constants.USER_ID;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import app.preplotus.R;

import app.preplotus.activities.SuperGroupsActivity;
import app.preplotus.adapters.MyExamsAdapter;
import app.preplotus.databinding.ActivityMySubscriptionBinding;
import app.preplotus.databinding.BtmshtCategoryBinding;
import app.preplotus.model.CategoryData;
import app.preplotus.model.GeneralResponse;
import app.preplotus.model.MyExamPreferencesResponse;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryDialog extends BottomSheetDialogFragment {

    ArrayList<String> superGroupTopics;
    private Context mContext;
    private APIInterface apiInterface;
    private ArrayList<CategoryData> list;
    private ProgressDialog pd;
    private Boolean isEdit = false;

    public CategoryDialog(Context contex) {
        mContext = contex;
    }

    private BtmshtCategoryBinding binding;

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        binding = BtmshtCategoryBinding.inflate(getLayoutInflater());
//        View contentView = View.inflate(getContext(), R.layout.btmsht_category, null);
//        ButterKnife.bind(this, contentView);
        init();
        dialog.setContentView(binding.getRoot());
    }

    private void init() {
        binding.rvList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        binding.rvList.setHasFixedSize(true);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(getContext(), ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        getPreferences();

        binding.tvAddExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddExam();
            }
        });

        binding.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEdit();
            }
        });

    }

    private void getPreferences() {
//        if (!pd.isShowing()) {
//            pd.show();
//        }

        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));

        apiInterface.getMyExamPrefereces(params).enqueue(new Callback<MyExamPreferencesResponse>() {
            @Override
            public void onResponse(Call<MyExamPreferencesResponse> call, Response<MyExamPreferencesResponse> response) {
//                if (pd.isShowing()) {
//                    pd.cancel();
//                }
                binding.rvList.setVisibility(View.VISIBLE);
                binding.pdd.setVisibility(View.GONE);
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        MyExamPreferencesResponse callback = response.body();
                        list = callback.getData();
                        superGroupTopics = new ArrayList<>();
                        MyExamsAdapter adapter = new MyExamsAdapter(mContext, list, false, CategoryDialog.this);
                        binding.rvList.setAdapter(adapter);

                        for (int i = 0; i < list.size(); i++) {
                            CategoryData data = list.get(i);
//                            if (!superGroupTopics.contains(data.getSupergroup_firebase_Name())) {
                                superGroupTopics.add(data.getSupergroup_firebase_Name());
//                            }
                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<MyExamPreferencesResponse> call, Throwable t) {
//                if (pd.isShowing()) {
//                    pd.cancel();
//                }
                binding.pdd.setVisibility(View.GONE);
            }
        });
    }


    public void onAddExam() {
        Intent intent = new Intent(mContext, SuperGroupsActivity.class);
        startActivity(intent);
    }


    public void onEdit() {
        if (isEdit) {
            binding.tvEdit.setText(getResources().getString(R.string.edit));
            isEdit = false;
            MyExamsAdapter adapter = new MyExamsAdapter(mContext, list, false, CategoryDialog.this);
            binding.rvList.setAdapter(adapter);
        } else {
            binding.tvEdit.setText(getResources().getString(R.string.back));
            isEdit = true;
            MyExamsAdapter adapter = new MyExamsAdapter(mContext, list, true, CategoryDialog.this);
            binding.rvList.setAdapter(adapter);
        }
    }

    public void removePreference(final int pos, final CategoryData data) {

        if (!pd.isShowing()) {
            pd.show();
        }

        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));
        params.put("examid", data.getCatId());

        apiInterface.removeExamPreferences(params).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        GeneralResponse callback = response.body();
                        Utils.showToast(mContext, callback.getMessage());
                        list.remove(pos);
                        superGroupTopics.remove(pos);

                        MyExamsAdapter adapter = new MyExamsAdapter(mContext, list, isEdit, CategoryDialog.this);
                        binding.rvList.setAdapter(adapter);

                        FirebaseMessaging.getInstance().unsubscribeFromTopic(data.getFirebase_topic_name());
                        Utils.removeTopic(mContext,data.getFirebase_topic_name());
                        String grpTopic = data.getSupergroup_firebase_Name();

                        if (!superGroupTopics.contains(grpTopic)){
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(grpTopic);
                            Utils.removeTopic(mContext,grpTopic);
                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
            }
        });

    }

}
