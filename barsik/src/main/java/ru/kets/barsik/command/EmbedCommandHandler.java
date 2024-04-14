package ru.kets.barsik.command;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import ru.kets.barsik.exception.EmbedCommandException;

public interface EmbedCommandHandler {

    MessageEmbed command(Message eventMessage) throws EmbedCommandException;

}
