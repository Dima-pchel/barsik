package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.EmbedCommandHandler;
import ru.kets.barsik.exception.EmbedCommandException;

import java.util.ArrayList;
import java.util.List;

import static ru.kets.barsik.constant.Constants.CommandName.HELP_COMMAND_NAME;
import static ru.kets.barsik.constant.Constants.HELP_COLOR;

@Component(HELP_COMMAND_NAME)
public class HelpCommandHandler implements EmbedCommandHandler {

    private static final String WORLD_DOMINATION_CAT = "https://image.spreadshirtmedia.com/image-server/v1/compositions/T210A2PA4301PT17X11Y14D1021402054W30965H37158/views/1,width=550,height=550,appearanceId=2,backgroundColor=000000,noPt=true/cat-world-domination-kitty-kitten-funny-gift-mens-t-shirt.jpg";

    @Override
    public MessageEmbed command(Message eventMessage) throws EmbedCommandException {

        List<MessageEmbed.Field> fieldList = new ArrayList<>();

        fieldList.add(new MessageEmbed.Field("answer {question})", "- return yes or no to your question", false, false));
        fieldList.add(new MessageEmbed.Field("avatar {tag user}", "- return avatar image", false, false));
        fieldList.add(new MessageEmbed.Field("ban {tag user}", "- ban selected user. Be careful! I can ban you for this action!  ", false, false));
        fieldList.add(new MessageEmbed.Field("choose {option1},{option2}, ... ,{optionN}", "- Return 1 from options. Use comma for separate options", false, false));
        fieldList.add(new MessageEmbed.Field("cat", "- return random cat image.", false, false));
        fieldList.add(new MessageEmbed.Field("fox", "- return random fox image.", false, false));
        fieldList.add(new MessageEmbed.Field("friend", "- return random dog image.", false, false));
        fieldList.add(new MessageEmbed.Field("again", "- return result from your last command.", false, false));
        fieldList.add(new MessageEmbed.Field("say {phrase}", "- return phrase.. or not.", false, false));
        fieldList.add(new MessageEmbed.Field("meow", "- i'm a cat i can meow.", false, false));
        fieldList.add(new MessageEmbed.Field("report {tag user}", "- user will be reported.", false, false));
        fieldList.add(new MessageEmbed.Field("signup help", "- return signup command list", false, false));
        fieldList.add(new MessageEmbed.Field("speak", "- just speak.", false, false));
        fieldList.add(new MessageEmbed.Field("abuseadd {words}", "- add words to abuse validation. Use comma to separate words", false, false));
        fieldList.add(new MessageEmbed.Field("banreason {reason}", "- add new reason for ban to ban command. %s in reason will be replaced to banned user tag", false, false));
        fieldList.add(new MessageEmbed.Field("game create {message} ", "- create new roulette game", false, false));
        fieldList.add(new MessageEmbed.Field("play", "- shoot in roulette game", false, false));

        MessageEmbed.ImageInfo img = new MessageEmbed.ImageInfo(WORLD_DOMINATION_CAT, WORLD_DOMINATION_CAT, 200, 200);


        return new MessageEmbed(null, "Cat Helper",
                null, EmbedType.RICH, null, HELP_COLOR,
                null, null, null, null, null, img, fieldList);
    }

}
