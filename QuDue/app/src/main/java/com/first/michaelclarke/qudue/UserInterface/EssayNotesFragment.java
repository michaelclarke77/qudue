package com.first.michaelclarke.qudue.UserInterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.first.michaelclarke.qudue.DataModels.Note;
import com.first.michaelclarke.qudue.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelclarke on 22/08/2016.
 */
public class EssayNotesFragment extends Fragment {

    public static RecyclerView notesRecyclerView;
    public static NoteAdapter noteAdapter;


    public static EssayNotesFragment newInstance() {
        EssayNotesFragment fragment = new EssayNotesFragment();
        return fragment;
    }

    public EssayNotesFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            notesRecyclerView = (RecyclerView) inflater.inflate(
                    R.layout.essay_notes, container, false);
            setupRecyclerView();


        return notesRecyclerView;

    }

    private void setupRecyclerView() {
        // prepareOfferData();//This is just an example - should draw down most recent data from cloud
       // noteAdapter = new NoteAdapter(noteList);
      //  noteAdapter.notifyDataSetChanged();
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        notesRecyclerView.setHasFixedSize(true);
        notesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        notesRecyclerView.setAdapter(noteAdapter);

    }


}


