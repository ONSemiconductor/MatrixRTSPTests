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

package com.onsemi.matrix.rtspclient.commands;

import com.onsemi.matrix.rtspclient.TestResult;
import com.onsemi.matrix.rtspclient.MessageLogger;
import com.onsemi.matrix.rtspclient.RTSPCommand;
import com.onsemi.matrix.rtspclient.TestLogger;
import com.onsemi.matrix.rtspclient.Settings;

import java.net.URI;

import br.com.voicetechnology.rtspclient.MissingHeaderException;
import br.com.voicetechnology.rtspclient.RTSPClient;
import br.com.voicetechnology.rtspclient.concepts.Header;
import br.com.voicetechnology.rtspclient.concepts.Request;
import br.com.voicetechnology.rtspclient.concepts.Response;

public class OptionsCommand extends RTSPCommand {
    private Settings settings = null;

    public OptionsCommand(RTSPClient client, MessageLogger mLogger, TestLogger tLogger, Settings settings) {
        super(client, mLogger, tLogger);

        this.settings = settings;
    }

    @Override
    public void execute() {
        super.execute();

        try {
            this.client.options("*", new URI(this.settings.getCameraURL()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public TestResult verify(Request request, Response response) {
        TestResult testResult = super.verify(request, response);

        if (!testResult.isPassed()) {
            return testResult;
        }

        String methodName = request.getMethod().toString();

        try {
            Header publicHeader = response.getHeader("Public");
            String publicHeaderValue = publicHeader.getRawValue();

            for (Request.Method method : Request.Method.values()) {
                if(!publicHeaderValue.contains(method.toString())) {
                    return new TestResult(methodName, false, String.format(
                            "'Public' header doesn't contain %s command", method));
                }
            }
        } catch (MissingHeaderException e) {
            return new TestResult(methodName, false, "Response doesn't contain 'Public' header");
        }


        return new TestResult(methodName, true);
    }
}
