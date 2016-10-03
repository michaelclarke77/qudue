package com.first.michaelclarke.qudue.UserInterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.first.michaelclarke.qudue.JavaProcessing.EssayContentData;
import com.first.michaelclarke.qudue.JavaProcessing.EssaySourcesData;
import com.first.michaelclarke.qudue.R;

/**
 * Created by michaelclarke on 22/08/2016.
 */
public class EssaySourcesFragment extends Fragment {

    public static EssaySourcesFragment newInstance() {
        EssaySourcesFragment fragment = new EssaySourcesFragment();
        return fragment;
    }

    public EssaySourcesFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.essay_references, container, false);

        EditText sources = (EditText) rootView.findViewById(R.id.essaySources);

        try {
            if (!EssaySourcesData.essaySources.getEssaySources().equals(null)) {
                sources.setText(EssaySourcesData.essaySources.getEssaySources());
            }
        } catch (NullPointerException ex){
        }

        return rootView;

    }
}


