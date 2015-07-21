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
import com.onsemi.matrix.rtspclient.ResultLogger;
import com.onsemi.matrix.rtspclient.Settings;

import br.com.voicetechnology.rtspclient.RTSPClient;

public class SetParameterCommand extends RTSPCommand {
    private Settings settings = null;

    public SetParameterCommand(RTSPClient client, MessageLogger mLogger, ResultLogger rLogger, Settings settings) {
        super(client, mLogger, rLogger);

        this.settings = settings;
    }

    @Override
    public void execute() {
        super.execute();

        this.client.setParameter(this.settings.getSetParameterValue());
    }
}
