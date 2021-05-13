package com.botdiril.userdata.achievement;

import com.botdiril.userdata.icon.Icons;

public class Achievements
{
    public static final Achievement beginner = new Achievement("beginner", "Beginner", "Use the bot and get level 10.", Icons.ACHIEVEMENT_BOT);
    public static final Achievement experienced = new Achievement("experienced", "Experience", "Get level 40.", Icons.ACHIEVEMENT_BOT);
    public static final Achievement master = new Achievement("master", "Master", "Get level 150.", Icons.ACHIEVEMENT_BOT);
    public static final Achievement ascended = new Achievement("ascended", "Ascended", "Get level 500.", Icons.ACHIEVEMENT_BOT);
    public static final Achievement god = new Achievement("god", "Botdiril God", "Get level 2000.", Icons.ACHIEVEMENT_BOT);

    public static final Achievement beta = new Achievement("betatester", "Botdiril Beta Tester", "Be a part of the original beta test.", Icons.ACHIEVEMENT_BETA);
}
