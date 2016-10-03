package com.first.michaelclarke.qudue.UserInterface;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.first.michaelclarke.qudue.DataModels.Note;
import com.first.michaelclarke.qudue.JavaProcessing.AudioPlay;
import com.first.michaelclarke.qudue.R;

import java.util.List;

/**
 * Created by michaelclarke on 23/08/2016.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {


    private List<Note> noteList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView noteTitle, content, date;
        public int noteId;
        public String type, file;
        private Context context;

        public MyViewHolder(View view) {
            super(view);
            context = view.getContext();
            noteTitle = (TextView) view.findViewById(R.id.noteTitle);
            content = (TextView) view.findViewById(R.id.noteContent);
            date = (TextView) view.findViewById(R.id.noteDate);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            Intent intent;

            switch (type) {

                case "TEXT": {

                    intent = new Intent(context, NoteView.class);
                    intent.putExtra("TITLE", noteTitle.getText());
                    intent.putExtra("CONTENT", content.getText());
                    intent.putExtra("ID", noteId);
                    context.startActivity(intent);

                    break;
                }
                case "PHOTO": {

                    intent = new Intent(context, PhotoView.class);
                    intent.putExtra("TITLE", noteTitle.getText());
                    intent.putExtra("FILE", file);
                    intent.putExtra("ID", noteId);
                    context.startActivity(intent);

                    break;
                }
                case "AUDIO": {

                    AudioPlay audioPlay = new AudioPlay();
                    audioPlay.playback(file);


                    break;
                }

            }
        }
    }


    public NoteAdapter(List<Note> list) {

        this.noteList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.type = note.getType();
        holder.noteId = note.getId();
        holder.noteTitle.setText(note.getTitle());
        holder.content.setText(note.getContent());
        holder.date.setText(note.getDate());
        holder.file = note.getFileId();
    }


    @Override
    public int getItemCount() {

        return noteList.size();
    }


}
