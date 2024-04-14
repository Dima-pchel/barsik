package ru.kets.barsik.command.impl.slash;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import ru.kets.barsik.command.SlashCommandHandler;
import ru.kets.barsik.dto.SlashCommandResponse;
import ru.kets.barsik.exception.EmbedCommandException;
import ru.kets.barsik.service.SignupService;

import javax.annotation.Resource;

public abstract class AbstractSlashSignupCommandHandler implements SlashCommandHandler {

    @Resource
    private SignupService signupService;

    @Override
    public SlashCommandResponse command(SlashCommandInteractionEvent event) {
        MessageEmbed messageEmbed;
        try {
            messageEmbed = processCommand(event);
        } catch (EmbedCommandException e) {
            return new SlashCommandResponse(e.getMessage());
        }
        return new SlashCommandResponse(messageEmbed);
    }

    protected SignupService getSignupService() {
        return signupService;
    }

    abstract MessageEmbed processCommand(SlashCommandInteractionEvent event) throws EmbedCommandException;
}
