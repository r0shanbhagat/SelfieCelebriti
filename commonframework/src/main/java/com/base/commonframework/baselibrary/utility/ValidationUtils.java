package com.base.commonframework.baselibrary.utility;

import android.content.Context;
import android.text.TextUtils;

import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;

/**
 * Created by Gautam Sharma on 23-12-2015.
 */
public final class ValidationUtils {

    private ValidationUtils() {
    }

    /**
     * Is email valid boolean.
     *
     * @param value the value
     * @return boolean boolean
     */
    public static boolean isEmailValid(@NonNull String value) {
        return !TextUtils.isEmpty(value) && android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches();
    }


    /**
     * Is only number boolean.
     *
     * @param value the value
     * @return boolean boolean
     */
    public static boolean isOnlyNumber(@NonNull String value) {
        if (TextUtils.isEmpty(value)) {
            return false;
        }

        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Is only string boolean.
     *
     * @param value the value
     * @return boolean boolean
     */
    public static boolean isOnlyString(@NonNull String value) {
        if (TextUtils.isEmpty(value)) {
            return false;
        }

        for (int i = 0; i < value.length(); i++) {
            if (Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /* Returns true if url is valid */
    public static boolean isValidUrl(String url) {
        /* Try creating a valid URL */
        try {
            new URL(url).toURI();
            return true;
        }
        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }

    /**
     * Is mobile number valid boolean.
     *
     * @param mobileNum the mobile num
     * @return boolean boolean
     */
    public static boolean isMobileNumberValid(@NonNull String mobileNum) {
        if (TextUtils.isEmpty(mobileNum) && mobileNum.length() == 10) {
            return false;
        }
        String expression = "^[9876]\\d{9}$";
        Pattern p = Pattern.compile(expression);
        Matcher m = p.matcher(mobileNum);
        return m.matches() && mobileNum.trim().length() > 0;
    }


    /**
     * Is mobile number valid with country code boolean.
     *
     * @param mobileNum the mobile num
     * @return the boolean
     */
    public static boolean isMobileNumberValidWithCountryCode(@NonNull String mobileNum) {
        Boolean result = false;
        if (TextUtils.isEmpty(mobileNum) || mobileNum.length() > 13 || mobileNum.length() < 10) {
            result = false;
        }
        if (mobileNum.length() >= 10) {
            if (mobileNum.startsWith("0") || mobileNum.startsWith("+91") || mobileNum.startsWith("91") || mobileNum.startsWith("091")) {
                String newMobile = mobileNum.substring(mobileNum.length() - 10);
                String expression = "^[9876]\\d{9}$";
                Pattern p = Pattern.compile(expression);
                Matcher m = p.matcher(newMobile);
                if (m.matches() && newMobile.trim().length() > 0) {
                    result = true;
                }
            } else {
                String expression = "^[987]\\d{9}$";
                Pattern p = Pattern.compile(expression);
                Matcher m = p.matcher(mobileNum);
                if (m.matches() && mobileNum.trim().length() > 0) {
                    result = true;
                }
            }
        }
        return result;
    }


    /**
     * Is valid pan number boolean.
     *
     * @param panNumber the pan number
     * @return boolean boolean
     */
    public static boolean isValidPanNumber(@NonNull String panNumber) {
        if (TextUtils.isEmpty(panNumber)) {
            return false;
        }

        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
        Matcher matcher = pattern.matcher(panNumber);
        // Check if pattern matches
        return matcher.matches();
    }


    /**
     * Is special character boolean.
     *
     * @param specialCharacter the special character
     * @return boolean boolean
     */
    public static boolean isSpecialCharacter(@NonNull String specialCharacter) {
        if (TextUtils.isEmpty(specialCharacter)) {
            return false;
        }
//        Pattern panPattern = Pattern.compile("[$+,:;=?@#|'<>.^*()%!]");
        Pattern panPattern = Pattern.compile("[a-zA-Z0-9 ]*");

        Matcher matcher = panPattern.matcher(specialCharacter);
        // Check if pattern matches
        return !matcher.matches();
    }

    /**
     * Is only name boolean.
     *
     * @param name the name
     * @return boolean boolean
     */
    public static boolean isOnlyName(@NonNull String name) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        Pattern panPattern = Pattern.compile("[a-zA-Z ]+(\\s+[a-zA-Z ]+)*");

        Matcher matcher = panPattern.matcher(name);
        // Check if pattern matches
        return matcher.matches();
    }

    /**
     * Is validate name boolean.
     *
     * @param context the m context
     * @param name    the name
     * @return boolean boolean
     */
    public static boolean isValidateName(@NonNull Context context, @NonNull String name) {
        boolean isSuccess = true;
        if (TextUtils.isEmpty(name)) {
            isSuccess = false;
            return isSuccess;
        }
        if (!ValidationUtils.isOnlyName(name)) {
            isSuccess = false;
            return isSuccess;
        }

        return isSuccess;
    }

    /**
     * Is validate phone number boolean.
     *
     * @param context     the m context
     * @param phoneNumber the phone number
     * @return boolean boolean
     */
    public static boolean isValidatePhoneNumber(@NonNull Context context, @NonNull String phoneNumber) {
        boolean isSuccess = true;
        if (TextUtils.isEmpty(phoneNumber)) {
            isSuccess = false;
            return isSuccess;
        }
        if (!ValidationUtils.isMobileNumberValid(phoneNumber)) {
            isSuccess = false;
            return isSuccess;
        }
        return isSuccess;
    }

    /**
     * Is validate email boolean.
     *
     * @param context the m context
     * @param emailId the email id
     * @return boolean boolean
     */
    public static boolean isValidateEmail(@NonNull Context context, @NonNull String emailId) {
        boolean isSuccess = true;
        if (TextUtils.isEmpty(emailId)) {
            isSuccess = false;
            return isSuccess;
        }
        if (!ValidationUtils.isEmailValid(emailId)) {
            isSuccess = false;
            return isSuccess;
        }
        return isSuccess;

    }

    /**
     * method to check if given string is null or empty.
     *
     * @param stringToCheck string to check if it is null or empty.
     * @return if given string is null or empty it will return default string else the given string itself.
     */
    public static String getCheckedString(String stringToCheck) {
        return !TextUtils.isEmpty(stringToCheck) ? stringToCheck : "";
    }

    /**
     * method to check if given string is null or empty.
     *
     * @param intToCheck string to check if it is null or empty.
     * @return if given string is null or empty it will return default string else the given string itself.
     */
    public static int getCheckedInt(Integer intToCheck) {
        return intToCheck != null ? intToCheck : -1;
    }

    /**
     * Is list not empty boolean.
     *
     * @param list the list
     * @return the boolean
     */
    public static boolean isListNotEmpty(List<?> list) {
        return list != null && !list.isEmpty();
    }

    /**
     * Is array not empty boolean.
     *
     * @param arr the arr
     * @return the boolean
     */
    public static boolean isArrayNotEmpty(int[] arr) {
        return arr != null && arr.length != 0;
    }


    public static Long getCheckedLong(Long longToCheck) {
        return longToCheck != null ? longToCheck : -1;
    }

    public static boolean isValidResponse(Object baseClass) {
        return baseClass != null;
    }


    /**
     * method to validate the given number string for min and max length
     *
     * @param pValue     number string to validate ex. user provided phone numbers.
     * @param pMinLength provided string must be at-least pMinLength long.
     * @param pMaxLength provided string must be lesser or equal to pMaxLength long.
     * @return return if given number string satisfies the min and max length
     */
    public static boolean validateNumbersForLength(String pValue, int pMinLength, int pMaxLength) {
        String regx = "^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(pValue);
        return matcher.find();
    }

    public static boolean validatePinCode(String pinCode) {
        String regx = "^[1-9][0-9]{5}$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(pinCode);
        return matcher.find();
    }

}
