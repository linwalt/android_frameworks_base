package android.ext.settings;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/** @hide */
public class ExtSettings {

    public static final BoolSetting AUTO_GRANT_OTHER_SENSORS_PERMISSION = new BoolSetting(
            Setting.Scope.PER_USER, "auto_grant_OTHER_SENSORS_perm", true);

    public static final IntSetting AUTO_REBOOT_TIMEOUT = new IntSetting(
            Setting.Scope.GLOBAL, "settings_reboot_after_timeout",
            // default value: 3 days
            (int) TimeUnit.DAYS.toMillis(3));

    public static final BoolSetting SCREENSHOT_TIMESTAMP_EXIF = new BoolSetting(
            Setting.Scope.PER_USER, "screenshot_timestamp_exif", false);

    // also read in packages/modules/DnsResolver (DnsTlsTransport.cpp and doh/network/driver.rs)
    public static final IntSysProperty CONNECTIVITY_CHECKS = new IntSysProperty(
            "persist.sys.connectivity_checks",
            ConnChecksSetting.VAL_DEFAULT,
            ConnChecksSetting.VAL_GRAPHENEOS, ConnChecksSetting.VAL_STANDARD, ConnChecksSetting.VAL_DISABLED
    );

    // The amount of time in milliseconds before a disconnected Wi-Fi adapter is turned off
    public static final IntSetting WIFI_AUTO_OFF = new IntSetting(
            Setting.Scope.GLOBAL, "wifi_off_timeout", 0 /* off by default */);

    // The amount of time in milliseconds before a disconnected Bluetooth adapter is turned off
    public static final IntSetting BLUETOOTH_AUTO_OFF = new IntSetting(
            Setting.Scope.GLOBAL, "bluetooth_off_timeout", 0 /* off by default */);

    private ExtSettings() {}

    // used for making settings defined in this class unreadable by third-party apps
    public static void getKeys(Setting.Scope scope, Set<String> dest) {
        for (Field field : ExtSettings.class.getDeclaredFields()) {
            if (!Setting.class.isAssignableFrom(field.getType())) {
                continue;
            }
            Setting s;
            try {
                s = (Setting) field.get(null);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }

            if (s.getScope() == scope) {
                if (!dest.add(s.getKey())) {
                    throw new IllegalStateException("duplicate definition of setting " + s.getKey());
                }
            }
        }
    }
}
