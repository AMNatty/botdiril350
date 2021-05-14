package com.botdiril.framework.response;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ResponseEmbed
{
    @Nullable
    private String title;

    @Nullable
    private StringBuilder description;
    @Nullable
    private String url;

    @Nullable
    private Integer color;

    @Nullable
    private String authorText;
    @Nullable
    private String authorURL;
    @Nullable
    private String authorIconURL;


    @Nullable
    private String footerText;
    @Nullable
    private String footerIconURL;
    @Nullable
    private ZonedDateTime footerTimestamp;

    @Nullable
    private String thumbnailURL;

    @Nullable
    private String imageURL;

    @NotNull
    private final List<ResponseEmbedField> fields = new ArrayList<>();


    public @Nullable String getTitle()
    {
        return this.title;
    }

    public ResponseEmbed setTitle(@Nullable String title)
    {
        this.title = title;

        return this;
    }

    public @Nullable String getDescription()
    {
        if (this.description == null)
            return null;

        return this.description.toString();
    }

    public ResponseEmbed setDescription(@Nullable String description)
    {
        if (description == null)
        {
            this.description = null;
            return this;
        }

        this.description = new StringBuilder(description);

        return this;
    }

    public ResponseEmbed appendDescription(@NotNull String description)
    {
        if (this.description == null)
        {
            this.description = new StringBuilder(description);
        }

        this.description.append(description);

        return this;
    }

    public @Nullable String getURL()
    {
        return this.url;
    }

    public ResponseEmbed setURL(@Nullable String url)
    {
        this.url = url;

        return this;
    }

    public @Nullable Integer getColor()
    {
        return this.color;
    }

    public ResponseEmbed setColor(@Nullable Integer color)
    {
        this.color = color;

        return this;
    }

    public ResponseEmbed setAuthor(@Nullable String authorText)
    {
        this.authorText = authorText;

        return this;
    }

    public ResponseEmbed setAuthor(@Nullable String authorText, @Nullable String authorURL, @Nullable String authorIconURL)
    {
        this.authorText = authorText;
        this.authorURL = authorURL;
        this.authorIconURL = authorIconURL;

        return this;
    }

    public @Nullable String getAuthorText()
    {
        return this.authorText;
    }

    public ResponseEmbed setAuthorText(@Nullable String authorText)
    {
        this.authorText = authorText;

        return this;
    }

    public @Nullable String getAuthorURL()
    {
        return this.authorURL;
    }

    public ResponseEmbed setAuthorURL(@Nullable String authorURL)
    {
        this.authorURL = authorURL;

        return this;
    }

    public @Nullable String getAuthorIconURL()
    {
        return this.authorIconURL;
    }

    public ResponseEmbed setAuthorIconURL(@Nullable String authorIconURL)
    {
        this.authorIconURL = authorIconURL;

        return this;
    }

    public ResponseEmbed setFooter(@Nullable String footerText, @Nullable String footerIconURL)
    {
        this.footerText = footerText;
        this.footerIconURL = footerIconURL;

        return this;
    }

    public ResponseEmbed setFooter(@Nullable String footerText)
    {
        this.footerText = footerText;

        return this;
    }

    public ResponseEmbed setFooter(@Nullable String footerText, @Nullable String footerIconURL, @Nullable ZonedDateTime footerTimestamp)
    {
        this.footerText = footerText;
        this.footerIconURL = footerIconURL;
        this.footerTimestamp = footerTimestamp;

        return this;
    }

    public @Nullable String getFooterText()
    {
        return this.footerText;
    }

    public ResponseEmbed setFooterText(@Nullable String footerText)
    {
        this.footerText = footerText;

        return this;
    }

    public @Nullable String getFooterIconURL()
    {
        return this.footerIconURL;
    }

    public ResponseEmbed setFooterIconURL(@Nullable String footerIconURL)
    {
        this.footerIconURL = footerIconURL;

        return this;
    }

    public @Nullable ZonedDateTime getFooterTimestamp()
    {
        return this.footerTimestamp;
    }

    public ResponseEmbed setFooterTimestamp(@Nullable ZonedDateTime footerTimestamp)
    {
        this.footerTimestamp = footerTimestamp;

        return this;
    }

    public @Nullable String getThumbnailURL()
    {
        return this.thumbnailURL;
    }

    public ResponseEmbed setThumbnailURL(@Nullable String thumbnailURL)
    {
        this.thumbnailURL = thumbnailURL;

        return this;
    }

    public ResponseEmbed setThumbnail(@Nullable String thumbnailURL)
    {
        return this.setThumbnailURL(thumbnailURL);
    }

    public @Nullable String getImageURL()
    {
        return this.imageURL;
    }

    public ResponseEmbed setImageURL(@Nullable String imageURL)
    {
        this.imageURL = imageURL;

        return this;
    }

    public @NotNull List<ResponseEmbedField> getFields()
    {
        return Collections.unmodifiableList(this.fields);
    }

    public ResponseEmbed addField(ResponseEmbedField field)
    {
        this.fields.add(field);

        return this;
    }

    public ResponseEmbed addField(String name, String value, boolean inline)
    {
        this.fields.add(new ResponseEmbedField(name, value, inline));

        return this;
    }

    public void forEachField(Consumer<ResponseEmbedField> fieldConsumer)
    {
        this.fields.forEach(fieldConsumer);
    }

    public Stream<ResponseEmbedField> streamFields()
    {
        return this.fields.stream();
    }

    public ResponseEmbed clearFields()
    {
        this.fields.clear();

        return this;
    }

    public record ResponseEmbedField(String name, String value, boolean inline)
    {

    }
}
