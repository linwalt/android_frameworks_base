package android.ext.settings;

import java.util.concurrent.TimeUnit;

import static android.ext.settings.GnssConstants.SUPL_DISABLED;
import static android.ext.settings.GnssConstants.SUPL_SERVER_GRAPHENEOS_PROXY;
import static android.ext.settings.GnssConstants.SUPL_SERVER_STANDARD;
import static android.ext.settings.RemoteProvisioningConstants.GRAPHENEOS_PROXY;
import static android.ext.settings.RemoteProvisioningConstants.STANDARD_SERVER;

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

    public static final IntSetting GNSS_SUPL = new IntSetting(
            Setting.Scope.GLOBAL, "force_disable_supl", // historical name
            SUPL_SERVER_GRAPHENEOS_PROXY, // default
            SUPL_SERVER_STANDARD, SUPL_DISABLED, SUPL_SERVER_GRAPHENEOS_PROXY // valid values
    );


    public static final IntSetting REMOTE_PROVISIONING_SERVER = new IntSetting(
            Setting.Scope.GLOBAL, "attest_remote_provisioner_server", // historical setting key
            GRAPHENEOS_PROXY, // default
            STANDARD_SERVER, GRAPHENEOS_PROXY // valid values
    );

    private ExtSettings() {}
}
