package app.preplotus.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import app.preplotus.R;
import app.preplotus.databinding.FragmentHelpBinding;


public class HelpFragment extends Fragment {

    FragmentHelpBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_help,container,false);

        binding.textHome.setText("NDJndajf");
        return binding.getRoot();
    }
}