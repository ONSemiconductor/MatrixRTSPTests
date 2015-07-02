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

import br.com.voicetechnology.rtspclient.RTSPClient;

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
}
