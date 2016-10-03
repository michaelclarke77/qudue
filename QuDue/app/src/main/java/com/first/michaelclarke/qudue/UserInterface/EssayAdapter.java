package com.first.michaelclarke.qudue.UserInterface;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.first.michaelclarke.qudue.DataModels.Essay;
import com.first.michaelclarke.qudue.JavaProcessing.DeadlineWarning;
import com.first.michaelclarke.qudue.R;

import java.util.List;

/**
 * Created by michaelclarke on 16/08/2016.
 * Class to create the EssayAdapter for the Home Screen's essay list RecyclerView
 */
public class EssayAdapter extends RecyclerView.Adapter<EssayAdapter.MyViewHolder>{


    private List<Essay> essayList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView essayTitle, wordCount, daysToDeadline, moduleTitle;
        public int essayId;
        public String wordCountAndLimit;
        private Context context;

        public MyViewHolder(View view) {
            super(view);
            context = view.getContext();
            essayTitle = (TextView) view.findViewById(R.id.essayTitle);
            wordCount = (TextView) view.findViewById(R.id.wordCount);
            daysToDeadline = (TextView) view.findViewById(R.id.daysToDeadline);
            moduleTitle = (TextView) view.findViewById(R.id.moduleTitle);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent intent =  new Intent(context, TabbedNavigation.class);
            intent.putExtra("ID", essayId);
            intent.putExtra("TITLE", essayTitle.getText());
            intent.putExtra("LIMIT", wordCountAndLimit);
            intent.putExtra("DATE", daysToDeadline.getText());
            intent.putExtra("MODULE", moduleTitle.getText());
            context.startActivity(intent);
        }
    }


    public EssayAdapter(List<Essay> eList) {

        this.essayList = eList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.essay_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Essay essay = essayList.get(position);
        holder.wordCountAndLimit = essay.getWordCountAndLimit();
        holder.essayId = (essay.getEssayId());
        holder.essayTitle.setText(essay.getEssayTitle());
        holder.moduleTitle.setText("Module: "+essay.getModuleCode());
        holder.wordCount.setText("Word Count: "+ essay.getWordCountAndLimit());
        DeadlineWarning.getWarning(essay.getDueDate());
        holder.daysToDeadline.setText(DeadlineWarning.diffInDays +" days");
    }


    @Override
    public int getItemCount() {
        return essayList.size();
    }

}

