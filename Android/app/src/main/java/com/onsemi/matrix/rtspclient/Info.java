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

public class Info {
    private String methodName = null;
    private  String error = null;

    private boolean isPassed = false;

    public Info(String methodName, boolean isPassed) {
        this.methodName = methodName;
        this.isPassed = isPassed;
    }

    public Info(String name, boolean isPassed, String error) {
        this(name, isPassed);

        this.error = error;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public String getError() {
        return this.error;
    }
}
