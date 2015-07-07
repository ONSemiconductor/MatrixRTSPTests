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

public class OptionsCommand extends RTSPCommand {
    private String uri = null;

    public OptionsCommand(RTSPClient client, MessageLogger mLogger, ResultLogger rLogger, String uri) {
        super(client, mLogger, rLogger);

        this.uri = uri;
    }

    @Override
    public void execute() {
        super.execute();

        try {
            this.client.options("*", new URI(this.uri));
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
            Header publicHeader = response.getHeader("Public");
            String publicHeaderValue = publicHeader.getRawValue();

            for (Request.Method method : Request.Method.values()) {
                if(!publicHeaderValue.contains(method.toString())) {
                    return new Info(methodName, false, String.format(
                            "'Public' header doesn't contain %s command", method));
                }
            }
        } catch (MissingHeaderException e) {
            return new Info(methodName, false, "Response doesn't contain 'Public' header");
        }


        return new Info(methodName, true);
    }
}
