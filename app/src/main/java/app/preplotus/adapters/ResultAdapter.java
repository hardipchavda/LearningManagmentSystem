package app.preplotus.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import app.preplotus.fragments.CompareReportFragment;

import org.jetbrains.annotations.NotNull;

import app.preplotus.fragments.ScorecardFragment;
import app.preplotus.fragments.SolutionFragment;

public class ResultAdapter extends FragmentPagerAdapter {

//    private String type;
    private int size = 3;

    public ResultAdapter(String type,@NonNull @NotNull FragmentManager fm, int behavior) {
        super(fm, behavior);
//        this.type = type;
        if (type.equals("practice")){
            size = 2;
        }
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        switch (position){
            case 0: fragment=new ScorecardFragment();break;
            case 1: fragment=new SolutionFragment();break;
            case 2: fragment=new CompareReportFragment();break;
        }
        return fragment;

    }

    @Override
    public int getCount() {
        return size;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        String tabtitle="";
        if(position==0)
            tabtitle="Scorecard";
        else  if(position==1)
            tabtitle="SOLUTION";
        else  if(position==2)
            tabtitle="COMPARE";

        return tabtitle;
    }

}
