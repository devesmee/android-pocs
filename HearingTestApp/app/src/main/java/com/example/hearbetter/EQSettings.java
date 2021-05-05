package com.example.hearbetter;

import android.media.audiofx.Equalizer;

public class EQSettings {
    float leftVol;
    float rightVol;
    float[] values;

    public Equalizer CreateSettings(Equalizer eq, float[] values){
        this.values = values;
        leftVol = values[0];
        rightVol = values[1];
        if(eq.getBandLevel((short)0) + values[2] > 1499)
            eq.setBandLevel((short)0, (short)1500);
        else if(eq.getBandLevel((short)0) + values[2] < -1500)
            eq.setBandLevel((short)0, (short)-1500);
        else
            eq.setBandLevel((short)0, (short)(eq.getBandLevel((short)0) + values[2]));

        if(eq.getBandLevel((short)1) + values[3] > 1499)
            eq.setBandLevel((short)1, (short)1500);
        else if(eq.getBandLevel((short)1) + values[3] < -1500)
            eq.setBandLevel((short)1, (short)-1500);
        else
            eq.setBandLevel((short)1, (short)(eq.getBandLevel((short)1) + values[3]));

        if(eq.getBandLevel((short)2) + values[4] > 1499)
            eq.setBandLevel((short)2, (short)1500);
        else if(eq.getBandLevel((short)2) + values[4] < -1500)
            eq.setBandLevel((short)2, (short)-1500);
        else
            eq.setBandLevel((short)2, (short)(eq.getBandLevel((short)2) + values[4]));

        if(eq.getBandLevel((short)3) + values[5] > 1499)
            eq.setBandLevel((short)3, (short)1500);
        else if(eq.getBandLevel((short)3) + values[5] < -1500)
            eq.setBandLevel((short)3, (short)-1500);
        else
            eq.setBandLevel((short)3, (short)(eq.getBandLevel((short)3) + values[5]));

        if(eq.getBandLevel((short)4) + values[6] > 1499)
            eq.setBandLevel((short)4, (short)1500);
        else if(eq.getBandLevel((short)4) + values[6] < -1500)
            eq.setBandLevel((short)4, (short)-1500);
        else
            eq.setBandLevel((short)4, (short)(eq.getBandLevel((short)4) + values[6]));
        return eq;
    }

    public String ToString(){
        String string = "";
        for(int i = 0; i < values.length; i++){
            string += values[i] + ",";
        }
        return string;
    }
}
