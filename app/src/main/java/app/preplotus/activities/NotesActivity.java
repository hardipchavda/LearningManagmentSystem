package app.preplotus.activities;

import static app.preplotus.utilities.Constants.CATEGORY_ID;
import static app.preplotus.utilities.Constants.USER_ID;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import app.preplotus.R;
import app.preplotus.adapters.NotesAdapter;
import app.preplotus.model.NotesResponse;
import app.preplotus.network.APIClient;
import app.preplotus.network.APIInterface;
import app.preplotus.utilities.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotesActivity extends AppCompatActivity {

    @BindView(R.id.rvNotes)
    RecyclerView rvNotes;
    @BindView(R.id.tvTitle)
    AppCompatTextView tvTitle;

    private Context mContext;
    private APIInterface apiInterface;
    private ProgressDialog pd;
    private String from;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        ButterKnife.bind(this);
        from = getIntent().getStringExtra("from");
        init();
    }

    @Override
    public void onBackPressed() {
        if (from!=null && from.equals("notification")){
            Intent in = new Intent(mContext,MainActivity.class);
            startActivity(in);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.iconBack)
    public void onBack() {
        onBackPressed();
    }

    private void init() {

        mContext = NotesActivity.this;
        apiInterface = APIClient.getClient().create(APIInterface.class);
        pd = new ProgressDialog(mContext, ProgressDialog.STYLE_SPINNER);
        pd.setMessage(getResources().getString(R.string.please_wait));
        pd.setCancelable(false);

        tvTitle.setText(getResources().getString(R.string.notes));

        rvNotes.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

        if (Utils.isNetworkAvailableShowToast(mContext)) {
            fetchNotes();
        }
    }

    private void fetchNotes() {

        if (!pd.isShowing()) {
            pd.show();
        }

        Map<String, String> params = new HashMap<>();

        params.put("userid", Utils.getPrefData(USER_ID, mContext));
        params.put("categoryid", Utils.getPrefData(CATEGORY_ID, mContext));

        apiInterface.fetchAllNotes(params).enqueue(new Callback<NotesResponse>() {
            @Override
            public void onResponse(Call<NotesResponse> call, Response<NotesResponse> response) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
                try {
                    if (Utils.checkResponseCode(response.code(), mContext) && response.body() != null) {
                        NotesResponse callback = response.body();

                        NotesAdapter adapter = new NotesAdapter(mContext,callback.getData());
                        rvNotes.setAdapter(adapter);

                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<NotesResponse> call, Throwable t) {
                if (pd.isShowing()) {
                    pd.cancel();
                }
            }
        });


    }

}
