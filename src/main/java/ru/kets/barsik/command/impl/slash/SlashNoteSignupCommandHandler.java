package ru.kets.barsik.command.impl.slash;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;
import ru.kets.barsik.exception.EmbedCommandException;

@Component("note")
public class SlashNoteSignupCommandHandler extends AbstractSlashSignupCommandHandler {

    @Override
    MessageEmbed processCommand(SlashCommandInteractionEvent event) throws EmbedCommandException {
        return getSignupService().setNote(event.getChannel(), event.getOption("note").getAsString());
    }
}
