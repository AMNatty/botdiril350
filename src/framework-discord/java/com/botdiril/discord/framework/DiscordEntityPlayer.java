package com.botdiril.discord.framework;

import com.botdiril.discord.userdata.DiscordUserInventory;
import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.sql.DBConnection;
import com.botdiril.userdata.UserInventory;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DiscordEntityPlayer extends EntityPlayer
{
    @Nullable
    protected final Member member;
    protected final User user;
    protected final long userID;

    public DiscordEntityPlayer(DBConnection db, @NotNull Member member)
    {
        super(db);

        this.member = member;
        var user = this.member.getUser();
        this.user = user;
        this.userID = user.getIdLong();
        this.avatarURL = user.getEffectiveAvatarUrl();
        this.mention = this.member.getAsMention();
        this.tag = user.getAsTag();
        this.name = user.getName();
    }

    public DiscordEntityPlayer(DBConnection db, User user)
    {
        super(db);

        this.member = null;
        this.user = user;
        this.userID = user.getIdLong();
        this.avatarURL = user.getEffectiveAvatarUrl();
        this.mention = user.getAsMention();
        this.tag = user.getAsTag();
        this.name = user.getName();
    }

    @Nullable
    public Member getMember()
    {
        return this.member;
    }

    public User getUser()
    {
        return this.user;
    }

    public long getUserID()
    {
        return this.userID;
    }

    @Override
    public UserInventory loadInventory()
    {
        return new DiscordUserInventory(this.db, this.userID);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof DiscordEntityPlayer dep)
        {
            return this.userID == dep.userID;
        }

        return super.equals(obj);
    }
}
