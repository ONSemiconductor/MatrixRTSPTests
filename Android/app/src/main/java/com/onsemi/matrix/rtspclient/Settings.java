/*
** Copyright 2015 ON Semiconductor
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
** You may obtain a copy of the License at
**
**  http://www.apache.org/licenses/LICENSE-2.0
**
** Unless required by applicable law or agreed to in writing, software
** distributed under the License is distributed on an "AS IS" BASIS,
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
** See the License for the specific language governing permissions and
** limitations under the License.
*/

package com.onsemi.matrix.rtspclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {
    private final String CameraURL = "rtsp://192.168.10.173:8551/PSIA/Streaming/channels/2?videoCodecType=H.264";

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
