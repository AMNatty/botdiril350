package com.botdiril.framework.response;

import org.jetbrains.annotations.Nullable;

public interface IResponse
{
    IResponse setText(@Nullable String text);

    IResponse addText(String text);

    IResponse setEmbed(@Nullable ResponseEmbed text);

    void send();
}
