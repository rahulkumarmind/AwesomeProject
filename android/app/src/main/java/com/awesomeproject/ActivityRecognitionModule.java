package com.awesomeproject;

import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.modules.core.PermissionListener;
import com.facebook.react.modules.core.PermissionAwareActivity;

public class ActivityRecognitionModule extends ReactContextBaseJavaModule implements PermissionListener {

    private Promise permissionPromise;
    private static final int ACTIVITY_RECOGNITION_PERMISSION_REQUEST_CODE = 1000;

    // Constructor
    public ActivityRecognitionModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "ActivityRecognition";
    }

    @ReactMethod
    public void requestPermission(Promise promise) {
        Activity activity = getCurrentActivity();
        if (activity == null) {
            promise.reject("Activity not found", "Could not find activity to request permission.");
            return;
        }

        this.permissionPromise = promise; // Store the promise to resolve/reject later

        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED) {
            promise.resolve(true);
        } else {
            if (activity instanceof PermissionAwareActivity) {
                ((PermissionAwareActivity) activity).requestPermissions(new String[]{android.Manifest.permission.ACTIVITY_RECOGNITION},
                        ACTIVITY_RECOGNITION_PERMISSION_REQUEST_CODE, this);
            } else {
                promise.reject("Error", "Activity must implement PermissionAwareActivity for permissions to work.");
            }
        }
    }

    @Override
    public boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ACTIVITY_RECOGNITION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (permissionPromise != null) {
                    permissionPromise.resolve(true);
                }
            } else {
                if (permissionPromise != null) {
                    permissionPromise.resolve(false);
                }
            }
            permissionPromise = null; // Reset the promise to ensure it's only used once
            return true; // Return true to indicate the result has been handled
        }
        return false; // Return false if the request code does not match
    }
}
