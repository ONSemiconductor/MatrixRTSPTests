/*
** Copyright 2015 ON Semiconductor Inc.
**
**
** This file is part of MatrixRTSPTests.
**
** MatrixRTSPTests is free software: you can redistribute it and/or modify
** it under the terms of the GNU General Public License as published by
** the Free Software Foundation, either version 3 of the License, or
** (at your option) any later version.
**
** MatrixRTSPTests is distributed in the hope that it will be useful,
** but WITHOUT ANY WARRANTY; without even the implied warranty of
** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
** GNU General Public License for more details.
**
** You should have received a copy of the GNU General Public License
** along with MatrixRTSPTests.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.onsemi.matrix.rtspclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {
    private final String CameraURL = "rtsp://192.168.1.168:8551/PSIA/Streaming/channels/2?videoCodecType=H.264";

    private final String GetParameterCommandParameter = "packets_received";
    private final String SetParameterCommandParameter = "barparam: barstuff";

    private Context context = null;

    public Settings(Context context) {
        this.context = context;

        String cameraUTL = this.getCameraURL();

        if (cameraUTL == "" || cameraUTL == null) {
            this.setPreference(R.string.key_saved_camera_url, CameraURL);
        }

        String getParameterCommandParameter = this.getGetParameterValue();

        if (getParameterCommandParameter == "" || getParameterCommandParameter == null) {
            this.setPreference(R.string.key_saved_get_parameter_parameter, GetParameterCommandParameter);
        }

        String setParameterCommandParameter = this.getSetParameterValue();

        if (setParameterCommandParameter == "" || setParameterCommandParameter == null) {
            this.setPreference(R.string.key_saved_set_parameter_parameter, SetParameterCommandParameter);
        }
    }

    public String getCameraURL() {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.key_saved_camera_url), null);
    }

    public String getGetParameterValue() {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.key_saved_get_parameter_parameter), null);
    }

    public String getSetParameterValue() {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(context.getString(R.string.key_saved_set_parameter_parameter), null);
    }

    private void setPreference(int key, String value) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        editor.putString(context.getString(key), value);

        editor.commit();
    }
}
