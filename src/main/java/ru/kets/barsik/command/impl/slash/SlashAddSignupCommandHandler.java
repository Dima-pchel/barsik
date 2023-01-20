package ru.kets.barsik.command.impl.slash;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.kets.barsik.exception.EmbedCommandException;
import ru.kets.barsik.helper.CommandHelper;

@Component("add")
public class SlashAddSignupCommandHandler extends AbstractSlashSignupCommandHandler {

    @Override
    MessageEmbed processCommand(SlashCommandInteractionEvent event) throws EmbedCommandException {
        String member = event.getOption("member") != null ? event.getOption("member").getAsString() : StringUtils.EMPTY;
        String user = StringUtils.isNotEmpty(member) ? CommandHelper.extractUser(member) : event.getUser().getId();
        return getSignupService().addUser(event.getChannel(), user, event.getOption("role").getAsString());
    }
}
