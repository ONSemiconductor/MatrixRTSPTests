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

package com.onsemi.matrix.rtspclient.commands;


import com.onsemi.matrix.rtspclient.MessageLogger;
import com.onsemi.matrix.rtspclient.RTSPCommand;
import com.onsemi.matrix.rtspclient.ResultLogger;
import com.onsemi.matrix.rtspclient.Test;

import java.net.URI;

import br.com.voicetechnology.rtspclient.RTSPClient;
import br.com.voicetechnology.rtspclient.concepts.Client;
import br.com.voicetechnology.rtspclient.concepts.Request;
import br.com.voicetechnology.rtspclient.concepts.Response;

public class DescribeCommand extends RTSPCommand {
    private String descriptor = null;
    private String uri = null;

    public DescribeCommand(RTSPClient client, MessageLogger mLogger, ResultLogger rLogger, String uri) {
        super(client, mLogger, rLogger);

        this.uri = uri;
    }

    @Override
    public void execute() {
        super.execute();

        try {
            this.client.describe(new URI(this.uri));
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
        this.messageLogger.info(String.format("Request:\n%s", request));

        this.messageLogger.info(String.format("Response:\n%s", response));

        if (this.descriptor != null) {
            this.messageLogger.info(String.format("%s\n", this.descriptor));
        }

        if (response.getStatusCode() == 200) {
            this.resultLogger.info(new Test(request.getMethod().toString(), true));
        } else {
            this.resultLogger.info(new Test(request.getMethod().toString(), false,
                    String.format("Expected: '%d' Actual: '%d'", 200, response.getStatusCode())));
        }
    }
}
