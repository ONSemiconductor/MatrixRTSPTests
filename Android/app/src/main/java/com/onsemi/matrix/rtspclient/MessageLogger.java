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

import android.widget.TextView;
import android.widget.Toast;

public class MessageLogger {
    private TextView textView;

    public MessageLogger(TextView textView) {
        this.textView = textView;
    }

    public void printRTSPMessage(final String message) {
        textView.post(new Runnable() {
            @Override
            public void run() {
                textView.setText(textView.getText() + message);
            }
        });
    }

    public void printErrorMessage(final String message){
        textView.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(textView.getContext(), message, Toast.LENGTH_LONG).show();
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
