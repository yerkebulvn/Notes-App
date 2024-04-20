package com.notes.app.Methods;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.notes.app.Activity.DeleteActivity.DeleteActivity;

// Навигацияға қатысты әдістерге арналған утилита класы
public class NavigationUtil {

    // Жою Әрекетіне өтудің статикалық әдісі
    public static void DeleteActivity(@NonNull Activity activity) {
        // Жою Әрекетін бастау ниетін жасау
        ActivityCompat.startActivity(activity, new Intent(activity, DeleteActivity.class), null);
        // Ағымдағы әрекетті артқы стектен шығару үшін аяқтау
        activity.finish();
    }
}
