package cz.tefek.botdiril.data;

import cz.tefek.botdiril.framework.command.Worker;

/**
 * General interface for any data object that is serialized.
 */
public interface IData
{
    /**
     * Deserialize the object, this should be called when the object is built.
     */
    public void deserialize() throws Exception;

    /**
     * Serialize the object. Object should be serialized when no {@link Worker} has
     * this object scheduled.
     */
    public void serialize() throws Exception;
}
