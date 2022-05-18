package com.example.imperativeassignment.utils;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Pattern;


public class UtilityClass {

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    public static void showToastMgs(Context ctx, String mgs) {
        Toast.makeText(ctx, mgs, Toast.LENGTH_SHORT).show();
    }

    public static boolean isValidMobileNumber(String number) {
        return (!TextUtils.isEmpty(number) && number.length() == 10);
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return ((!TextUtils.isEmpty(email)) && (pattern.matcher(email).matches()));
//        return (TextUtils.isEmpty(email)) ? false : pattern.matcher(email).matches();
       /* if(!TextUtils.isEmpty(email)) {
            Pattern pattern = Patterns.EMAIL_ADDRESS;
            return pattern.matcher(email).matches();
        }else{
            return false;
        }*/
    }



    public static void hideKeyboard(Activity activity) {
        activity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }


}
