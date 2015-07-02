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

import br.com.voicetechnology.rtspclient.RTSPClient;

public class SetParameterCommand extends RTSPCommand {
    private String parameter = null;

    public SetParameterCommand(RTSPClient client, MessageLogger mLogger, ResultLogger rLogger, String parameter) {
        super(client, mLogger, rLogger);

        this.parameter = parameter;
    }

    @Override
    public void execute() {
        super.execute();

        this.client.setParameter(this.parameter);
    }
}
