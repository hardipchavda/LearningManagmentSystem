package app.preplotus.adapters;

import static app.preplotus.utilities.Constants.CATEGORY_ID;
import static app.preplotus.utilities.Constants.CATEGORY_NAME;
import static app.preplotus.utilities.Constants.CAT_TOPIC_NAME;
import static app.preplotus.utilities.Constants.SUBSCRIBED;
import static app.preplotus.utilities.Constants.SUPER_GROUP_ID;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;
import app.preplotus.R;

import app.preplotus.activities.MainActivity;
import app.preplotus.model.CategoryData;
import app.preplotus.utilities.CategoryDialog;
import app.preplotus.utilities.Utils;

import java.util.ArrayList;

public class MyExamsAdapter extends RecyclerView.Adapter<MyExamsAdapter.viewHolder> {
    Context mContext;
    ArrayList<CategoryData> list;
    Boolean isEdit = false;
    CategoryDialog categoryDialog;

    public MyExamsAdapter(Context context, ArrayList<CategoryData> list, Boolean isEdit, CategoryDialog categoryDialog) {
        this.mContext = context;
        this.list = list;
        this.isEdit = isEdit;
        this.categoryDialog = categoryDialog;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_my_exams, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int ppp) {
        final int position = holder.getAdapterPosition();
        final CategoryData data = list.get(position);

        holder.tvTitle.setText(data.getCatName());
        holder.imgAction.setVisibility(View.VISIBLE);
        if (Utils.getPrefData(CATEGORY_ID, mContext).equals(data.getCatId())) {
            holder.imgAction.setImageResource(R.drawable.check_c);
        } else if (isEdit) {
            holder.imgAction.setImageResource(R.drawable.cross);
            holder.imgAction.setColorFilter(ContextCompat.getColor(mContext, R.color.black), android.graphics.PorterDuff.Mode.SRC_IN);
        } else {
            holder.imgAction.setVisibility(View.GONE);
        }

        Glide.with(mContext).load(data.getCatImage()).into(holder.image);

        holder.imgAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryDialog.removePreference(position, data);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEdit) {
                    Utils.setPrefData(CATEGORY_NAME, data.getCatName(), mContext);
                    Utils.setPrefData(CATEGORY_ID, data.getCatId(), mContext);
                    Utils.setPrefData(SUPER_GROUP_ID, data.getSupergroup_Id(), mContext);
                    FirebaseMessaging.getInstance().subscribeToTopic(data.getFirebase_topic_name());
                    Utils.insertTopic(mContext,data.getFirebase_topic_name());
                    if (Utils.getPrefData(CAT_TOPIC_NAME, mContext).trim().length() != 0) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(Utils.getPrefData(CAT_TOPIC_NAME, mContext));
                        Utils.removeTopic(mContext,Utils.getPrefData(CAT_TOPIC_NAME, mContext));
                    }
                    Utils.setPrefData(CAT_TOPIC_NAME, data.getFirebase_topic_name(), mContext);
                    Utils.setPrefData("isDetailFetched", "", mContext);
                    Utils.setPrefData(SUBSCRIBED, "", mContext);
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView image, imgAction;
        AppCompatTextView tvTitle;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            imgAction = itemView.findViewById(R.id.imgAction);

        }
    }
}
