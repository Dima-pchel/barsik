package ru.kets.barsik.command.impl.slash;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kets.barsik.command.SlashCommandHandler;
import ru.kets.barsik.dto.SlashCommandResponse;
import ru.kets.barsik.exception.EmbedCommandException;
import ru.kets.barsik.service.SignupService;

import javax.annotation.Resource;

import static ru.kets.barsik.constant.Constants.ERROR_MESSAGE;

public abstract class AbstractSlashSignupCommandHandler implements SlashCommandHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSlashSignupCommandHandler.class);

    @Resource
    private SignupService signupService;

    @Override
    public SlashCommandResponse command(SlashCommandInteractionEvent event) {
        MessageEmbed messageEmbed;
        try {
            messageEmbed = processCommand(event);
        } catch (EmbedCommandException e) {
            return new SlashCommandResponse(e.getMessage());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return new SlashCommandResponse(ERROR_MESSAGE);
        }
        return new SlashCommandResponse(messageEmbed);
    }

    protected SignupService getSignupService() {
        return signupService;
    }

    abstract MessageEmbed processCommand(SlashCommandInteractionEvent event) throws EmbedCommandException;
}
