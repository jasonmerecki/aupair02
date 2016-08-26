package com.jkmcllc.aupair01.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class RequestHandler extends AbstractHandler {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        out.println("<h1>WTF</h1>");
        
        baseRequest.setHandled(true);
    }

}
