package ru.kets.barsik.command.impl.slash;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.kets.barsik.exception.EmbedCommandException;

@Component("create")
public class SlashCreateSignupCommandHandler extends AbstractSlashSignupCommandHandler {

    @Override
    MessageEmbed processCommand(SlashCommandInteractionEvent event) throws EmbedCommandException {
        String name = event.getOption("name") != null ? event.getOption("name").getAsString() : StringUtils.EMPTY;
        return getSignupService().createSignup(event.getChannel(), name);
    }
}
