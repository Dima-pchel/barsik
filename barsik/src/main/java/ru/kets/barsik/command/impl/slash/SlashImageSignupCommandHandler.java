package ru.kets.barsik.command.impl.slash;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.stereotype.Component;
import ru.kets.barsik.exception.EmbedCommandException;

@Component("image")
public class SlashImageSignupCommandHandler extends AbstractSlashSignupCommandHandler {

    @Override
    MessageEmbed processCommand(SlashCommandInteractionEvent event) throws EmbedCommandException {
        OptionMapping imageOption = event.getOption("image");
        if (imageOption == null) {
            throw new EmbedCommandException("Please specify an image link.");
        }
        return getSignupService().setImage(event.getChannel(), imageOption.getAsString());
    }
}
