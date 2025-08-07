package com.vhkfoundation.commonutility;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceConnector {
    public static final String PREF_NAME = "app_prefrences";
    public static final int MODE = Context.MODE_PRIVATE;
    public static final String FCMID = "fcmid";
    public static final String ISNOTIFICATION = "isnotification";
    public static final String LOGINUSERETYPE = "loginuseretype";
    public static final String USERID = "userID";
    public static final String TOKEN = "TOKEN";
    public static final String USERCODE = "user_code";
    public static final String USERNAME = "name";
    public static final String USEREMAIL = "email";
    public static final String USEREMOBILE = "mobile";
    public static final String USERPERSONADATA = "USERPERSONADATA";
    public static final String STATELIST = "STATELIST";
    public static final String DISTRICTS = "DISTRICTS";
    public static final String AUTOLOGIN = "autologin";

    public static final String ISVOLUNTEER = "is_volunteer";

    public static final String ISSHOWPROFILE = "is_profile_page";

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static void cleanPrefrences(Context context) {
        getPreferences(context).edit().clear().apply();
    }

    public static void writeBoolean(Context context, String key, boolean value) {
        getEditor(context).putBoolean(encrypt(key), value).commit();
    }

    public static boolean readBoolean(Context context, String key, boolean defValue) {
        return getPreferences(context).getBoolean(encrypt(key), defValue);
    }

    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(encrypt(key), value).commit();
    }

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(encrypt(key), defValue);
    }

    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(encrypt(key), value).commit();
    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(decrypt(key), defValue);
    }

    public static void writeFloat(Context context, String key, float value) {
        getEditor(context).putFloat(decrypt(key), value).commit();
    }

    public static float readFloat(Context context, String key, float defValue) {
        return getPreferences(context).getFloat(decrypt(key), defValue);
    }

    public static void writeLong(Context context, String key, long value) {
        getEditor(context).putLong(decrypt(key), value).commit();
    }

    public static long readLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(decrypt(key), defValue);
    }

    public static String encrypt(String plaintext) {
//        byte[] encryptedBytes = new byte[0];
//        try {
//            byte[] iv = new byte[12];
//            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
//            keyStore.load(null);
//            SecretKey key = (SecretKey) keyStore.getKey(KEY_ALIAS, null);
//
//            if (key == null) {
//                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE);
//                keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_ALIAS,
//                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
//                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
//                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
//                        .setRandomizedEncryptionRequired(false)
//                        .build());
//                keyGenerator.generateKey();
//                key = (SecretKey) keyStore.getKey(KEY_ALIAS, null);
//
//            }
//
//            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
//            cipher.init(Cipher.ENCRYPT_MODE, key);
//            encryptedBytes = cipher.doFinal(plaintext.getBytes());
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new IllegalStateException("Keystore save error");
//        }
//        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
        return plaintext;
    }

    public static String decrypt(String encryptedText) {
//        byte[] decryptedBytes = new byte[0];
//        try {
//            byte[] iv = new byte[12];
//            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
//            keyStore.load(null);
//            SecretKey key = (SecretKey) keyStore.getKey(KEY_ALIAS, null);
//
//            if (key == null) {
//                throw new IllegalStateException("Key not found in keystore");
//            }
//
//            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
//            cipher.init(Cipher.DECRYPT_MODE, key);
//            byte[] decodedBytes = Base64.decode(encryptedText, Base64.DEFAULT);
//            decryptedBytes = cipher.doFinal(decodedBytes);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new IllegalStateException("Keystore save error");
//        }
//        return new String(decryptedBytes);
        return encryptedText;
    }
}
