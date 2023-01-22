package ru.kets.barsik.event.jda;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kets.barsik.command.SlashCommandHandler;
import ru.kets.barsik.dto.SlashCommandResponse;

import java.util.Map;

import static ru.kets.barsik.integrations.constant.Constants.ERROR_MESSAGE;

@Service
public class SlashCommandListener extends ListenerAdapter implements EventListener {

    @Autowired
    Map<String, SlashCommandHandler> slashCommandHandlerMap;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        try {
            SlashCommandHandler slashCommandHandler = slashCommandHandlerMap.get(event.getName());
            if (slashCommandHandler != null) {

                SlashCommandResponse result = slashCommandHandler.command(event);
                if (StringUtils.isNotBlank(result.getText())) {
                    event.reply(result.getText()).queue();
                } else if (result.getMessageEmbed() != null) {
                    event.replyEmbeds(result.getMessageEmbed()).queue();
                } else {
                    event.reply(event.getName()).queue();
                }
            } else {
                event.reply(String.format("Command %s not found", event.getName())).queue();
            }
        } catch (Exception e) {
            event.reply(ERROR_MESSAGE);
        }
    }

}
