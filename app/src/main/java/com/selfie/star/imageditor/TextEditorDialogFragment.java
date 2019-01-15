package com.selfie.star.imageditor;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.selfie.star.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by Burhanuddin Rashid on 1/16/2018.
 */

public class TextEditorDialogFragment extends DialogFragment {

    public static final String TAG = TextEditorDialogFragment.class.getSimpleName();
    public static final String EXTRA_INPUT_TEXT = "extra_input_text";
    public static final String EXTRA_FONT_PATH = "extra_font_path";
    public static final String EXTRA_COLOR_CODE = "extra_color_code";
    private EditText mAddTextEditText;
    private Spinner spinner;
    private TextView mAddTextDoneTextView;
    private InputMethodManager mInputMethodManager;
    private int mColorCode;
    private String fontPath;
    private TextEditor mTextEditor;

    //Show dialog with provide text and text color
    public static TextEditorDialogFragment show(@NonNull AppCompatActivity appCompatActivity,
                                                @NonNull String fontPath,
                                                @NonNull String inputText,
                                                @ColorInt int colorCode) {
        Bundle args = new Bundle();
        args.putString(EXTRA_INPUT_TEXT, inputText);
        args.putString(EXTRA_FONT_PATH, fontPath);
        args.putInt(EXTRA_COLOR_CODE, colorCode);
        TextEditorDialogFragment fragment = new TextEditorDialogFragment();
        fragment.setArguments(args);
        fragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return fragment;
    }

    //Show dialog with default text input as empty and text color white
    public static TextEditorDialogFragment show(@NonNull AppCompatActivity appCompatActivity) {
        return show(appCompatActivity,
                "","", ContextCompat.getColor(appCompatActivity, R.color.colorWhite));
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        //Make dialog full screen with transparent background
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_text_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAddTextEditText = view.findViewById(R.id.add_text_edit_text);
        spinner = view.findViewById(R.id.spinner);
        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mAddTextDoneTextView = view.findViewById(R.id.add_text_done_tv);




        //Setup the color picker for text color
        RecyclerView addTextColorPickerRecyclerView = view.findViewById(R.id.add_text_color_picker_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        addTextColorPickerRecyclerView.setLayoutManager(layoutManager);
        addTextColorPickerRecyclerView.setHasFixedSize(true);
        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(getActivity());
        //This listener will change the text color when clicked on any color from picker
        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
                mColorCode = colorCode;
                mAddTextEditText.setTextColor(colorCode);
            }
        });
        addTextColorPickerRecyclerView.setAdapter(colorPickerAdapter);
        fontPath=getArguments().getString(EXTRA_FONT_PATH);
        mAddTextEditText.setText(getArguments().getString(EXTRA_INPUT_TEXT));
        mColorCode = getArguments().getInt(EXTRA_COLOR_CODE);
        mAddTextEditText.setTextColor(mColorCode);

       // mAddTextEditText.setTypeface(Typeface.createFromAsset(getContext().getAssets(),fontPath));
        mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        setFontSpinner();


        //Make a callback on activity when user is done with text editing
        mAddTextDoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                dismiss();
                String inputText = mAddTextEditText.getText().toString();
                if (!TextUtils.isEmpty(inputText) && mTextEditor != null) {
                    mTextEditor.onDone(inputText, mColorCode,fontPath);
                }
            }
        });

    }

    private void setFontSpinner() {
        List<String> fontList = new ArrayList<>();
        fontList.add("fonts/blacklarch.ttf");
        fontList.add("fonts/blessd.ttf");
        fontList.add("fonts/fancy.ttf");
        fontList.add("fonts/fon.ttf");
        fontList.add("fonts/homestile.ttf");
        fontList.add("fonts/personaluse.ttf");
        fontList.add("fonts/smartwatch.ttf");
        MySpinnerAdapter adapter = new MySpinnerAdapter(getContext(), R.layout.view_spinner_item, fontList);
        spinner.setAdapter(adapter);
        if(!TextUtils.isEmpty(fontPath)) {
            spinner.setSelection(fontList.indexOf(fontPath));
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fontPath=fontList.get(position);
                mAddTextEditText.setTypeface(Typeface.createFromAsset(getContext().getAssets(),fontPath));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Callback to listener if user is done with text editing
    public void setOnTextEditorListener(TextEditor textEditor) {
        mTextEditor = textEditor;
    }


    public interface TextEditor {
        void onDone(String inputText, int colorCode,String fontPath);
    }

    private static class MySpinnerAdapter extends ArrayAdapter<String> {
        // Initialise custom font, for example:

        // (In reality I used a manager which caches the Typeface objects)
        // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);
        List<String> items;

        private MySpinnerAdapter(Context context, int resource, List<String> items) {
            super(context, resource, items);
            this.items=items;
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            String itemValue=items.get(position);
            view.setText(getFontName(itemValue));
            view.setTypeface(Typeface.createFromAsset(getContext().getAssets(),itemValue));
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            String itemValue=items.get(position);
            view.setText(getFontName(itemValue));
            view.setTypeface(Typeface.createFromAsset(getContext().getAssets(),itemValue));
            return view;
        }

        private String getFontName(String fontPath){
            String split[]=fontPath.split("/");
            String value=split[1];
            return value.substring(0, value.lastIndexOf("."));
        }
    }



}
