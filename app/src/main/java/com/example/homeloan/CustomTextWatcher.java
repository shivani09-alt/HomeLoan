package com.example.homeloan;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputEditText;

 class CustomTextWatcher implements TextWatcher {
    private final TextInputEditText mEditText;
    private final Context context;
    AutoCompleteTextView autoCompleteTextView;
    public CustomTextWatcher(AutoCompleteTextView autoCompleteTextView, TextInputEditText e, Context context) {
        mEditText = e;
        this.autoCompleteTextView=autoCompleteTextView;
        this.context=context;
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(autoCompleteTextView!=null){
            autoCompleteTextView.setBackground(context.getDrawable(R.drawable.backfround_shape));

        }
        else {
            mEditText.setBackground(context.getDrawable(R.drawable.backfround_shape));
        }
    }

    public void afterTextChanged(Editable s) {
    }
}
