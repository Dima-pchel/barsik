package ru.kets.barsik.command.impl.slash;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.stereotype.Component;
import ru.kets.barsik.exception.EmbedCommandException;

@Component("note")
public class SlashNoteSignupCommandHandler extends AbstractSlashSignupCommandHandler {

    @Override
    MessageEmbed processCommand(SlashCommandInteractionEvent event) throws EmbedCommandException {
        OptionMapping noteOption = event.getOption("note");
        if (noteOption == null) {
            throw new EmbedCommandException("Please specify a note.");
        }
        return getSignupService().setNote(event.getChannel(), noteOption.getAsString());
    }
}
