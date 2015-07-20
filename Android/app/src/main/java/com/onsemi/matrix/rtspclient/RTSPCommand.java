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

import java.util.Observable;
import java.util.Observer;

import br.com.voicetechnology.rtspclient.RTSPClient;
import br.com.voicetechnology.rtspclient.concepts.Client;
import br.com.voicetechnology.rtspclient.concepts.ClientListener;
import br.com.voicetechnology.rtspclient.concepts.Content;
import br.com.voicetechnology.rtspclient.concepts.EntityMessage;
import br.com.voicetechnology.rtspclient.concepts.Request;
import br.com.voicetechnology.rtspclient.concepts.Response;

public class RTSPCommand extends Observable implements ClientListener {
    protected RTSPClient client = null;

    protected MessageLogger messageLogger = null;
    protected ResultLogger resultLogger = null;

    protected Observer requestFinishedObserver = null;

    public RTSPCommand(RTSPClient client, MessageLogger mLogger, ResultLogger rLogger) {
        this.client = client;

        this.messageLogger = mLogger;
        this.resultLogger = rLogger;
    }

    public void execute() {
        this.client.setClientListener(this);
    }

    public RTSPClient getClient() {
        return this.client;
    }

    public void setRequestFinishedObserver(Observer requestFinishedObserver) {
        this.requestFinishedObserver = requestFinishedObserver;
    }

    protected void notifyRequestFinishedObserver() {
        if (this.requestFinishedObserver != null) {
            requestFinishedObserver.update(this, null);
        }
    }

    @Override
    public void mediaDescriptor(Client client, String descriptor) { }

    @Override
    public void generalError(Client client, Throwable error) {
        error.printStackTrace();
    }

    @Override
    public void requestFailed(Client client, Request request, Throwable cause) {
        this.messageLogger.info(String.format("Error:\n%s", cause));
    }

    @Override
    public void response(Client client, Request request, Response response) {
        this.print(request, response);

        this.resultLogger.info(this.verify(request, response));

        this.notifyRequestFinishedObserver();
    }

    public Info verify(Request request, Response response) {
        if (response.getStatusCode() == 200) {
            return new Info(request.getMethod().toString(), true);
        }

        return new Info(request.getMethod().toString(), false,
                String.format("Expected status: '%d OK' Actual status: '%d %s'",
                        200, response.getStatusCode(), response.getStatusText()));
    }

    protected void print(Request request, Response response) {
        this.messageLogger.info(String.format("Request:\n%s", request));

        print(request.getEntityMessage());

        this.messageLogger.info(String.format("Response:\n%s", response));

        print(response.getEntityMessage());
    }

    private void print(EntityMessage entityMessage) {
        if (entityMessage != null) {
            Content content = entityMessage.getContent();

            this.messageLogger.info(String.format("%s\n\n", new String(content.getBytes())));
        }
    }
}
