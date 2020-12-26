package cz.tefek.botdiril.gamelogic.card;

public class TrainResult
{
    private final EnumTrainResult outcome;
    private final long xpLearnt;

    public TrainResult(EnumTrainResult outcome, long xpLearnt)
    {
        this.outcome = outcome;
        this.xpLearnt = xpLearnt;
    }

    public EnumTrainResult getOutcome()
    {
        return this.outcome;
    }

    public long getXPLearnt()
    {
        return this.xpLearnt;
    }
}
