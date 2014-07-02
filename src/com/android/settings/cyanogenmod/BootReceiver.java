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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.settings.Utils;

import java.util.Arrays;
import java.util.List;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context ctx, Intent intent) {
		usbRomSettings(ctx);
    }

	// tmtmtm
    private void usbRomSettings(Context ctx) {
		final String USE_FASTCHARGE_IN_HOSTMODE_PROP = "persist.sys.use_fcharge_host";
		final String USE_FASTCHARGE_IN_HOSTMODE_DEFAULT = "0";
		final String FASTCHARGE_IN_HOSTMODE_FILE = "/sys/kernel/usbhost/usbhost_fastcharge_in_host_mode";
		String useFastChargeInHostMode = SystemProperties.get(USE_FASTCHARGE_IN_HOSTMODE_PROP,
			                                                  USE_FASTCHARGE_IN_HOSTMODE_DEFAULT);
        Log.i(TAG, "usbRomSettings useFastChargeInHostMode="+useFastChargeInHostMode);
		if("1".equals(useFastChargeInHostMode)) {
			if (Utils.fileWriteOneLine(FASTCHARGE_IN_HOSTMODE_FILE, "0")) {
				if (Utils.fileWriteOneLine(FASTCHARGE_IN_HOSTMODE_FILE, "1")) {
			        Log.i(TAG, "usbRomSettings useFastChargeInHostMode toggled off/on");
			    }
			}
		} else if("0".equals(useFastChargeInHostMode)) {
			if (Utils.fileWriteOneLine(FASTCHARGE_IN_HOSTMODE_FILE, "1")) {
				if (Utils.fileWriteOneLine(FASTCHARGE_IN_HOSTMODE_FILE, "0")) {
			        Log.i(TAG, "usbRomSettings useFastChargeInHostMode toggled on/off");
			    }
			}
		}
 
		final String USE_FI_MODE_PROP = "persist.sys.use_fi_mode";
		final String USE_FI_MODE_DEFAULT = "1";
	    final String FI_MODE_FILE = "/sys/kernel/usbhost/usbhost_fixed_install_mode";
		String useFiMode = SystemProperties.get(USE_FI_MODE_PROP, USE_FI_MODE_DEFAULT);
		Log.i(TAG, "usbRomSettings useFiMode="+useFiMode);
		// useFiMode=1 is boot default, see: arch/arm/mach-tegra/usbhost.c
		if("0".equals(useFiMode)) {
            if (Utils.fileWriteOneLine(FI_MODE_FILE, "0")) {
		        Log.i(TAG, "usbRomSettings useFiMode switched from FI to OTG mode");
			}
		}
   }
}
