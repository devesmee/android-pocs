package com.example.hearbetter.TestFragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hearbetter.EQSettings;
import com.example.hearbetter.R;
import com.example.hearbetter.setFrequencyVol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BassTestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BassTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BassTestFragment extends Fragment implements setFrequencyVol {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private short index;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View view;

    setFrequencyVol SF;

    private MediaPlayer mp;

    float leftVolume = 0.5f;
    float rightVolume = 0.5f;
    float volValue = 0.05f;

    float[] bands;


    private boolean playing = false;
    private float[] values;
    private Equalizer eq;
    private boolean mpAvail;

    public BassTestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BassTestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BassTestFragment newInstance(String param1, String param2) {
        BassTestFragment fragment = new BassTestFragment();
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
            index = (short)getArguments().getInt("index");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SF = (setFrequencyVol) getActivity();

        bands = new float[5];
        for(int i = 0; i < 5; i++){
            bands[i] = 0.5f;
        }

        if(index == 0)
            SetFrequencyVol((short)index, leftVolume);
        if(index == 1)
            SetFrequencyVol((short)index, rightVolume);

        values = new float[7];
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bass_test, container, false);

        ChangeImage(index);


        setupMediaPlayer(index ,view);
        final Button btnPlay = view.findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(v -> {
            if(!playing){
                setupMediaPlayer(index ,view);
                setupEq();
                mp.setOnCompletionListener(mp -> {
                    btnPlay.setBackground(view.getResources().getDrawable(R.drawable.play, null));
                    playing = false;
                });
                mp.start();
                mpAvail = true;
                btnPlay.setBackground(view.getResources().getDrawable(R.drawable.stop, null));
                playing = true;
            }
            else{
                mpAvail = false;
                mp.stop();
                mp.release();
                btnPlay.setBackground(view.getResources().getDrawable(R.drawable.play, null));
                playing = false;
            }
        });
        final Button btnPlus = view.findViewById(R.id.btnPlus);
        btnPlus.setOnClickListener(v -> IncreaseVolume(index));
        final Button btnMinus = view.findViewById(R.id.btnMinus);
        btnMinus.setOnClickListener(v -> DecreaseVolume(index));

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mpAvail = false;
                btnPlay.setBackground(getResources().getDrawable(R.drawable.stop, null));
            }
        });

        return view;
    }

    private void IncreaseVolume(int index) {
        switch (index){
            case 0:
                if(mpAvail){
                    leftVolume += volValue;
                    mp.setVolume(leftVolume, 0);
                    SetFrequencyVol((short)index, leftVolume);
                }
                break;
            case 1:
                if(mpAvail) {
                    rightVolume += volValue;
                    mp.setVolume(0, rightVolume);
                    SetFrequencyVol((short) index, rightVolume);
                }
                break;
        }
        if(index >= 2){
            if(mpAvail) {
                bands[index - 2] += volValue;
                mp.setVolume(bands[index - 2], bands[index - 2]);
                SetFrequencyVol((short) index, (bands[index - 2]) * 2500);
            }
        }
    }

    private void DecreaseVolume(int index) {
        switch (index){
            case 0:
                if(mpAvail) {
                    leftVolume -= volValue;
                    mp.setVolume(leftVolume, 0);
                    SetFrequencyVol((short) index, leftVolume);
                }
                break;
            case 1:
                if(mpAvail) {
                    rightVolume -= volValue;
                    mp.setVolume(0, rightVolume);
                    SetFrequencyVol((short) index, rightVolume);
                }
                break;
        }
        if(index >= 2){
            if(mpAvail){
                bands[index - 2] -= volValue;
                mp.setVolume(bands[index - 2], bands[index - 2]);
                SetFrequencyVol((short)index, (bands[index-2])*2500);
            }
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void setupMediaPlayer(int Index, View v){
        switch(Index){
            //Left ear test
            case 0:
                mp = MediaPlayer.create(v.getContext(), R.raw.mid);
                mp.setVolume(leftVolume, 0);
                break;
            //Right ear test
            case 1:
                mp = MediaPlayer.create(v.getContext(), R.raw.mid);
                mp.setVolume(0, rightVolume);
                break;
            //Low Bass Test
            case 2:
                mp = MediaPlayer.create(v.getContext(), R.raw.lowbass);
                mp.setVolume(bands[index - 2] + (leftVolume - bands[index - 2]),
                        bands[index - 2] + (rightVolume - bands[index - 2]));
                break;
            //High Bass Test
            case 3:
                mp = MediaPlayer.create(v.getContext(), R.raw.highbass);
                mp.setVolume(bands[index - 2] + (leftVolume - bands[index - 2]),
                        bands[index - 2] + (rightVolume - bands[index - 2]));
                break;
            //mid Test
            case 4:
                mp = MediaPlayer.create(v.getContext(), R.raw.mid);
                mp.setVolume(bands[index - 2] + (leftVolume - bands[index - 2]),
                        bands[index - 2] + (rightVolume - bands[index - 2]));
                break;
            //Low Treble Test
            case 5:
                mp = MediaPlayer.create(v.getContext(), R.raw.lowhigh);
                mp.setVolume(bands[index - 2] + (leftVolume - bands[index - 2]),
                        bands[index - 2] + (rightVolume - bands[index - 2]));
                break;
            //High Treble Test
            case 6:
                mp = MediaPlayer.create(v.getContext(), R.raw.highs);
                mp.setVolume(bands[index - 2] + (leftVolume - bands[index - 2]),
                        bands[index - 2] + (rightVolume - bands[index - 2]));
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
        }catch(ClassCastException e){
            throw new ClassCastException("Error in retrieving data");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void SetFrequencyVol(short freqNum, float value) {
        SF.SetFrequencyVol(freqNum, value);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setupEq(){
        try {
            File root = android.os.Environment.getExternalStorageDirectory();
            FileInputStream fileInputStream;
            fileInputStream = new FileInputStream(new File(root.getAbsolutePath() + "/HearBetter/EqSetting.txt"));
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lineData = bufferedReader.readLine();
            String[] separated = lineData.split(",");
            for(int i = 0; i < 7; i++){
                values[i] = Float.parseFloat(separated[i]);
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(getActivity().getApplicationContext(), "File not found!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getActivity().getApplicationContext(), "File not found!", Toast.LENGTH_SHORT).show();
        }
        if(eq == null){
            eq = new Equalizer(-1, mp.getAudioSessionId());
        }
        EQSettings Settings = new EQSettings();
        eq = Settings.CreateSettings(eq, values);
        eq.setEnabled(true);
    }

    private void ChangeImage(int Index){
        switch(Index){
            //Left ear test
            case 0:
                ImageView image = view.findViewById(R.id.imageTest);
                image.setImageResource(R.drawable.left_ear);
                break;
            //Right ear test
            case 1:
                ImageView image2 = view.findViewById(R.id.imageTest);
                image2.setImageResource(R.drawable.right_ear);
                break;
        }
    }

}
