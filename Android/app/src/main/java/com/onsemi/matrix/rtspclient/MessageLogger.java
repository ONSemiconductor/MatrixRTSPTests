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

import android.widget.TextView;

public class MessageLogger {
    private TextView textView;

    public MessageLogger(TextView textView) {
        this.textView = textView;
    }

    public void info(final String message) {
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setText(textView.getText() + message);
            }
        });
    }

    public void clean() {
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setText("");
            }
        });
    }
}
