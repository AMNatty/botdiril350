package com.botdiril.framework.command.context;

import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.response.IResponse;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.framework.sql.DBConnection;
import com.botdiril.userdata.UserInventory;
import com.botdiril.userdata.properties.PropertyObject;
import org.intellij.lang.annotations.PrintFormat;

public abstract class CommandContext
{
    public DBConnection db;
    public UserInventory inventory;
    public PropertyObject userProperties;
    public EntityPlayer player;

    public EntityPlayer botPlayer;
    public String botIconURL;

    public IResponse response;
    protected boolean shouldSend = false;

    public void clearResponse()
    {
        this.shouldSend = false;
        this.response = this.createResponse();
    }

    public void respondf(@PrintFormat String msg, Object... objects)
    {
        this.response.addText(msg.formatted(objects));
        this.shouldSend = true;
    }

    public void respond(String msg)
    {
        this.response.addText(msg);
        this.shouldSend = true;
    }

    public void respond(ResponseEmbed embed)
    {
        this.response.setEmbed(embed);
        this.shouldSend = true;
    }

    public void send()
    {
        if (!this.shouldSend)
            return;

        var defaultResponse = this.getDefaultResponse();
        defaultResponse.send();

        this.clearResponse();
    }

    public abstract IResponse getDefaultResponse();

    public abstract IResponse createResponse();
}
