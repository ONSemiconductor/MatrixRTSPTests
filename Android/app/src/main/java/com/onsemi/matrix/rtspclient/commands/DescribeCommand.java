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


import com.onsemi.matrix.rtspclient.MessageLogger;
import com.onsemi.matrix.rtspclient.RTSPCommand;
import com.onsemi.matrix.rtspclient.TestLogger;
import com.onsemi.matrix.rtspclient.Settings;

import java.net.URI;

import br.com.voicetechnology.rtspclient.RTSPClient;
import br.com.voicetechnology.rtspclient.concepts.Client;
import br.com.voicetechnology.rtspclient.concepts.Request;
import br.com.voicetechnology.rtspclient.concepts.Response;

public class DescribeCommand extends RTSPCommand {
    private String descriptor = null;
    private Settings settings = null;

    public DescribeCommand(RTSPClient client, MessageLogger mLogger, TestLogger tLogger, Settings settings) {
        super(client, mLogger, tLogger);

        this.settings = settings;
    }

    @Override
    public void execute() {
        super.execute();

        try {
            this.client.describe(new URI(this.settings.getCameraURL()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mediaDescriptor(Client client, String descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public void response(Client client, Request request, Response response) {
        this.messageLogger.printRTSPMessage(String.format("Request:\n%s", request));

        this.messageLogger.printRTSPMessage(String.format("Response:\n%s", response));

        if (this.descriptor != null) {
            this.messageLogger.printRTSPMessage(String.format("%s\n", this.descriptor));
        }

        this.testLogger.printTestResult(this.verify(request, response));

        this.notifyRequestFinishedObserver();
    }
}
