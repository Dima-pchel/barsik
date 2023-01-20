package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.EmbedCommandHandler;
import ru.kets.barsik.exception.EmbedCommandException;

import java.util.ArrayList;
import java.util.List;

import static ru.kets.barsik.integrations.constant.Constants.HELP_COLOR;

@Component("help")
public class HelpCommandHandler implements EmbedCommandHandler {

    private static final String COMMAND_NAME = "help";
    private static final String WORLD_DOMINATION_CAT = "https://downloader.disk.yandex.ru/preview/97c8ae775829700ed4af809f3e576ebd9677c9e9e2b8577e666a8fcfbf2e1a91/63bbd40f/foCp0qlbq3QY5tfXBiqVJPtmKg9Tojpzor6YuEtozRQi_mQooppYrho6sadwU2z3iNZv5h3f7YPJMRh0h6s9xA%3D%3D?uid=0&filename=cat.png&disposition=inline&hash=&limit=0&content_type=image%2Fpng&owner_uid=0&tknv=v2&size=2048x2048";

    @Override
    public MessageEmbed command(Message eventMessage) throws EmbedCommandException {

        List<MessageEmbed.Field> fieldList = new ArrayList<>();

        fieldList.add(new MessageEmbed.Field("barsik answer {question})", "- return yes or no to your question", false, false));
        fieldList.add(new MessageEmbed.Field("barsik avatar {tag user}", "- return avatar image", false, false));
        fieldList.add(new MessageEmbed.Field("barsik ban {tag user}", "- ban selected user. Be careful! I can ban you for this action!  ", false, false));
        fieldList.add(new MessageEmbed.Field("barsik choose {option1},{option2}, ... ,{optionN}", "- Return 1 from options. Use comma for separate options", false, false));
        fieldList.add(new MessageEmbed.Field("barsik cat", "- return random cat image.", false, false));
        fieldList.add(new MessageEmbed.Field("barsik fox", "- return random fox image.", false, false));
        fieldList.add(new MessageEmbed.Field("barsik friend", "- return random dog image.", false, false));
        fieldList.add(new MessageEmbed.Field("barsik again", "- return result from your last command.", false, false));
        fieldList.add(new MessageEmbed.Field("barsik say {phrase}", "- return phrase.. or not.", false, false));
        fieldList.add(new MessageEmbed.Field("barsik meow", "- i'm a cat i can meow.", false, false));
        fieldList.add(new MessageEmbed.Field("barsik report {tag user}", "- user will be reported.", false, false));
        fieldList.add(new MessageEmbed.Field("barsik signup help", "- return signup command list", false, false));
        fieldList.add(new MessageEmbed.Field("barsik speak", "- just speak.", false, false));
        fieldList.add(new MessageEmbed.Field("barsik abuseadd {words}", "- add words to abuse validation. Use comma to separate words", false, false));
        fieldList.add(new MessageEmbed.Field("barsik banreason {reason}", "- add new reason for ban to ban command. %s in reason will be replaced to banned user tag", false, false));

        MessageEmbed.ImageInfo img = new MessageEmbed.ImageInfo(WORLD_DOMINATION_CAT, WORLD_DOMINATION_CAT, 200, 200);


        return new MessageEmbed(null, "Cat Helper",
                null, EmbedType.RICH, null, HELP_COLOR,
                null, null, null, null, null, img, fieldList);
    }

}
