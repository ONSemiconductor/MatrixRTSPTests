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

package com.onsemi.matrix.rtspclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
            this.commands = this.createCommands();

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
        SparseArray<RTSPCommand> cs = new SparseArray<>();

        RTSPClient client = new RTSPClient();
        client.setTransport(new PlainTCP());

        cs.put(R.id.describeBtn, new DescribeCommand(client, this.messageLogger, this.resultLogger, this.settings.getCameraURL()));
        cs.put(R.id.optionsBtn, new OptionsCommand(client, this.messageLogger, this.resultLogger, this.settings.getCameraURL()));
        cs.put(R.id.setupBtn, new SetupCommand(client, this.messageLogger, this.resultLogger, this.settings.getCameraURL(), 0));

        cs.put(R.id.playBtn, new PlayCommand(client, this.messageLogger, this.resultLogger));
        cs.put(R.id.pauseBtn, new PauseCommand(client, this.messageLogger, this.resultLogger));
        cs.put(R.id.recordBtn, new RecordCommand(client, this.messageLogger, this.resultLogger));
        cs.put(R.id.teardownBtn, new TeardownCommand(client, this.messageLogger, this.resultLogger));

        cs.put(R.id.announceBtn, new AnnounceCommand(client, this.messageLogger, this.resultLogger,
                this.getString(R.string.announce_command_description)));

        cs.put(R.id.getParameterBtn, new GetParameterCommand(client, this.messageLogger, this.resultLogger,
                this.settings.getGetParameterValue()));

        cs.put(R.id.setParameterBtn, new SetParameterCommand(client, this.messageLogger, this.resultLogger,
                this.settings.getSetParameterValue()));

        cs.put(R.id.runAllBtn, new RunAllCommand(client, this.messageLogger, this.resultLogger,
                this.settings.getCameraURL(), 0, this.settings.getGetParameterValue(),
                this.settings.getSetParameterValue(), this.getString(R.string.announce_command_description)));

        return cs;
    }
}
