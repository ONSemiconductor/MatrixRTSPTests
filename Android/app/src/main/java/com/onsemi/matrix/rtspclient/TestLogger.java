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

public class TestLogger {
    private TextView textView;

    private ArrayList<TestResult> result = new ArrayList<TestResult>();

    public TestLogger(TextView textView) {
        this.textView = textView;
    }

    public void printTestResult(TestResult result) {
        this.result.add(result);

        this.textView.post(new Runnable() {
            @Override
            public void run() {
                String message = new String();

                for(int i = 0; i < TestLogger.this.result.size(); i++) {
                    String result = TestLogger.this.result.get(i).isPassed() ? "passed" : "failed";
                    String resultColor = TestLogger.this.result.get(i).isPassed() ? "#00ff00" : "#ff0000";

                    message += String.format("Command: <font color='#999999'>%s</font> Result: <font color='%s'>%s</font><br/>",
                            TestLogger.this.result.get(i).getMethodName(), resultColor, result);

                    if(TestLogger.this.result.get(i).getError() != null) {
                        message += String.format("> Error: <font color='#777777'>%s</font><br/>",
                                TestLogger.this.result.get(i).getError());
                    }
                }

                TestLogger.this.textView.setText(Html.fromHtml(message));
            }
        });
    }

    public void clean() {
        this.result.clear();

        this.textView.post(new Runnable() {
            @Override
            public void run() {
                TestLogger.this.textView.setText("");
            }
        });
    }
}
