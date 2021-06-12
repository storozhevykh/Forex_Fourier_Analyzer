package com.storozhevykh.forexfourieranalyzer.view;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HelpDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HelpDialogFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String text;
    private String parameterName;

    public HelpDialogFragment() {
        // Required empty public constructor
    }

    public HelpDialogFragment(String text, String parameterName) {
        this.text = text;
        this.parameterName = parameterName;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HelpDialogFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HelpDialogFragment newInstance(String param1, String param2) {
        HelpDialogFragment fragment = new HelpDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // set text in dialog programmatically
        TextView headTextView = new TextView(getActivity());
        headTextView.setText(parameterName);
        headTextView.setPadding(16,8,8,8);
        headTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
        headTextView.setTypeface(headTextView.getTypeface(), Typeface.BOLD_ITALIC);

        TextView textView = new TextView(getActivity());
        textView.setText(text);
        textView.setPadding(16,8,8,8);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);

        ScrollView scrollView = new ScrollView(getActivity());

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(headTextView);
        linearLayout.addView(textView);
        scrollView.addView(linearLayout);

        return scrollView;
    }

}