package ru.kets.barsik.command.impl.slash;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.kets.barsik.exception.EmbedCommandException;
import ru.kets.barsik.helper.CommandHelper;

@Component("add")
public class SlashAddSignupCommandHandler extends AbstractSlashSignupCommandHandler {

    @Override
    MessageEmbed processCommand(SlashCommandInteractionEvent event) throws EmbedCommandException {
        OptionMapping roleOption = event.getOption("role");
        if (roleOption == null) {
            throw new EmbedCommandException("Please specify a role.");
        }
        String member = event.getOption("member") != null ? event.getOption("member").getAsString() : StringUtils.EMPTY;
        String user = StringUtils.isNotEmpty(member) ? CommandHelper.extractUser(member) : event.getUser().getId();
        return getSignupService().addUser(event.getChannel(), user, roleOption.getAsString());
    }
}
