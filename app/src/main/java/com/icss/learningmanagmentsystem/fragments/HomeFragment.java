package com.icss.learningmanagmentsystem.fragments;

import static com.icss.learningmanagmentsystem.utilities.Constants.CATEGORY_ID;
import static com.icss.learningmanagmentsystem.utilities.Constants.SUBSCRIBED;
import static com.icss.learningmanagmentsystem.utilities.Constants.SUPER_GROUP_ID;
import static com.icss.learningmanagmentsystem.utilities.Constants.USER_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.icss.learningmanagmentsystem.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.icss.learningmanagmentsystem.adapters.MyExamsAdapter;
import com.icss.learningmanagmentsystem.adapters.MySubscriptionsAdapter;
import com.icss.learningmanagmentsystem.adapters.PackagesAdapter;
import com.icss.learningmanagmentsystem.adapters.SubjectsAdapter;
import com.icss.learningmanagmentsystem.model.CategoryData;
import com.icss.learningmanagmentsystem.model.MyExamPreferencesResponse;
import com.icss.learningmanagmentsystem.model.SubscriptionData;
import com.icss.learningmanagmentsystem.model.SubscriptionResponse;
import com.icss.learningmanagmentsystem.model.TestsTabResponse;
import com.icss.learningmanagmentsystem.modelclass.CategoryNameDataModel;
import com.icss.learningmanagmentsystem.adapters.TestsAdapter;
import com.icss.learningmanagmentsystem.modelclass.LatestDataModel;

import com.icss.learningmanagmentsystem.modelclass.HomeSecondDataModel;
import com.icss.learningmanagmentsystem.activities.SubscriptionActivity;
import com.icss.learningmanagmentsystem.activities.ViewAllActivity;
import com.icss.learningmanagmentsystem.network.APIClient;
import com.icss.learningmanagmentsystem.network.APIInterface;
import com.icss.learningmanagmentsystem.utilities.CategoryDialog;
import com.icss.learningmanagmentsystem.utilities.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView, recyclerView1;
    ImageView img2;
    RecyclerView recyclerView3;
    TextView view_all;
    TextView mock_test;
    ImageView imageView;
    TextView exm_name;
    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = getActivity();
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);
        view_all = view.findViewById(R.id.view_all);
        mock_test = view.findViewById(R.id.mock_test);
        img2 = view.findViewById(R.id.img2);

        imageView = view.findViewById(R.id.img2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), SubscriptionActivity.class);
                    getActivity().startActivity(intent);
            }
        });

        mock_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        view_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ViewAllActivity.class);
                getActivity().startActivity(intent);
            }
        });

        recyclerView1 = view.findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView1.setLayoutManager(linearLayoutManager1);

        recyclerView = view.findViewById(R.id.recycler_2);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView3 = view.findViewById(R.id.recycler_3);

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getActivity(), 3);
        recyclerView3.setLayoutManager(gridLayoutManager1);

        fetchData();

        return view;
    }



    private void fetchData() {

        if (!pd.isShowing()) {
            pd.show();
        }

        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));
        params.put("categoryid", Utils.getPrefData(CATEGORY_ID, mContext));

        Log.e("ttt",Utils.getPrefData(CATEGORY_ID, mContext));

        apiInterface.fetchTestsTabData(params).enqueue(new Callback<TestsTabResponse>() {
            @Override
            public void onResponse(Call<TestsTabResponse> call, Response<TestsTabResponse> response) {
                try {
                    if (pd.isShowing()) {
                        pd.cancel();
                    }
                } catch (Exception e){}
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        TestsTabResponse callback = response.body();

                        TestsAdapter testsAdapter = new TestsAdapter(mContext, callback.getLatest_test());
                        recyclerView.setAdapter(testsAdapter);

                        SubjectsAdapter subjectsAdapter = new SubjectsAdapter(mContext, callback.getSubject_list());
                        recyclerView3.setAdapter(subjectsAdapter);

                        PackagesAdapter packagesAdapter = new PackagesAdapter(mContext, callback.getMock_test());
                        recyclerView1.setAdapter(packagesAdapter);

                        Glide.with(mContext).load(response.body().getOffer_image()).into(img2);

                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<TestsTabResponse> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
            }
        });

    }

}