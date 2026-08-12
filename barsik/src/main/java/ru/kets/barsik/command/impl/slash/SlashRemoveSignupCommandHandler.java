package ru.kets.barsik.command.impl.slash;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.stereotype.Component;
import ru.kets.barsik.exception.EmbedCommandException;

@Component("remove")
public class SlashRemoveSignupCommandHandler extends AbstractSlashSignupCommandHandler {

    @Override
    MessageEmbed processCommand(SlashCommandInteractionEvent event) throws EmbedCommandException {
        OptionMapping roleOption = event.getOption("role");
        if (roleOption == null) {
            throw new EmbedCommandException("Please specify a role.");
        }
        return getSignupService().removeRole(event.getChannel(), roleOption.getAsString());
    }
}
