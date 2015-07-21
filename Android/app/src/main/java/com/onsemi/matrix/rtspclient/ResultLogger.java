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

import android.text.Html;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultLogger {
    private TextView textView;

    private ArrayList<Info> result = new ArrayList<Info>();

    public ResultLogger(TextView textView) {
        this.textView = textView;
    }

    public void info(Info info) {
        result.add(info);

        this.textView.post(new Runnable() {
            @Override
            public void run() {
                String message = new String();

                for(int i = 0; i < ResultLogger.this.result.size(); i++) {
                    String result = ResultLogger.this.result.get(i).isPassed() ? "passed" : "failed";
                    String resultColor = ResultLogger.this.result.get(i).isPassed() ? "#00ff00" : "#ff0000";

                    message += String.format("Command: <font color='#999999'>%s</font> Result: <font color='%s'>%s</font><br/>",
                            ResultLogger.this.result.get(i).getMethodName(), resultColor, result);

                    if(ResultLogger.this.result.get(i).getError() != null) {
                        message += String.format("> Error: <font color='#777777'>%s</font><br/>",
                                ResultLogger.this.result.get(i).getError());
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
