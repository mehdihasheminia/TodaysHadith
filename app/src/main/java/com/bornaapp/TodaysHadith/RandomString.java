package com.bornaapp.TodaysHadith;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomString {

    private static RandomString instance = null;

    private RandomString(){
        instance = this;
    }

    public static void init(){
        //private instantiation
        new RandomString();

        //Retrieve strings from resources
       // String[] resources = context.getResources().getStringArray(R.array.My_String_Array);

        //Initialize a random sequence of strings
        instance.seqOfNumbers = new ArrayList<>();
        for (int i = 0; i < resources.length; i++) {
            instance.seqOfNumbers.add(i);
        }
        Collections.shuffle(instance.seqOfNumbers);
    }

    public List<Integer> seqOfNumbers;
    private int iteratorIndex;

    public boolean IsInitialized() {
        try {
            return !seqOfNumbers.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private String GetStringResource(int index) {
        //Retrieve strings from resources
        String[] resources = context.getResources().getStringArray(R.array.My_String_Array);
        //return string at specified index
        return resources[index];
    }

    public String Current() {
        if (!IsInitialized())
            return "";

        return GetStringResource(seqOfNumbers.get(iteratorIndex));
    }

    public String Next() {
        if (!IsInitialized())
            return "";

        if (iteratorIndex < seqOfNumbers.size() - 1)
            iteratorIndex++;
        else
            iteratorIndex = 0;

        return GetStringResource(seqOfNumbers.get(iteratorIndex));
    }

    public String Previous() {
        if (!IsInitialized())
            return "";

        if (iteratorIndex > 0)
            iteratorIndex--;
        else
            iteratorIndex = seqOfNumbers.size() - 1;

        return GetStringResource(seqOfNumbers.get(iteratorIndex));
    }
}