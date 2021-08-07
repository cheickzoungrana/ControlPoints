package com.example.wzoungrana.controlpoints.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.wzoungrana.controlpoints.LoginActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.ECField;

/**
 * @author CHEICK ZOUNGRANA on 05/08/2021.
 */

public class Session {

    public static String last_connected;
    public static boolean isConnected = false;
    // Context
    static Context _context;

    // Shared pref mode
    private static final int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "controlPoint";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // Sharedpref file name
    private static final String KEY_LASTNAME = "Nom";
    private static final String KEY_FIRSTNAME = "Prenom";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_IDENTIFIANT = "id";


    static SharedPreferences pref;
    static SharedPreferences pref2;

    // Editor for Shared preferences
    public static SharedPreferences.Editor editor;
    public static SharedPreferences.Editor editor2;

    public Session(Context context) {
    }

    public static void init(Context context) {
        _context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        pref2 = _context.getSharedPreferences("user_sync", PRIVATE_MODE);
        editor = pref.edit();
        editor2 = pref2.edit();
    }

    public static boolean deconnexion() {

        try {
            Session.last_connected = pref.getString(KEY_LOGIN, null);
        } catch (Exception e){}
        isConnected = false;
        try {
            editor.clear();
            editor.commit();
        } catch (Exception e){}

        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
        try {
            ((Activity) _context).finish();
        } catch (Exception e) {
        }

        return true;
    }

    public static boolean checkLogin() {

        if (!isLoggedIn()) {

            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
            return false;
        }

        return true;
    }

    public static String getId() {
        return pref.getString(KEY_IDENTIFIANT, null);
    }

    public static boolean isLoggedIn() {
        if (pref == null)
            return false;
        return pref.getBoolean(IS_LOGIN, false);
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static final String Encrypt(final String s) {
        return md5(s);
    }

    public static final Boolean check(final String nonEncrypted, String encrypted) {
        return md5(nonEncrypted).equals(encrypted);
    }

    public static String getLogin(){
        return pref.getString(KEY_LOGIN, "");
    }

    public static String getPrenom(){
        return pref.getString(KEY_FIRSTNAME, "");
    }

    public static String getNom(){
        return pref.getString(KEY_LASTNAME, "");
    }

}