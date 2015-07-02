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

import android.text.Html;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultLogger {
    private TextView textView;

    private ArrayList<Test> result = new ArrayList<>();

    public ResultLogger(TextView textView) {
        this.textView = textView;
    }

    public void info(Test test) {
        result.add(test);

        this.textView.post(new Runnable() {
            @Override
            public void run() {
                String message = new String();

                for(Test test : ResultLogger.this.result) {
                    String result = test.isPassed() ? "passed" : "failed";
                    String resultColor = test.isPassed() ? "#00ff00" : "#ff0000";

                    message += String.format("Command: <font color='#999999'>%s</font> Result: <font color='%s'>%s</font><br/>", test.getName(), resultColor, result);

                    if(test.getError() != null) {
                        message += String.format("> Error: <font color='#777777'>%s</font><br/>", test.getError());
                    }
                }

                ResultLogger.this.textView.setText(Html.fromHtml(message));
            }
        });
    }

    public void clean() {
        this.result.clear();

        this.textView.post(new Runnable() {
            @Override
            public void run() {
                ResultLogger.this.textView.setText("");
            }
        });
    }
}
