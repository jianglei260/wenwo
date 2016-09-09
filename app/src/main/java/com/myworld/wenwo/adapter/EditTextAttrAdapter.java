package com.myworld.wenwo.adapter;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.databinding.Observable;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by jianglei on 16/7/20.
 */

public class EditTextAttrAdapter {
    @BindingAdapter("checkMaxValue")
    public static void setCheckMaxValue(final EditText editText, final float value) {
        editText.getText();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
                if (TextUtils.isEmpty(s.toString()))
                    return;
                try {
                    float inputValue = Float.valueOf(editText.getText().toString());
                    if (inputValue > value)
                        editText.setError("价格不能超过" + value + "哦!");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @BindingAdapter("editable")
    public static void setEditable(EditText editText, Editable editable) {
        switch (editable) {
            case YES:
                editText.setFocusable(true);
                editText.setEnabled(true);
                break;
            case NO:
                editText.setEnabled(false);
                editText.setFocusable(false);
                break;
        }
    }

    @BindingConversion
    public static String convertObserbleString(ObservableField<String> observableField) {
        return observableField.get();
    }

    @BindingAdapter("bindEditText")
    public static void bindEditText(final EditText editText, final ObservableField<String> value) {
        if (!value.get().equals(editText.getText().toString()))
            editText.setText(value.get());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
                value.set(s.toString());
            }
        });
        value.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                editText.setText(value.get());
            }
        });
    }

}
