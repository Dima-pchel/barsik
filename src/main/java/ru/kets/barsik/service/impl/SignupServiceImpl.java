package ru.kets.barsik.service.impl;

import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kets.barsik.exception.EmbedCommandException;
import ru.kets.barsik.repo.RoleRepo;
import ru.kets.barsik.repo.SignupRepo;
import ru.kets.barsik.repo.pojo.Role;
import ru.kets.barsik.repo.pojo.Signup;
import ru.kets.barsik.service.SignupService;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static ru.kets.barsik.integrations.constant.Constants.*;

@Service
public class SignupServiceImpl implements SignupService {
    Logger LOG = LoggerFactory.getLogger(SignupServiceImpl.class);
    private static final SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

    @Resource
    private SignupRepo signupRepo;

    @Resource
    private RoleRepo roleRepo;

    @Override
    public MessageEmbed getSignupByChannel(MessageChannelUnion channel) throws EmbedCommandException {
        Signup signup = getSignup(channel);
        return createEmbed(signup);
    }

    @Override
    public MessageEmbed createSignup(MessageChannelUnion channel, String name) throws EmbedCommandException {
        Signup oldSignup = signupRepo.findSignupByChannelId(channel.getId());

        if (oldSignup != null) {
            signupRepo.delete(oldSignup);
        }

        Signup signup = null;
        if (StringUtils.isNotEmpty(name)) {
            Signup pattern = signupRepo.findSignupByNameAndPattern(name, true);
            if (pattern != null) {
                signup = cloneSignup(pattern);
            }
        }

        if (signup == null) {
            signup = new Signup();
            signup.setName(name);
            signup.setRoles(Collections.emptyList());
        }

        signup.setChannelId(channel.getId());

        signupRepo.save(signup);

        return createEmbed(signup);
    }

    @Override
    public MessageEmbed saveSignupPattern(MessageChannelUnion channel, String name) throws EmbedCommandException {
        Signup signup = getSignup(channel);

        String patternName = extractPatternName(name, signup.getName());

        Signup oldPattern = signupRepo.findSignupByNameAndPattern(patternName, true);

        if (oldPattern != null) {
            signupRepo.delete(oldPattern);
        }

        Signup pattern = cloneSignup(signup);
        pattern.setPattern(true);
        if (StringUtils.isNotEmpty(name)) {
            pattern.setName(name);
        }
        signupRepo.save(pattern);

        throw new EmbedCommandException(String.format("Pattern %s has been saved!!!", name));
    }

    @Override
    public MessageEmbed addUser(MessageChannelUnion channel, User user, String roleName) throws EmbedCommandException {
        final String correctedRoleName = correctRoleName(roleName);
        Signup signup = getSignup(channel);
        List<Role> roles = signup.getRoles();

        Optional<Role> emptyRole = roles
                .stream()
                .filter(role -> StringUtils.isEmpty(role.getUser()) && correctedRoleName.equalsIgnoreCase(role.getRole()))
                .findFirst();
        if (emptyRole.isPresent()) {
            emptyRole.get().setUser(user.getId());
        } else {
            Role role = new Role();
            role.setUser(user.getId());
            role.setRole(correctedRoleName);
            roles.add(role);
        }
        signupRepo.save(signup);
        return createEmbed(signup);
    }

