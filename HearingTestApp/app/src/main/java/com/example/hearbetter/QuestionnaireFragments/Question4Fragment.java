package com.example.hearbetter.QuestionnaireFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.hearbetter.OnDataPass;
import com.example.hearbetter.Profile;
import com.example.hearbetter.R;


public class Question4Fragment extends Fragment implements OnDataPass {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int index;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private String reason;
    RadioGroup rg;
    RadioButton rbOther;

    public Question4Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Question3Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Question4Fragment newInstance(String param1, String param2) {
        Question4Fragment fragment = new Question4Fragment();
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
            index = getArguments().getInt("index");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_question4, container, false);
        rg = view.findViewById(R.id.rbReasons);

        final RadioButton rbMed = view.findViewById(R.id.radioButtonMedical);
        final RadioButton rbPers = view.findViewById(R.id.radioButtonPersonal);
        rbOther = view.findViewById(R.id.radioButtonOther);
        rg.setOnCheckedChangeListener((group, checkedId) -> {
            if(rg.getCheckedRadioButtonId() == rbMed.getId()) {
                reason = "Medical";
            }
            if(rg.getCheckedRadioButtonId() == rbPers.getId()) {
                reason = "Personal";
            }
        });
        return view;
    }

    @Override
    public void onDataPass(String data) {

    }

    @Override
    public void onProfileDataPass() {
        if(rg.getCheckedRadioButtonId() == rbOther.getId()) {
            EditText tbOther = view.findViewById(R.id.tbOther);
            reason = tbOther.getText().toString();
        }
        Profile.AddData("reason", reason);
    }
}
