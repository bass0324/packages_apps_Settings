/*
 * Copyright (C) 2012 The CyanogenMod Project
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

package com.android.settings.cyanogenmod;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.ListPreference;
import android.preference.PreferenceScreen;
import android.util.Log;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

/**
 * USB Host Settings
 */
public class UsbHostSettings extends SettingsPreferenceFragment
        implements Preference.OnPreferenceChangeListener {
    private static final String TAG = "UsbHostSettings";

    public static final String FI_MODE_FILE = "/sys/kernel/usbhost/usbhost_fixed_install_mode";
    private static final String FI_MODE_PREF = "fixed_installation";   // from res/values/strings.xml
    private CheckBoxPreference mFiModePref;

    public static final String FASTCHARGE_IN_HOSTMODE_FILE = "/sys/kernel/usbhost/usbhost_fastcharge_in_host_mode";
    private static final String FASTCHARGE_IN_HOSTMODE_PREF = "fastcharge_in_host_mode";   // from res/values/strings.xml
    private CheckBoxPreference mFastChargeInHostModePref;

    private static final String HP_WIRED_ACCESSORY_PREF = "hotplug_wired_accessory";   // from res/values/strings.xml
    private CheckBoxPreference mHpWiredAccessoryPref;
    private static final String USE_HP_WIRED_ACCESSORY_PERSIST_PROP = "persist.sys.use_wired_accessory";
    private static final String USE_HP_WIRED_ACCESSORY_DEFAULT = "1";

    public static final String HP_ON_BOOT_FILE = "/sys/kernel/usbhost/usbhost_hotplug_on_boot";
    private static final String HP_ON_BOOT_PREF = "hotplug_on_boot";   // from res/values/strings.xml
    private CheckBoxPreference mHpOnBootPref;


/*
    private static final String USE_DITHERING_PREF = "pref_use_dithering";

    private static final String USE_DITHERING_PERSIST_PROP = "persist.sys.use_dithering";

    private static final String USE_DITHERING_DEFAULT = "1";

    private static final String USE_16BPP_ALPHA_PREF = "pref_use_16bpp_alpha";

    private static final String USE_16BPP_ALPHA_PROP = "persist.sys.use_16bpp_alpha";

    private ListPreference mUseDitheringPref;

    private CheckBoxPreference mUse16bppAlphaPref;

    private AlertDialog alertDialog;
*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.usbhost_settings);

        PreferenceScreen prefSet = getPreferenceScreen();
        mFiModePref = (CheckBoxPreference) prefSet.findPreference(FI_MODE_PREF);
        mFastChargeInHostModePref = (CheckBoxPreference) prefSet.findPreference(FASTCHARGE_IN_HOSTMODE_PREF);
        mHpWiredAccessoryPref = (CheckBoxPreference) prefSet.findPreference(HP_WIRED_ACCESSORY_PREF);
        mHpOnBootPref = (CheckBoxPreference) prefSet.findPreference(HP_ON_BOOT_PREF);
        String temp;

        if((temp = Utils.fileReadOneLine(FI_MODE_FILE)) != null) {
            mFiModePref.setChecked("1".equals(temp));
        }
        mFiModePref.setEnabled(false);

        if((temp = Utils.fileReadOneLine(FASTCHARGE_IN_HOSTMODE_FILE)) != null) {
            mFastChargeInHostModePref.setChecked("1".equals(temp));
        }
        //mFastChargeInHostModePref.setEnabled(false);

        //mHpWiredAccessoryPref.setEnabled(false);

        //mHpOnBootPref.setEnabled(false);
        if((temp = Utils.fileReadOneLine(HP_ON_BOOT_FILE)) != null) {
            mHpOnBootPref.setChecked("1".equals(temp));
        }

        if (getPreferenceManager() != null) {

            String useHpWiredAccessory = SystemProperties.get(USE_HP_WIRED_ACCESSORY_PERSIST_PROP,
                                                              USE_HP_WIRED_ACCESSORY_DEFAULT);
            mHpWiredAccessoryPref.setChecked("1".equals(useHpWiredAccessory));

/*
            String useDithering = SystemProperties.get(USE_DITHERING_PERSIST_PROP, USE_DITHERING_DEFAULT);
            mUseDitheringPref = (ListPreference) prefSet.findPreference(USE_DITHERING_PREF);
            mUseDitheringPref.setOnPreferenceChangeListener(this);
            mUseDitheringPref.setValue(useDithering);
            mUseDitheringPref.setSummary(mUseDitheringPref.getEntry());

            mUse16bppAlphaPref = (CheckBoxPreference) prefSet.findPreference(USE_16BPP_ALPHA_PREF);
            String use16bppAlpha = SystemProperties.get(USE_16BPP_ALPHA_PROP, "0");
            mUse16bppAlphaPref.setChecked("1".equals(use16bppAlpha));
*/

            /* Display the warning dialog */
/*
            alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle(R.string.performance_settings_warning_title);
            alertDialog.setMessage(getResources().getString(R.string.performance_settings_warning));
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                    getResources().getString(com.android.internal.R.string.ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
            alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    PerformanceSettings.this.finish();
                }
            });
            alertDialog.show();
*/
        }
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        if(preference == mFiModePref) {
            Log.i(TAG, "onPreferenceTreeClick mFiModePref checked="+mFiModePref.isChecked());
            if (Utils.fileWriteOneLine(FI_MODE_FILE, mFiModePref.isChecked() ? "1" : "0")) {
                Log.i(TAG, "onPreferenceTreeClick value changed");
                return true;
            }
            Log.i(TAG, "onPreferenceTreeClick failed");

        } else if(preference == mFastChargeInHostModePref) {
            Log.i(TAG, "onPreferenceTreeClick mFastChargeInHostModePref checked="+mFastChargeInHostModePref.isChecked());
            if (Utils.fileWriteOneLine(FASTCHARGE_IN_HOSTMODE_FILE, mFastChargeInHostModePref.isChecked() ? "1" : "0")) {
                Log.i(TAG, "onPreferenceTreeClick value changed");
                return true;
            }
            Log.i(TAG, "onPreferenceTreeClick failed");

        } else if(preference == mHpWiredAccessoryPref) {
            SystemProperties.set(USE_HP_WIRED_ACCESSORY_PERSIST_PROP,
                    mHpWiredAccessoryPref.isChecked() ? "1" : "0");
            return true;
                    
        } else if(preference == mHpOnBootPref) {
            Log.i(TAG, "onPreferenceTreeClick mHpOnBootPref checked="+mHpOnBootPref.isChecked());
            if (Utils.fileWriteOneLine(HP_ON_BOOT_FILE, mHpOnBootPref.isChecked() ? "1" : "0")) {
                Log.i(TAG, "onPreferenceTreeClick value changed");
                return true;
            }
            Log.i(TAG, "onPreferenceTreeClick failed");

        }
        // If we didn't handle it, let preferences handle it.
        Log.i(TAG, "onPreferenceTreeClick not handled");
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
/*
        if (preference == mUseDitheringPref) {
            String newVal = (String) newValue;
            int index = mUseDitheringPref.findIndexOfValue(newVal);
            SystemProperties.set(USE_DITHERING_PERSIST_PROP, newVal);
            mUseDitheringPref.setSummary(mUseDitheringPref.getEntries()[index]);
        }
*/

/*
        String fname = "";

        if (newValue != null) {
            if (preference == mFiModePref) {
                Log.i(TAG, "onPreferenceChange mFiModePref "+(String)newValue);
                fname = FI_MODE_FILE;
            } else {
                Log.i(TAG, "onPreferenceChange other");
            }

            if (Utils.fileWriteOneLine(fname, (String) newValue)) {
                Log.i(TAG, "onPreferenceChange value changed");
                return true;
            }
        }
*/
        Log.i(TAG, "onPreferenceChange not implemented");
        return false;
    }

}
