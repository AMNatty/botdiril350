package cz.tefek.botdiril.data;

public interface IDataBinary extends IData
{
    @Override
    public default void deserialize() throws Exception
    {
        /*
        var file = new File(this.getPath());
        var fis = new FileInputStream(file);
        var mpf = new MessagePackFactory();
        var om = new ObjectMapper(mpf);
        om.readerFor(getClass()).readValue(fis, getClass());
        fis.close();
        */
    }

    public String getPath();

    @Override
    public default void serialize() throws Exception
    {
        /*
        var writer = new PrintWriter(this.getPath());
        var gson = new Gson();
        this.serializeBinary(gson, writer);
        writer.close();
        */
    }
}
