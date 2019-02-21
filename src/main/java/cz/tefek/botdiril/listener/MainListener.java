package cz.tefek.botdiril.listener;

import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import cz.tefek.botdiril.BotMain;

public class MainListener extends ListenerAdapter
{
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event)
    {
        var guild = event.getGuild();
        var gc = BotMain.bot.getGuildConfig(guild.getIdLong());
        var messageContentsRaw = event.getMessage().getContentRaw().toLowerCase();

        var prefix = gc.getPrefix();
        var globalPrefix = BotMain.bot.getConfig().getGlobalPrefix();

        if (prefix != null)
        {
            if (messageContentsRaw.startsWith(prefix))
            {

            }
        }
        else if (messageContentsRaw.startsWith(globalPrefix))
        {

        }
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event)
    {

    }

    @Override
    public void onReady(ReadyEvent event)
    {
        var thisJDA = event.getJDA();
        var shard = thisJDA.getShardInfo();
        var shardsTotal = BotMain.bot.getShardManager().getShardsTotal();

        BotMain.logger.info(String.format("Shard %d is ready.", shard.getShardId(), shardsTotal));
    }
}
