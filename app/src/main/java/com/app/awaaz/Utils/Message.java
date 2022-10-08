package com.app.awaaz.Utils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.app.awaaz.R;

public class Message {
    public static void makeToasts(Context context, String text, boolean isError){
        if(isError){
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = inflater.inflate(R.layout.toast_withouterror_layout,null);
            layout.setBackground(ContextCompat.getDrawable(context,R.drawable.toast_layout));
            TextView textView = layout.findViewById(R.id.toast_without_error);
            textView.setText(text);
            textView.setTextColor(Color.WHITE);
            Toast toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }else{
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = inflater.inflate(R.layout.toast_withouterror_layout,null);
            layout.setBackground(ContextCompat.getDrawable(context,R.drawable.error_toast_layout));
            TextView textView = layout.findViewById(R.id.toast_without_error);
            textView.setText(text);
            Toast toast = new Toast(context);
            textView.setTextColor(Color.WHITE);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();

        }
    }
}
