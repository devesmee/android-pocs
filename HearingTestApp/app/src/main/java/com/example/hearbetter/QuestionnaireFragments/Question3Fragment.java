package com.example.hearbetter.QuestionnaireFragments;

import android.os.Bundle;
import android.provider.MediaStore;
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


public class Question3Fragment extends Fragment implements OnDataPass {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int index;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String tinitus;

    private View view;


    public Question3Fragment() {
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
    public static Question3Fragment newInstance(String param1, String param2) {
        Question3Fragment fragment = new Question3Fragment();
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
        view = inflater.inflate(R.layout.fragment_question3, container, false);
        RadioGroup rg = view.findViewById(R.id.rbtinitus);
        RadioButton rgYes = view.findViewById(R.id.radioButtonYes);
        RadioButton rgNo = view.findViewById(R.id.radioButtonNo);
        RadioButton rgSometimes = view.findViewById(R.id.radioButtonSometimes);
        rg.setOnCheckedChangeListener((group, checkedId) -> {
            if(rg.getCheckedRadioButtonId() == rgYes.getId()) {
                tinitus = "Yes";
            }
            if(rg.getCheckedRadioButtonId() == rgNo.getId()) {
                tinitus = "No";
            }
            if(rg.getCheckedRadioButtonId() == rgSometimes.getId()) {
                tinitus = "Sometimes";
            }
        });
        return view;
    }

    @Override
    public void onDataPass(String data) {

    }

    @Override
    public void onProfileDataPass() {
        Profile.AddData("tinnitus", tinitus);
    }
}
