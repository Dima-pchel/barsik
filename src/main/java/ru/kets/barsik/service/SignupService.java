package ru.kets.barsik.service;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import ru.kets.barsik.exception.EmbedCommandException;

public interface SignupService {

    MessageEmbed getSignupByChannel(MessageChannelUnion channel) throws EmbedCommandException;

    MessageEmbed createSignup(MessageChannelUnion channel, String name) throws EmbedCommandException;

    MessageEmbed saveSignupPattern(MessageChannelUnion channel, String name) throws EmbedCommandException;

    MessageEmbed addUser(MessageChannelUnion channel, User author, String right) throws EmbedCommandException;

    MessageEmbed setDate(MessageChannelUnion channel, String date) throws EmbedCommandException;

    MessageEmbed setNote(MessageChannelUnion channel, String note) throws EmbedCommandException;

    MessageEmbed setImage(MessageChannelUnion channel, String imgUrl) throws EmbedCommandException;

    MessageEmbed setThumbnail(MessageChannelUnion channel, String imgUrl) throws EmbedCommandException;

    MessageEmbed addRole(MessageChannelUnion channel, String roleName) throws EmbedCommandException;

    MessageEmbed removeRole(MessageChannelUnion channel, String role) throws EmbedCommandException;

    MessageEmbed removeUser(MessageChannelUnion channel, User author) throws EmbedCommandException;
}
