package cz.tefek.botdiril.servlet;

import org.eclipse.jetty.io.WriterOutputStream;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.servlet.ServletHandler;

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IntroPageHandler extends ServletHandler
{
    @Override
    public void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        response.setStatus(HttpServletResponse.SC_OK);

        try (var fis = new FileInputStream("assets/servlet/login.html"))
        {
            fis.transferTo(new WriterOutputStream(response.getWriter()));
        }

        baseRequest.setHandled(true);
    }
}
