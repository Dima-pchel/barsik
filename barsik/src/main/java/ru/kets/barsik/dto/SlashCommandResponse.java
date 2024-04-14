package ru.kets.barsik.dto;

import net.dv8tion.jda.api.entities.MessageEmbed;

public class SlashCommandResponse {

    private String text;
    private MessageEmbed messageEmbed;

    public SlashCommandResponse(String text, MessageEmbed messageEmbed) {
        this.text = text;
        this.messageEmbed = messageEmbed;
    }

    public SlashCommandResponse(String text) {
        this.text = text;
    }

    public SlashCommandResponse(MessageEmbed messageEmbed) {
        this.messageEmbed = messageEmbed;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MessageEmbed getMessageEmbed() {
        return messageEmbed;
    }

    public void setMessageEmbed(MessageEmbed messageEmbed) {
        this.messageEmbed = messageEmbed;
    }
}
