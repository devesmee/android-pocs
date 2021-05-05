package com.example.hearbetter.TestFragments;

import android.media.audiofx.Equalizer;
import com.example.hearbetter.setFrequencyVol;

public class HeadphoneEQSettings{

    public static void CreateSettings(int index, setFrequencyVol SF){
        if(index == 0){
            SF.SetFrequencyVol((short)2, (short)-45);
            SF.SetFrequencyVol((short)3, (short)260);
            SF.SetFrequencyVol((short)4, (short)-140);
            SF.SetFrequencyVol((short)5, (short)-180);
            SF.SetFrequencyVol((short)6, (short)-450);
        }
        if(index == 1){
            SF.SetFrequencyVol((short)0, (short)0);
            SF.SetFrequencyVol((short)1, (short)0);
            SF.SetFrequencyVol((short)2, (short)0);
            SF.SetFrequencyVol((short)3, (short)0);
            SF.SetFrequencyVol((short)4, (short)0);
        }
    }
}
