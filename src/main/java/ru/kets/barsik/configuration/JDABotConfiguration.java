package ru.kets.barsik.configuration;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class JDABotConfiguration {

    @Value("${token}")
    private String token;

    private JDA jda;

    @Bean
    public JDA configureBot(List<EventListener> listeners) {
        JDABuilder builder = JDABuilder.createDefault(token);

        // Disable parts of the cache
//        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        // Enable the bulk delete event
        builder.setBulkDeleteSplittingEnabled(false);
        // Enable cache all guilds members
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        // Disable compression (not recommended)
//        builder.setCompression(Compression.NONE);
        // Set activity (like "playing Something")
        builder.setActivity(Activity.watching("тебе в душу"));
        // need for correct working member cache policy
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT);

        JDA build = builder.build();

        for (EventListener jdaEventListener : listeners) {
            build.addEventListener(jdaEventListener);
        }

        setJda(build);
        return build;
    }

    public JDA getJda() {
        return jda;
    }

    public void setJda(JDA jda) {
        this.jda = jda;
    }
}
