/*
 * Copyright (C) 2019 BaikalOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.baikalos;

import static android.os.Process.myUid;

import android.app.ActivityThread;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.LocaleList;
import android.os.SystemProperties;
import android.text.FontConfig;
import android.util.Log;

import android.provider.Settings;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;



public class BaikalSpoofer { 
	

    private static final String TAG = "BaikalSpoofer";

    private static boolean sIsGmsUnstable = false;
    private static boolean sIsFinsky = false;
    private static boolean sPreventHwKeyAttestation = false;

    private static String sPackageName = null;
    private static String sProcessName = null;
    
    
    public static void maybeSpoofProperties(Application app, Context context) {
        maybeSpoofBuild(app.getPackageName(), app.getProcessName(), context);
        maybeSpoofDevice(app.getPackageName(), context);
     }   
     
    public static int maybeSpoofFeature(String packageName, String name, int version) {
        if (packageName != null &&
                packageName.contains("com.google.android.apps.photos") ) {

            Log.i(TAG, "App " + packageName + " is requested " + name + " feature with " + version + " version");
            if( name.contains("PIXEL_2021_EXPERIENCE") || name.contains("PIXEL_2022_EXPERIENCE") ) {
                return 0;
            }
            if( "com.google.photos.trust_debug_certs".equals(name) ) return 1;
            if( "com.google.android.apps.photos.NEXUS_PRELOAD".equals(name) ) return 1;
            if( "com.google.android.feature.PIXEL_EXPERIENCE".equals(name) ) return 1;
            if( "com.google.android.apps.photos.PIXEL_PRELOAD".equals(name) ) return 1;
            if( "com.google.android.apps.photos.PIXEL_2016_PRELOAD".equals(name) ) return 1;
            if( name != null ) {
                if( name.startsWith("com.google.android.apps.photos.PIXEL") ) return 0;
                if( name.startsWith("com.google.android.feature.PIXEL") ) return 0;
            }
            return -1;
        }
        return -1;
    }

    public static void setVersionField(String key, String value) {
        /*
         * This would be much prettier if we just removed "final" from the Build fields,
         * but that requires changing the API.
         *
         * While this an awful hack, it's technically safe because the fields are
         * populated at runtime.
         */
        try {
            // Unlock
            Field field = Build.VERSION.class.getDeclaredField(key);
            field.setAccessible(true);

            // Edit
            field.set(null, value);

            // Lock
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Log.e(TAG, "Failed to spoof Version." + key, e);
        }
    }

    public static void setVersionField(String key, int value) {
        /*
         * This would be much prettier if we just removed "final" from the Build fields,
         * but that requires changing the API.
         *
         * While this an awful hack, it's technically safe because the fields are
         * populated at runtime.
         */
        try {
            // Unlock
            Field field = Build.VERSION.class.getDeclaredField(key);
            field.setAccessible(true);

            // Edit
            field.set(null, value);

            // Lock
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Log.e(TAG, "Failed to spoof Version." + key, e);
        }
    }


    public static void setBuildField(String key, String value) {
        /*
         * This would be much prettier if we just removed "final" from the Build fields,
         * but that requires changing the API.
         *
         * While this an awful hack, it's technically safe because the fields are
         * populated at runtime.
         */
        try {
            // Unlock
            Field field = Build.class.getDeclaredField(key);
            field.setAccessible(true);

            // Edit
            field.set(null, value);

            // Lock
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Log.e(TAG, "Failed to spoof Build." + key, e);
        }
    }

    public static void setProcessField(String key, String value) {
        /*
         * This would be much prettier if we just removed "final" from the Build fields,
         * but that requires changing the API.
         *
         * While this an awful hack, it's technically safe because the fields are
         * populated at runtime.
         */
        try {
            // Unlock
            Field field = Process.class.getDeclaredField(key);
            field.setAccessible(true);

            // Edit
            field.set(null, value);

            // Lock
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Log.e(TAG, "Failed to spoof Process." + key, e);
        }
    }

    private static void maybeSpoofBuild(String packageName, String processName, Context context) {

        sProcessName = processName;
        sPackageName = packageName;
        
        if( "com.google.android.gms.unstable".equals(processName) &&
            "com.google.android.gms".equals(packageName) ) {

            sIsGmsUnstable = true;
            sPreventHwKeyAttestation = true;
            
            String stockFp = SystemProperties.get("ro.build.stock_fingerprint", null);
            String stockSecurityPatch = SystemProperties.get("ro.build.stock_sec_patch", null);
            
            setVersionField("DEVICE_INITIAL_SDK_INT", Build.VERSION_CODES.S);

            setBuildField("FINGERPRINT", "Xiaomi/beryllium/beryllium:10/QKQ1.190828.002/V12.0.3.0.QEJMIXM:user/release-keys");
            setBuildField("PRODUCT", "beryllium");
            setBuildField("DEVICE", "beryllium");
            setBuildField("MODEL", "POCOPHONE F1");
            
            Log.e(TAG, "Spoof Device GMS SECURITY_PATCH: [" + stockSecurityPatch + "]");
            if( stockSecurityPatch != null && !stockSecurityPatch.isEmpty() )
                setVersionField("SECURITY_PATCH", stockSecurityPatch);
                
            return;                    
        } else if( "com.android.vending".equals(packageName) ) {
            sIsFinsky = true;
        }
    }
    
    private static void maybeSpoofDevice(String packageName, Context context) {
    	if( packageName == null ) return;
    	   if (packageName != null &&
                packageName.contains("com.google.android.apps.photos") ) {
                	setBuildField("FINGERPRINT", "google/marlin/marlin:10/QP1A.191005.007.A3/5972272:user/release-keys");
                    setBuildField("PRODUCT", "marlin");
                    setBuildField("DEVICE", "marlin");
                    setBuildField("MODEL", "Pixel XL");
                    setBuildField("BRAND", "google");
                    setBuildField("MANUFACTURER", "Google");
                    }
    }
    
    public static boolean isPreventHwKeyAttestation() {
        return sPreventHwKeyAttestation;
    }

    public static boolean isCurrentProcessGmsUnstable() {
        return sIsGmsUnstable;
    }

    public static String getPackageName() {
        return sProcessName;
    }

    public static String getProcessName() {
        return sPackageName;
    }

    private static boolean isCallerSafetyNet() {
        return Arrays.stream(Thread.currentThread().getStackTrace())
                .anyMatch(elem -> elem.getClassName().contains("DroidGuard"));
    }

    public static void onEngineGetCertificateChain() {
        // Check stack for SafetyNet
        if (isCurrentProcessGmsUnstable() && isCallerSafetyNet()) {
            throw new UnsupportedOperationException();
        }

        // Check stack for PlayIntegrity
        if (sIsFinsky) {
            throw new UnsupportedOperationException();
        }

        if(sPreventHwKeyAttestation) {
            throw new UnsupportedOperationException();
        } 
    }
}
