package com.icss.learningmanagmentsystem.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.icss.learningmanagmentsystem.R;
import com.icss.learningmanagmentsystem.databinding.FragmentHelpBinding;


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