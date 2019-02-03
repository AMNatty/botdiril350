package cz.tefek.botdiril.servlet;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

import cz.tefek.botdiril.config.BotConfig;

public class ServletController
{
    private Server server;
    private int port;

    public ServletController(BotConfig config)
    {
        this.server = new Server();
        // Set to the port specified in the config file
        this.port = config.getPort();

        ResourceHandler resourceHandler = new ResourceHandler();

        // No directory listing
        resourceHandler.setDirectoriesListed(false);
        // index.html will be our welcome file
        resourceHandler.setWelcomeFiles(new String[] { "index.html" });
        // Base folder
        resourceHandler.setResourceBase("assets/webi/");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resourceHandler, new DefaultHandler() });
        server.setHandler(handlers);
    }

    public void start() throws Exception
    {
        ServerConnector connector = new ServerConnector(this.server);
        connector.setPort(port);

        server.setConnectors(new Connector[] { connector });

        server.start();
    }

    public void stop() throws Exception
    {
        // Stop the server
        server.stop();
        // Wait for the thread to join
        server.join();
    }
}
