package com.vhkfoundation.commonutility;

import android.content.Context;
import android.text.format.Time;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckValidation {
    private Context context;
    public CheckValidation(Context context) {
        this.context = context;
    }

    public static boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,7})$";

        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            return true;
        }else {
            return false;
        }
    }

    public static int emptyEditTextError(EditText[] edtTexts, String[] errorMsg) {
        int count = 0;
        for (int i = 0; i < edtTexts.length; i++) {
            edtTexts[i].setError(null);
            if (edtTexts[i].getText().toString().trim().length() == 0) {
                edtTexts[i].setError(errorMsg[i]);
                count++;
            }
        }
        return count;
    }

    public static boolean isPhoneNumberValid(String number) {
        if (number.length() < 10 || number.length() > 11) {
            //	Log.d("tag", "Number is not valid");
            return false;
        }
        return true;
    }


    public static int emptyEditTextError(EditText[] edtTexts, String[] errorMsg, int[] minCount, String[] minError) {
        int count = 0;
        for (int i = 0; i < edtTexts.length; i++) {
            edtTexts[i].setError(null);
            if (edtTexts[i].getText().toString().trim().length() == 0) {
                edtTexts[i].setError(errorMsg[i]);
                count++;
            } else {
                if (minCount[i] != 0) {
                    if (edtTexts[i].getText().length() <= minCount[i]) {
                        if (minError[i].equals("")) {
                            String strError = "Please enter minimum " + minCount[i] + " character";
                            edtTexts[i].setError(strError);
                        } else {
                            edtTexts[i].setError(minError[i]);
                        }
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public static boolean containsOnlyNumbers(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i)))
                return false;
        }
        return true;
    }

    public static boolean urlValidate(String url) {
        String expression = ".*(youtube|youtu.be).*";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        if (matcher.matches()) {
            return true;
        } else if (url.equals("")) {
            return false;
        }
        return false;
    }

    public static String getEditTextValue(EditText text) {
        return text.getText().toString().trim();
    }

    public static boolean isPasswordLengthCorrect(EditText text) {
        if (text.getText() != null && text.getText().toString().trim().length() >= 8) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isPasswordValid(String number) {
        //String regexStr = "^([0-9\\(\\)\\/\\+ \\-]*)$";
        String regexStr = " (?!^[0-9]*$)(?!^[a-zA-Z]*$)^([a-zA-Z0-9]{8,20})$";

        if (number.length() < 6 || number.length() > 13 /*|| number.matches(regexStr) == false*/) {
            //	Log.d("tag", "Number is not valid");
            return false;
        }
        return true;
    }

    public static long currentTimeInMillis() {
        Time time = new Time();
        time.setToNow();
        return time.toMillis(false);
    }

    private String CapitalizeFirstChar(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static boolean validation(EditText[] editTexts) {
        for (EditText editText : editTexts) {
            if (editText.getText().toString().trim().isEmpty()) {
                editText.setError("This field is required");
                editText.requestFocus(); // Focus the first empty field
                return false;
            }
        }
        return true;
    }


    public static String getGenderRadioSelected(RadioButton[] radioButtons) {
        for (RadioButton radioButton : radioButtons) {
            if (radioButton.isChecked()) {
                String text = radioButton.getText().toString().trim();
                switch (text) {
                    case "Male": return "1";
                    case "Female": return "2";
                    case "Other": return "3";
                }
            }
        }
        return "1";
    }    public static String getOccopationRadioSelected(RadioButton[] radioButtons) {
        for (RadioButton radioButton : radioButtons) {
            if (radioButton.isChecked()) {
                String text = radioButton.getText().toString().trim();
                switch (text) {
                    case "Business": return "1";
                    case "Salaried": return "2";
                    case "Self-employment": return "3";
                }
            }
        }
        return "1";
    }

    public static String getPhysicalRadioSelected(RadioButton[] radioButtons) {
        for (RadioButton radioButton : radioButtons) {
            if (radioButton.isChecked()) {
                String text = radioButton.getText().toString().trim();
                switch (text) {
                    case "Yes": return "1";
                    case "No": return "2";
                }
            }
        }
        return "2";
    }

    public static void showToast(String msg,Context context){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    public static String formatIsoDate(String isoDateString) {
        try {
            // Input format with UTC timezone
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            // Output format in desired form
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

            Date date = inputFormat.parse(isoDateString);
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
