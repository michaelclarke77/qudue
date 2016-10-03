package com.first.michaelclarke.qudue.UserInterface;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.first.michaelclarke.qudue.R;

/**
 * Created by michaelclarke on 22/08/2016.
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    Context context = MainActivity.context;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        // getItem is called to instantiate the fragment for the given page.
        switch (position) {
            case 0:
                //at position 0 (first page) return the tab
                return EssayOverviewFragment.newInstance();
            case 1:
                //at position 1 (second page) return the tab
                return EssayNotesFragment.newInstance();
            case 2:
                //at position 2 (third page) return the tab
                return EssayPreviewFragment.newInstance();
            case 3:
                //at position 4 (fifth page) return the tab
                return EssaySourcesFragment.newInstance();
            default:
                // It is better to use default so that it always returns a fragment and no problems would ever occur
                return EssayOverviewFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        // Show total pages.
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

            case 0:
                //at position 0 (first page) return the tab's title
                return context.getString(R.string.essay_overview_tab_title);
            case 1:
                //at position 1 (second page) return the tab's title
                return context.getString(R.string.notes_tab_title);
            case 2:
                //at position 2 (third page) return the tab's title
                return context.getString(R.string.essay_preview_tab_title);
            case 3:
                //at position 4 (fourth page) return the tab's title
                return context.getString(R.string.source_title);
            default:
                return null;

        }
    }
}
