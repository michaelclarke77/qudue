package com.first.michaelclarke.qudue.JavaProcessing;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by michaelclarke on 15/08/2016.
 */
public class CustomTextView extends TextView {

    public CustomTextView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Quicksand-Regular.otf"));
    }
}
