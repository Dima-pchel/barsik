package ru.kets.barsik.command.impl.slash;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.stereotype.Component;
import ru.kets.barsik.exception.EmbedCommandException;

@Component("thumbnail")
public class SlashThumbnailSignupCommandHandler extends AbstractSlashSignupCommandHandler {

    @Override
    MessageEmbed processCommand(SlashCommandInteractionEvent event) throws EmbedCommandException {
        OptionMapping thumbnailOption = event.getOption("thumbnail");
        if (thumbnailOption == null) {
            throw new EmbedCommandException("Please specify a thumbnail link.");
        }
        return getSignupService().setThumbnail(event.getChannel(), thumbnailOption.getAsString());
    }
}
