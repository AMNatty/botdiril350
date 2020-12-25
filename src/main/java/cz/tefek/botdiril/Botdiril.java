package cz.tefek.botdiril;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.Set;

public class Botdiril
{
    public static final long AUTHOR_ID = 263648016982867969L;
    public static final String AUTHOR = "<@" + AUTHOR_ID + ">";
    public static final String BRANDING = "Botdiril";
    private static final String PLAYING = "www.tefek.cz";

    private final EventBus eventBus;
    private final ShardManager shardManager;

    public Botdiril()
    {
        try
        {
            MessageAction.setDefaultMentions(Set.of());

            var jdaBuilder = DefaultShardManagerBuilder.createDefault(BotMain.config.getApiKey(),
                GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_EMOJIS);
            jdaBuilder.addEventListeners(this.eventBus = new EventBus());
            jdaBuilder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
            jdaBuilder.setActivity(Activity.listening(PLAYING));
            this.shardManager = jdaBuilder.build();
        }
        catch (LoginException e)
        {
            throw new MajorFailureException("An exception has occurred while setting up JDA.", e);
        }
    }

    public ShardManager getShardManager()
    {
        return this.shardManager;
    }

    public EventBus getEventBus()
    {
        return this.eventBus;
    }
}
