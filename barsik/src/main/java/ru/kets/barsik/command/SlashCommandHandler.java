package ru.kets.barsik.command;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import ru.kets.barsik.dto.SlashCommandResponse;

public interface SlashCommandHandler {

    SlashCommandResponse command(SlashCommandInteractionEvent event);

}
