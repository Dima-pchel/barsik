package ru.kets.barsik.service;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import ru.kets.barsik.exception.EmbedCommandException;

public interface SignupService {

    MessageEmbed getSignupByChannel(MessageChannelUnion channel) throws EmbedCommandException;

    MessageEmbed createSignup(MessageChannelUnion channel) throws EmbedCommandException;

    MessageEmbed addUser(MessageChannelUnion channel, User author, String right) throws EmbedCommandException;

    MessageEmbed setDate(MessageChannelUnion channel, String date) throws EmbedCommandException;

    MessageEmbed setNote(MessageChannelUnion channel, String note) throws EmbedCommandException;

    MessageEmbed setImage(MessageChannelUnion channel, String imgUrl) throws EmbedCommandException;
}
