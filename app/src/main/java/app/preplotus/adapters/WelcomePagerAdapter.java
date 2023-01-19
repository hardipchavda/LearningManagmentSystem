package app.preplotus.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewpager.widget.PagerAdapter;

import app.preplotus.R;

import java.util.ArrayList;
import java.util.HashMap;

public class WelcomePagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private ArrayList<HashMap<String,String>> listData;

    public WelcomePagerAdapter(Context context, ArrayList<HashMap<String,String>> listData) {
        this.mContext = context;
        this.layoutInflater = (LayoutInflater) this.mContext.getSystemService(this.mContext.LAYOUT_INFLATER_SERVICE);
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = this.layoutInflater.inflate(R.layout.custom_row_pager, container, false);

        HashMap<String,String> map = listData.get(position);

        AppCompatTextView tvHeader = view.findViewById(R.id.tvHeader);
        ImageView img = view.findViewById(R.id.img);

        tvHeader.setText(map.get("title"));
        int id = mContext.getResources().getIdentifier(map.get("image"), "drawable", mContext.getPackageName());
        img.setImageResource(id);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}