    @Override
    public MessageEmbed setDate(MessageChannelUnion channel, String date) throws EmbedCommandException {
        try {
            Signup signup = getSignup(channel);
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
        Signup signup = getSignup(channel);
        signup.setNote(note);
        signupRepo.save(signup);
        return createEmbed(signup);
    }

    @Override
    public MessageEmbed setImage(MessageChannelUnion channel, String imgUrl) throws EmbedCommandException {
        if (StringUtils.isEmpty(imgUrl) || !imgUrl.startsWith("http")) {
            throw new EmbedCommandException("Wrong link format. Please use correct link.");
        }
        Signup signup = getSignup(channel);
        signup.setImageLink(imgUrl);
        signupRepo.save(signup);
        return createEmbed(signup);
    }

    @Override
    public MessageEmbed setThumbnail(MessageChannelUnion channel, String imgUrl) throws EmbedCommandException {
        if (StringUtils.isEmpty(imgUrl) || !imgUrl.startsWith("http")) {
            throw new EmbedCommandException("Wrong link format. Please use correct link.");
        }
        Signup signup = getSignup(channel);
        signup.setThumbnailLink(imgUrl);
        signupRepo.save(signup);
        return createEmbed(signup);
    }

    @Override
    public MessageEmbed addRole(MessageChannelUnion channel, String roleName) throws EmbedCommandException {
        Signup signup = getSignup(channel);
        List<Role> roles = signup.getRoles();
        if (roles == null) {
            roles = new ArrayList<>();
        }

        Role role = new Role();
        role.setRole(correctRoleName(roleName));
        roles.add(role);
        signup.setRoles(roles);

        signupRepo.save(signup);
        return createEmbed(signup);
    }

    @Override
    public MessageEmbed removeRole(MessageChannelUnion channel, String roleName) throws EmbedCommandException {
        Signup signup = getSignup(channel);
        List<Role> filtredRoles = signup.getRoles();
        Role userRole = null;
        boolean emptyRoleDeleted = false;

        for (Role role : signup.getRoles()) {
            if (StringUtils.isEmpty(role.getUser()) && roleName.equalsIgnoreCase(role.getRole())) {
                filtredRoles.remove(role);
                emptyRoleDeleted = true;
                break;
            } else if (roleName.equalsIgnoreCase(role.getRole())) {
                userRole = role;
            }
        }
        if (!emptyRoleDeleted) {
            if (userRole != null) {
                filtredRoles.remove(userRole);
            } else {
                throw new EmbedCommandException(String.format("Role %s is not found in current signup.", roleName));
            }
        }
        signup.setRoles(filtredRoles);
        signupRepo.save(signup);
        return createEmbed(signup);
    }

    @Override
    public MessageEmbed removeUser(MessageChannelUnion channel, User author) throws EmbedCommandException {
        Signup signup = getSignup(channel);
        for (Role role : signup.getRoles()) {
            if (author.getId().equals(role.getUser())) {
                role.setUser(null);
                roleRepo.save(role);
            }
        }

        return createEmbed(getSignup(channel));
    }

    @NotNull
    private Signup getSignup(MessageChannelUnion channel) throws EmbedCommandException {
        Signup signup = signupRepo.findSignupByChannelId(channel.getId());
        if (signup == null) {
            throw new EmbedCommandException("MEOW! Signup not found!");
        }
        return signup;
    }

    private static String extractPatternName(String name, String signupName) throws EmbedCommandException {
        if (StringUtils.isEmpty(name)) {
            if (StringUtils.isNotBlank(signupName)) {
                return signupName;
            } else {
                throw new EmbedCommandException("You must setup pattern name before saving");
            }
        } else {
            return name;
        }
    }

    private String correctRoleName(String roleName) {
        if (StringUtils.isEmpty(roleName)) {
            return DEFAULT_ROLE_NAME;
        }
        return roleName;
    }


    private MessageEmbed createEmbed(Signup signup) throws EmbedCommandException {
        if (signup == null) {
            throw new EmbedCommandException("signup not found!!");
        }

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
        String description = createDescription(signup.getRoles(), formatDate);
        MessageEmbed.ImageInfo img = null;
        if (StringUtils.isNotEmpty(signup.getImageLink())) {
            img = new MessageEmbed.ImageInfo(signup.getImageLink(), signup.getImageLink(), 100, 200);
        }

        MessageEmbed.Thumbnail tail = null;
        if (StringUtils.isNotEmpty(signup.getThumbnailLink())) {
            tail = new MessageEmbed.Thumbnail(signup.getThumbnailLink(), "", 190, 190);

        }

        List<MessageEmbed.Field> fieldList = new ArrayList<>();
        if (StringUtils.isNotEmpty(signup.getNote())) {
            MessageEmbed.Field field = new MessageEmbed.Field("Note", signup.getNote(), false, false);
            fieldList.add(field);
        }

        MessageEmbed.Footer footer = null;
        if (signup.getDate() != null) {
            footer = new MessageEmbed.Footer("Your Time", TIME_ICON_IMG_LINK, null);
        }

//        MessageEmbed.AuthorInfo authorInfo = new MessageEmbed.AuthorInfo("name", "", "", "");

        return new MessageEmbed(null, name,
                description, EmbedType.RICH, offDateTime, 100500,
                tail, null, null, null, footer, img, fieldList);
    }

    private String createDescription(List<Role> roles, String formatDate) {
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
                    .append(" - ");
            if (StringUtils.isNotEmpty(role.getUser())) {
                sb.append("<@")
                        .append(role.getUser())
                        .append(">");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private Signup cloneSignup(Signup signup) {
        Signup clone = new Signup();
        clone.setName(signup.getName());
        clone.setImageLink(signup.getImageLink());
        clone.setThumbnailLink(signup.getThumbnailLink());
        clone.setNote(signup.getNote());

        List<Role> clonedRoles = CollectionUtils.emptyIfNull(signup.getRoles())
                .stream()
                .map(this::cloneRole)
                .collect(Collectors.toList());
        clone.setRoles(clonedRoles);
        return clone;
    }

    private Role cloneRole(Role role) {
        Role clone = new Role();
        clone.setRole(role.getRole());
        return clone;
    }
}
