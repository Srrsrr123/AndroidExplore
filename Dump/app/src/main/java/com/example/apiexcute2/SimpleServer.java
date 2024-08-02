package com.example.apiexcute2;

import fi.iki.elonen.NanoHTTPD;

public class SimpleServer extends NanoHTTPD {

    public SimpleServer(int port) {
        super(port);
    }

    @Override
    public NanoHTTPD.Response serve(IHTTPSession session) {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html><html><body>");
        builder.append("<p>Hello! This Android HTTP Server.</p>");
        builder.append("<h1>:)</h1></body></html>\n");
        return newFixedLengthResponse(builder.toString());
    }
}
