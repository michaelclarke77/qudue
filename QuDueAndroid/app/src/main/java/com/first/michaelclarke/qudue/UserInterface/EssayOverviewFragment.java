package com.first.michaelclarke.qudue.UserInterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.first.michaelclarke.qudue.R;

/**
 * Created by michaelclarke on 22/08/2016.
 */
public class EssayOverviewFragment extends Fragment {

        public static EssayOverviewFragment newInstance() {
            EssayOverviewFragment fragment = new EssayOverviewFragment();
            return fragment;
        }

        public EssayOverviewFragment(){
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.essay_overview, container, false);

            TextView title = (TextView) rootView.findViewById(R.id.essayTitleField);
            title.setText(TabbedNavigation.selectedEssayTitle);

            TextView count = (TextView) rootView.findViewById(R.id.wordLimit);
            count.setText(TabbedNavigation.selectedWordCount);

            TextView deadline = (TextView) rootView.findViewById(R.id.deadlineDays);
            deadline.setText(TabbedNavigation.selectedDaysToDeadline);

            TextView module = (TextView) rootView.findViewById(R.id.module);
            module.setText(formatModuleText(TabbedNavigation.selectedModuleTitle));

            return rootView;

        }

        private String formatModuleText(String text){

            String[] stringArray = text.split(": ");

            return stringArray[1];
        }
    }


