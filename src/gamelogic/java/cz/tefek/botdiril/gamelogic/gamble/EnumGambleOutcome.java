package cz.tefek.botdiril.gamelogic.gamble;

import cz.tefek.botdiril.gamelogic.weighted.IWeightedRandom;
import cz.tefek.botdiril.userdata.icon.Icons;

public enum EnumGambleOutcome implements IWeightedRandom<EnumGambleOutcome>
{
    LOSE_EVERYTHING("Lose everything",
        "You lost everything - **%s** " + Icons.KEK + "... But don't worry! Some of those keks get stored in your jackpot pool!",
        6.8,
        gi -> GambleResult.of(-gi.gambledKeks())),

    LOSE_THREE_QUARTERS("Lose 75%",
        "You lost three quarters of your bet. You **lost %s** " + Icons.KEK + ". Pretty unlucky. (-75%%)",
        15,
        gi -> GambleResult.of(Math.round(-gi.gambledKeks() * 0.75))),

    LOSE_HALF("Lose 50%",
        "You lost half of your bet. You **lost %s** " + Icons.KEK + ". (-50%%)",
        30,
        gi -> GambleResult.of(Math.round(-gi.gambledKeks() * 0.5))),

    LOSE_QUARTER("Lose 25%",
        "You **lost %s** " + Icons.KEK + ". (-25%%)",
        72,
        gi -> GambleResult.of(Math.round(-gi.gambledKeks() * 0.25))),

    WIN_THIRD("Win 33%",
        "You **win %s** " + Icons.KEK + ". (+33%%)",
        40,
        gi -> GambleResult.of(Math.round(gi.gambledKeks() * 0.33))),

    WIN_HALF("Win 50%",
        "You **win %s** " + Icons.KEK + ". (+50%%)",
        22,
        gi -> GambleResult.of(Math.round(gi.gambledKeks() * 0.5))),

    WIN_DOUBLE("Win 100%",
        "You doubled your bet! **+%s** " + Icons.KEK + ". (+100%%)",
        8,
        gi -> GambleResult.of(gi.gambledKeks())),

    WIN_TRIPLE("Win 200%",
        "POGKEK. You **win %s** " + Icons.KEK + ". (+200%%)",
        5,
        gi -> GambleResult.of(gi.gambledKeks() * 2)),

    WIN_QUADRUPLE("Win 300%!",
        Icons.OTHER_KEKOVERDRIVE + " You win four times your original bet! " + Icons.OTHER_THEFAST + " **+%s** " + Icons.KEK + " (+300%%).",
        1,
        gi -> GambleResult.of(gi.gambledKeks() * 3)),

    JACKPOT("JACKPOT!!!",
        Icons.OTHER_KEKOVERDRIVE + " YOU WIN THE JACKPOT! **+%s** " + Icons.KEK + ".",
        0.2,
        gi -> GambleResult.of(gi.jackpotPool()));

    private final String shortName;
    private final String text;
    private final double weight;
    private final GambleFunction calc;

    EnumGambleOutcome(String shortName, String fullName, double weight, GambleFunction calc)
    {
        this.shortName = shortName;
        this.text = fullName;
        this.weight = weight;
        this.calc = calc;
    }

    public GambleResult apply(GambleInput gambleInput)
    {
        var gambleResult = this.calc.gambleModifier(gambleInput);
        gambleResult.setOutcome(this);
        return gambleResult;
    }

    public String getShortName()
    {
        return this.shortName;
    }

    public String getText()
    {
        return this.text;
    }

    public double getWeight()
    {
        return this.weight;
    }
}
