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
