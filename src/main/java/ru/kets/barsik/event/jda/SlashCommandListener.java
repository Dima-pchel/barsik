package ru.kets.barsik.event.jda;

import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kets.barsik.command.EmbedCommandHandler;
import ru.kets.barsik.command.SlashCommandHandler;
import ru.kets.barsik.configuration.JDABotConfiguration;
import ru.kets.barsik.dto.SlashCommandResponse;

import javax.annotation.Resource;

import java.util.Map;

import static ru.kets.barsik.integrations.constant.Constants.ERROR_MESSAGE;
import static ru.kets.barsik.integrations.constant.Constants.HELP_COLOR;

@Service
public class SlashCommandListener extends ListenerAdapter implements EventListener {
    private static final String WORLD_DOMINATION_CAT = "https://downloader.disk.yandex.ru/preview/97c8ae775829700ed4af809f3e576ebd9677c9e9e2b8577e666a8fcfbf2e1a91/63bbd40f/foCp0qlbq3QY5tfXBiqVJPtmKg9Tojpzor6YuEtozRQi_mQooppYrho6sadwU2z3iNZv5h3f7YPJMRh0h6s9xA%3D%3D?uid=0&filename=cat.png&disposition=inline&hash=&limit=0&content_type=image%2Fpng&owner_uid=0&tknv=v2&size=2048x2048";

    @Resource
    private JDABotConfiguration botConfiguration;

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
