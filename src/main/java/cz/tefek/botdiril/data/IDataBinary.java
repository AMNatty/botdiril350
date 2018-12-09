package cz.tefek.botdiril.data;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public interface IDataBinary extends IData
{
    @Override
    public default void deserialize() throws Exception
    {
        var file = new File(this.getPath());
        var fis = new FileInputStream(file);
        var mpf = new MessagePackFactory();
        var om = new ObjectMapper(mpf);
        om.readerFor(getClass()).withValueToUpdate(this).readValue(fis);
        fis.close();
    }

    public String getPath();

    @Override
    public default void serialize() throws Exception
    {
        var file = new File(this.getPath());
        var fos = new FileOutputStream(file);
        var mpf = new MessagePackFactory();
        var om = new ObjectMapper(mpf);
        om.writeValue(fos, this);
        fos.close();
    }
}
