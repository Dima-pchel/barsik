package ru.kets.barsik.service.impl;

import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kets.barsik.exception.EmbedCommandException;
import ru.kets.barsik.repo.SignupRepo;
import ru.kets.barsik.repo.pojo.Role;
import ru.kets.barsik.repo.pojo.Signup;
import ru.kets.barsik.service.SignupService;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static ru.kets.barsik.integrations.constant.Constants.DEFAULT_ROLE_NAME;
import static ru.kets.barsik.integrations.constant.Constants.DEFAULT_TIME_ZONE;

@Service
public class SignupServiceImpl implements SignupService {
    Logger LOG = LoggerFactory.getLogger(SignupServiceImpl.class);
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Resource
    private SignupRepo signupRepo;

    @Override
    public MessageEmbed getSignupByChannel(MessageChannelUnion channel) throws EmbedCommandException {
        Signup signup = signupRepo.findSignupByChannelId(channel.getId());
        return createEmbed(signup);
    }

    @Override
    public MessageEmbed createSignup(MessageChannelUnion channel) throws EmbedCommandException {
        Signup oldSignup = signupRepo.findSignupByChannelId(channel.getId());

        if (oldSignup != null) {
            signupRepo.delete(oldSignup);
        }

        Signup signup = new Signup();

        signup.setChannelId(channel.getId());
        signup.setRoles(Collections.emptyList());
        signupRepo.save(signup);

        return createEmbed(signup);
    }

    @Override
    public MessageEmbed addUser(MessageChannelUnion channel, User user, String roleName) throws EmbedCommandException {
        final String correctedRoleName = correctRoleName(roleName);
        Signup signup = signupRepo.findSignupByChannelId(channel.getId());
        List<Role> roles = signup.getRoles();

        Optional<Role> emptyRole = roles
                .stream()
                .filter(role -> StringUtils.isEmpty(role.getUser()) && correctedRoleName.equals(role.getRole()))
                .findFirst();
        if (emptyRole.isPresent()) {
            emptyRole.get().setUser(user.getId());
        } else {
            Role role = new Role();
            role.setUser(user.getId());
            role.setRole(roleName);
            roles.add(role);
        }
        signupRepo.save(signup);
        return createEmbed(signup);
    }

    @Override
    public MessageEmbed setDate(MessageChannelUnion channel, String date) throws EmbedCommandException {
        try {
            Signup signup = signupRepo.findSignupByChannelId(channel.getId());
            signup.setDate(formatter.parse(date));
            signupRepo.save(signup);
            return createEmbed(signup);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new EmbedCommandException("bad date format use pattern yyyy-MM-dd HH:mm");
        }
    }

    @Override
    public MessageEmbed setNote(MessageChannelUnion channel, String note) throws EmbedCommandException {
        Signup signup = signupRepo.findSignupByChannelId(channel.getId());
        signup.setNote(note);
        signupRepo.save(signup);
        return createEmbed(signup);
    }

    @Override
    public MessageEmbed setImage(MessageChannelUnion channel, String imgUrl) throws EmbedCommandException {
        Signup signup = signupRepo.findSignupByChannelId(channel.getId());
        signup.setImageLink(imgUrl);
        signupRepo.save(signup);
        return createEmbed(signup);
    }

    private String correctRoleName(String roleName) {
        if (StringUtils.isEmpty(roleName)) {
            return DEFAULT_ROLE_NAME;
        }
        return roleName;
    }


    private MessageEmbed createEmbed(Signup signup) {

        String name = StringUtils.isEmpty(signup.getName()) ? "signup" : signup.getName();
        String formatDate = StringUtils.EMPTY;
        Date date = signup.getDate();
        OffsetDateTime offDateTime = null;
        if (date != null) {
            formatDate = formatter.format(date);
            ZoneOffset zone = ZoneOffset.of(DEFAULT_TIME_ZONE);
            Instant inst = Instant.ofEpochMilli(date.getTime());
            offDateTime = OffsetDateTime.ofInstant(inst, zone);
        }
        String description = createDescription(signup.getRoles(), formatDate, signup.getNote());
        MessageEmbed.ImageInfo img = null;
        if (StringUtils.isNotEmpty(signup.getImageLink())) {
            img = new MessageEmbed.ImageInfo(signup.getImageLink(), signup.getImageLink(), 200, 200);
        }
//        MessageEmbed.Footer footer = new MessageEmbed.Footer(channelId, null, null);
        return new MessageEmbed(null, name,
                description, EmbedType.RICH, offDateTime, 100500,
                null, null, null, null, null, img, null);
    }

    private String createDescription(List<Role> roles, String formatDate, String note) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(formatDate)) {
            sb.append("Event date: ")
                    .append(formatDate)
                    .append(" (GMT+3 Moscow)")
                    .append("\n")
                    .append("\n");
        }
        for (Role role : roles) {
            sb.append(role.getRole())
                    .append(" - <@")
                    .append(role.getUser())
                    .append(">\n")
                    .append(">\n");
        }
        if (StringUtils.isNotEmpty(note)) {
            sb.append("Note: \n")
                    .append(note)
                    .append("\n");
        }

        return sb.toString();
    }
}
