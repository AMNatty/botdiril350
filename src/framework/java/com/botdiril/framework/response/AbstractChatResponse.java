package com.botdiril.framework.response;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class AbstractChatResponse implements IResponse
{
    @Nullable
    protected ResponseEmbed embed;

    @NotNull
    protected StringBuilder message;

    protected AbstractChatResponse()
    {
        this.message = new StringBuilder();
    }

    @Override
    public AbstractChatResponse setEmbed(@Nullable ResponseEmbed embed)
    {
        this.embed = embed;

        return this;
    }

    public AbstractChatResponse setText(@Nullable String text)
    {
        this.message = new StringBuilder(Objects.requireNonNullElse(text, ""));

        return this;
    }

    public AbstractChatResponse addText(String text)
    {
        this.message.append('\n');
        this.message.append(text);

        return this;
    }
}
