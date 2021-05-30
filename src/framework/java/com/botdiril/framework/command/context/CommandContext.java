package com.botdiril.framework.command.context;

import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.response.IResponse;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.framework.sql.DBConnection;
import com.botdiril.userdata.UserInventory;
import com.botdiril.userdata.properties.PropertyObject;
import com.google.errorprone.annotations.FormatMethod;
import com.google.errorprone.annotations.FormatString;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.Random;

public abstract class CommandContext
{
    public DBConnection db;
    public UserInventory inventory;
    public PropertyObject userProperties;
    public EntityPlayer player;

    public Random random;
    public RandomDataGenerator rdg;

    public EntityPlayer botPlayer;
    public String botIconURL;

    public IResponse response;
    protected boolean shouldSend = false;

    public void clearResponse()
    {
        this.shouldSend = false;
        this.response = this.createResponse();
    }

    @FormatMethod
    public void respondf(@FormatString String msg, Object... objects)
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

    public IResponse getDefaultResponse()
    {
        return this.response;
    }

    public abstract IResponse createResponse();
}
