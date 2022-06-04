package com.sahil.StXaviersSocialClub.utils;

public class StringManipulation {
    public static String expandUsername(String username){
        return username.replace("."," ");
    }
    public static String comdenseUsername(String username){
        return username.replace(" ",".");
    }
}
