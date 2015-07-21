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

package com.onsemi.matrix.rtspclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.onsemi.matrix.rtspclient.commands.AnnounceCommand;
import com.onsemi.matrix.rtspclient.commands.DescribeCommand;
import com.onsemi.matrix.rtspclient.commands.GetParameterCommand;
import com.onsemi.matrix.rtspclient.commands.OptionsCommand;
import com.onsemi.matrix.rtspclient.commands.PauseCommand;
import com.onsemi.matrix.rtspclient.commands.PlayCommand;
import com.onsemi.matrix.rtspclient.commands.RecordCommand;
import com.onsemi.matrix.rtspclient.commands.RunAllCommand;
import com.onsemi.matrix.rtspclient.commands.SetParameterCommand;
import com.onsemi.matrix.rtspclient.commands.SetupCommand;
import com.onsemi.matrix.rtspclient.commands.TeardownCommand;

import java.util.Arrays;

import br.com.voicetechnology.rtspclient.RTSPClient;
import br.com.voicetechnology.rtspclient.transport.PlainTCP;


public class MainActivity extends AppCompatActivity {
    private MessageLogger messageLogger = null;
    private ResultLogger resultLogger = null;

    private SparseArray<RTSPCommand> commands = null;

    private Settings settings = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            this.settings = new Settings(this);

            this.messageLogger = new MessageLogger((TextView)findViewById(R.id.messageLogsTextView));
            this.resultLogger = new ResultLogger((TextView)findViewById(R.id.resultLogsTextView));

            this.commands = this.createCommands();

            this.setupRunAllButton();

            this.setupTabHost();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            this.messageLogger.clean();
            this.resultLogger.clean();
            return true;
        } else if(id == R.id.action_settings) {
            this.startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v) {
        final RTSPCommand command = MainActivity.this.commands.get(v.getId());

        if(command == null) {
            return;
        }

        if (command.getClass() == PlayCommand.class || command.getClass() == PauseCommand.class ||
                command.getClass() == TeardownCommand.class || command.getClass() == RecordCommand.class ||
                command.getClass() == GetParameterCommand.class || command.getClass() == SetParameterCommand.class ||
                command.getClass() == AnnounceCommand.class) {
            if(command.getClient().getSession() == null) {
                Toast.makeText(MainActivity.this, "Click 'setup' to create session", Toast.LENGTH_LONG).show();
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                command.execute();
            }
        }).start();
    }

    private SparseArray<RTSPCommand> createCommands() {
        SparseArray<RTSPCommand> cs = new SparseArray<RTSPCommand>();

        RTSPClient client = new RTSPClient();
        client.setTransport(new PlainTCP());

        cs.put(R.id.describeBtn, new DescribeCommand(client, this.messageLogger, this.resultLogger, this.settings));
        cs.put(R.id.optionsBtn, new OptionsCommand(client, this.messageLogger, this.resultLogger, this.settings));
        cs.put(R.id.setupBtn, new SetupCommand(client, this.messageLogger, this.resultLogger, this.settings, 0));

        cs.put(R.id.playBtn, new PlayCommand(client, this.messageLogger, this.resultLogger));
        cs.put(R.id.pauseBtn, new PauseCommand(client, this.messageLogger, this.resultLogger));
        cs.put(R.id.recordBtn, new RecordCommand(client, this.messageLogger, this.resultLogger));
        cs.put(R.id.teardownBtn, new TeardownCommand(client, this.messageLogger, this.resultLogger));

        cs.put(R.id.announceBtn, new AnnounceCommand(client, this.messageLogger, this.resultLogger,
                this.getString(R.string.announce_command_description)));

        cs.put(R.id.getParameterBtn, new GetParameterCommand(client, this.messageLogger, this.resultLogger, this.settings));
        cs.put(R.id.setParameterBtn, new SetParameterCommand(client, this.messageLogger, this.resultLogger, this.settings));

        return cs;
    }

    private void setupRunAllButton() {
        Button runAllBtn = (Button)this.findViewById(R.id.runAllBtn);

        runAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new RunAllCommand(Arrays.asList(new RTSPCommand[] {
                                commands.get(R.id.optionsBtn), commands.get(R.id.describeBtn), commands.get(R.id.setupBtn),
                                commands.get(R.id.playBtn), commands.get(R.id.pauseBtn), commands.get(R.id.recordBtn),
                                commands.get(R.id.getParameterBtn), commands.get(R.id.setParameterBtn),
                                commands.get(R.id.announceBtn), commands.get(R.id.teardownBtn)
                        })).execute();
                    }
                }).start();
            }
        });
    }

    private void setupTabHost() {
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

        tabHost.setup();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator("Result");
        tabSpec.setContent(R.id.resultLogsScrollView);
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator("Output");
        tabSpec.setContent(R.id.messageLogsScrollView);
        tabHost.addTab(tabSpec);
    }
}
