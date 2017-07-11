package com.ihs.android.contracttracing.contracttracing.views.fragments;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ihs.android.contracttracing.views.activities.R;
import com.ihs.android.contracttracing.views.activities.databinding.FragmentProgressLoaderBinding;


/**
 * Created by Moiz-IHS on 5/30/2017.
 */

public class LoaderFragment extends Fragment {
    private String loadingText;
    private FragmentProgressLoaderBinding binding;

    public LoaderFragment() {
    }

    @SuppressLint("ValidFragment")
    public LoaderFragment(String text) {
        loadingText = text;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_progress_loader, container, false);
        View view = binding.getRoot();
        binding.loaderText.setText(loadingText);
        return view;
    }

}