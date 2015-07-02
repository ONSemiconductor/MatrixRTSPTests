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

import java.net.URI;
import java.net.URISyntaxException;

import br.com.voicetechnology.rtspclient.RTSPClient;
import br.com.voicetechnology.rtspclient.concepts.Client;
import br.com.voicetechnology.rtspclient.concepts.Request;
import br.com.voicetechnology.rtspclient.concepts.Response;

public class RunAllCommand extends RTSPCommand {
    private String uri;
    private int port;

    private String getParameterValue;
    private String setParameterValue;

    private String announceDescription;

    public RunAllCommand(RTSPClient client, MessageLogger mLogger, ResultLogger rLogger,
                         String uri, int port,
                         String getParameterValue, String setParameterValue,
                         String announceDescription) {
        super(client, mLogger, rLogger);

        this.uri = uri;
        this.port = port;

        this.getParameterValue = getParameterValue;
        this.setParameterValue = setParameterValue;

        this.announceDescription = announceDescription;
    }

    @Override
    public void execute() {
        super.execute();

        try {
            this.client.options("*", new URI(uri));

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void response(Client client, Request request, Response response) {
        this.print(request, response);

        try {
            switch (request.getMethod()) {
                case OPTIONS:
                    this.client.describe(new URI(uri));
                    break;
                case DESCRIBE:
                    this.client.setup(new URI(uri), port);
                    break;
                case SETUP:
                    this.client.play();
                    break;
                case PLAY:
                    this.client.pause();
                    break;
                case PAUSE:
                    this.client.record();
                    break;
                case RECORD:
                    this.client.getParameter(getParameterValue);
                    break;
                case GET_PARAMETER:
                    this.client.setParameter(setParameterValue);
                    break;
                case SET_PARAMETER:
                    this.client.announce(announceDescription);
                    break;
                case ANNOUNCE:
                    this.client.teardown();
                    break;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
