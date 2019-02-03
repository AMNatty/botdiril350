package cz.tefek.botdiril.data;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

public interface IDataJson extends IData
{
    @Override
    public default void deserialize() throws Exception
    {
        var file = new File(this.getPath());
        var fr = new FileReader(file);
        var gson = new Gson();
        this.deserializeJSON(gson, fr);
        fr.close();
    }

    public default void deserializeJSON(Gson gson, Reader reader)
    {
        gson.fromJson(reader, getClass());
    }

    /**
     * Generate the path of this file.
     */
    public String getPath();

    @Override
    public default void serialize() throws Exception
    {
        var writer = new PrintWriter(this.getPath());
        var gson = new Gson();
        this.serializeJSON(gson, writer);
        writer.close();
    }

    public default void serializeJSON(Gson gson, Writer writer)
    {
        gson.toJson(this, writer);
    }
}
