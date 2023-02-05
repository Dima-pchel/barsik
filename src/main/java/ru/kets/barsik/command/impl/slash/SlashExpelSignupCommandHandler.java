package ru.kets.barsik.command.impl.slash;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;
import ru.kets.barsik.exception.EmbedCommandException;
import ru.kets.barsik.helper.CommandHelper;

@Component("expel")
public class SlashExpelSignupCommandHandler extends AbstractSlashSignupCommandHandler {

    @Override
    MessageEmbed processCommand(SlashCommandInteractionEvent event) throws EmbedCommandException {
        String role = event.getOption("role") != null ? event.getOption("role").toString() : null;
        String user = event.getOption("user").getAsString();
        return getSignupService().removeUser(event.getChannel(), CommandHelper.extractUser(user), role);
    }
}
