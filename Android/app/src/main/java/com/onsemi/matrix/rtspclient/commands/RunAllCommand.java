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

import com.onsemi.matrix.rtspclient.RTSPCommand;

import java.util.List;
import java.util.Observable;
import java.util.Observer;


public class RunAllCommand implements Observer {
    private List<RTSPCommand> commands = null;

    public RunAllCommand(List<RTSPCommand> commands) {
        this.commands = commands;
    }

    private RTSPCommand findCommandByType(Class type) {
        for(RTSPCommand command : this.commands) {
            if(command.getClass() == type) {
                return command;
            }
        }

        return null;
    }

    public void execute() {
        try {
            RTSPCommand command = this.findCommandByType(OptionsCommand.class);
            command.setRequestFinishedObserver(this);

            command.execute();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        RTSPCommand command = null;

        ((RTSPCommand)observable).setRequestFinishedObserver(null);

        if (observable.getClass() == OptionsCommand.class) {
            command = RunAllCommand.this.findCommandByType(DescribeCommand.class);
        } else if (observable.getClass() == DescribeCommand.class) {
            command = RunAllCommand.this.findCommandByType(SetupCommand.class);
        } else if (observable.getClass() == SetupCommand.class) {
            command = RunAllCommand.this.findCommandByType(PlayCommand.class);
        } else if (observable.getClass() == PlayCommand.class) {
            command = RunAllCommand.this.findCommandByType(PauseCommand.class);
        } else if (observable.getClass() == PauseCommand.class) {
            command = RunAllCommand.this.findCommandByType(RecordCommand.class);
        } else if (observable.getClass() == RecordCommand.class) {
            command = RunAllCommand.this.findCommandByType(AnnounceCommand.class);
        } else if (observable.getClass() == AnnounceCommand.class) {
            command = RunAllCommand.this.findCommandByType(GetParameterCommand.class);
        } else if (observable.getClass() == GetParameterCommand.class) {
            command = RunAllCommand.this.findCommandByType(SetParameterCommand.class);
        } else if (observable.getClass() == SetParameterCommand.class) {
            command = RunAllCommand.this.findCommandByType(TeardownCommand.class);
        } else if (observable.getClass() == TeardownCommand.class) {
            command = RunAllCommand.this.findCommandByType(TeardownCommand.class);
        }

        if (command != null) {
            command.setRequestFinishedObserver(this);
            command.execute();
        }
    }
}
