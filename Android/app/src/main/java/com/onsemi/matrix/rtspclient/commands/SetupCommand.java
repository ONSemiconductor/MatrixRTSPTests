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

import com.onsemi.matrix.rtspclient.Info;
import com.onsemi.matrix.rtspclient.MessageLogger;
import com.onsemi.matrix.rtspclient.RTSPCommand;
import com.onsemi.matrix.rtspclient.ResultLogger;

import java.net.URI;

import br.com.voicetechnology.rtspclient.MissingHeaderException;
import br.com.voicetechnology.rtspclient.RTSPClient;
import br.com.voicetechnology.rtspclient.concepts.Header;
import br.com.voicetechnology.rtspclient.concepts.Request;
import br.com.voicetechnology.rtspclient.concepts.Response;

public class SetupCommand extends RTSPCommand {
    private int port;
    private String uri;

    public SetupCommand(RTSPClient client, MessageLogger mLogger, ResultLogger rLogger, String uri, int port) {
        super(client, mLogger, rLogger);

        this.uri = uri;
        this.port = port;
    }

    @Override
    public void execute() {
        super.execute();

        try {
            this.client.setup(new URI(this.uri), this.port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Info verify(Request request, Response response) {
        Info info = super.verify(request, response);

        if (!info.isPassed()) {
            return info;
        }

        String methodName = request.getMethod().toString();

        try {
            Header transportHeader = response.getHeader("Transport");
            String[] transportProperties = transportHeader.getRawValue().split(";");

            String clientPort = null;

            for(String transportProperty : transportProperties) {
                if (transportProperty.startsWith("client_port=")) {
                    clientPort = transportProperty;
                    break;
                }
            }

            if (clientPort == null) {
                return new Info(methodName, false, "'Transport' header doesn't contain 'client_port' property");
            }

            if (clientPort.compareTo(String.format("client_port=%d-%d", port, port + 1)) != 0) {
                return new Info(methodName, false, String.format("Expected: 'client_port=%d-%d' Actual: '%s'", port, port + 1, clientPort));
            }
        } catch (MissingHeaderException e) {
            return new Info(methodName, false, "Response doesn't contain 'Transport' header");
        }


        return new Info(methodName, true);
    }
}
