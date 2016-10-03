package com.first.michaelclarke.qudue.UserInterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.first.michaelclarke.qudue.JavaProcessing.EssayContentData;
import com.first.michaelclarke.qudue.R;

/**
 * Created by michaelclarke on 22/08/2016.
 */
public class EssayPreviewFragment extends Fragment {

    public static EssayPreviewFragment newInstance() {
        EssayPreviewFragment fragment = new EssayPreviewFragment();
        return fragment;
    }

    public EssayPreviewFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.essay_preview, container, false);

        EditText preview = (EditText) rootView.findViewById(R.id.essayPreview);

        try {
            if (!EssayContentData.essayContent.getEssayContent().equals(null)) {
                preview.setText(EssayContentData.essayContent.getEssayContent());
            }
        } catch (NullPointerException ex){
        }

        return rootView;

    }
}
