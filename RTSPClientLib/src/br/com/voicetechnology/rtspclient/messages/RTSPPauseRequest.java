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

package br.com.voicetechnology.rtspclient.messages;

import java.net.URISyntaxException;

import br.com.voicetechnology.rtspclient.MissingHeaderException;
import br.com.voicetechnology.rtspclient.RTSPRequest;
import br.com.voicetechnology.rtspclient.headers.SessionHeader;

public class RTSPPauseRequest extends RTSPRequest {
    public RTSPPauseRequest() {}

    public RTSPPauseRequest(String messageLine) throws URISyntaxException
    {
        super(messageLine);
    }

    @Override
    public byte[] getBytes() throws MissingHeaderException
    {
        getHeader(SessionHeader.NAME);
        return super.getBytes();
    }
}
