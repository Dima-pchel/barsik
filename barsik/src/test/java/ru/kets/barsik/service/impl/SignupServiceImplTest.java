package ru.kets.barsik.service.impl;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kets.barsik.exception.EmbedCommandException;
import ru.kets.barsik.repo.RoleRepo;
import ru.kets.barsik.repo.SignupRepo;
import ru.kets.barsik.repo.pojo.Role;
import ru.kets.barsik.repo.pojo.Signup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SignupServiceImplTest {

    private static final String CHANNEL_ID = "channel-1";

    @Mock
    private SignupRepo signupRepo;

    @Mock
    private RoleRepo roleRepo;

    @Mock
    private MessageChannelUnion channel;

    @InjectMocks
    private SignupServiceImpl signupService;

    @BeforeEach
    void setUp() {
        lenient().when(channel.getId()).thenReturn(CHANNEL_ID);
    }

    private Signup signupWithRoles(Role... roles) {
        Signup signup = new Signup();
        signup.setChannelId(CHANNEL_ID);
        signup.setRoles(new ArrayList<>(Arrays.asList(roles)));
        return signup;
    }

    private Role role(String roleName, String user) {
        Role role = new Role();
        role.setRole(roleName);
        role.setUser(user);
        return role;
    }

    @Test
    void getSignupByChannel_throwsWhenNotFound() {
        when(signupRepo.findSignupByChannelId(CHANNEL_ID)).thenReturn(null);

        EmbedCommandException ex = assertThrows(EmbedCommandException.class,
                () -> signupService.getSignupByChannel(channel));
        assertEquals("MEOW! Signup not found!", ex.getMessage());
    }

    @Test
    void getSignupByChannel_returnsEmbedWhenFound() throws EmbedCommandException {
        Signup signup = signupWithRoles();
        signup.setName("raid");
        when(signupRepo.findSignupByChannelId(CHANNEL_ID)).thenReturn(signup);

        MessageEmbed embed = signupService.getSignupByChannel(channel);

        assertEquals("raid", embed.getTitle());
    }

    @Test
    void saveSignupPattern_savesClonedPatternAndThrowsConfirmationMessage() {
        Signup signup = signupWithRoles(role("Tank", "user-1"));
        signup.setName("raid");
        when(signupRepo.findSignupByChannelId(CHANNEL_ID)).thenReturn(signup);
        when(signupRepo.findSignupByNameAndPattern("weekly", true)).thenReturn(null);

        EmbedCommandException ex = assertThrows(EmbedCommandException.class,
                () -> signupService.saveSignupPattern(channel, "weekly"));

        assertEquals("Pattern weekly has been saved!!!", ex.getMessage());
        ArgumentCaptor<Signup> captor = ArgumentCaptor.forClass(Signup.class);
        verify(signupRepo).save(captor.capture());
        Signup saved = captor.getValue();
        assertEquals("weekly", saved.getName());
        assertTrue(saved.isPattern());
        assertEquals(1, saved.getRoles().size());
        assertNull(saved.getRoles().get(0).getUser());
    }

    @Test
    void saveSignupPattern_deletesExistingPatternWithSameName() {
        Signup signup = signupWithRoles();
        signup.setName("raid");
        Signup oldPattern = new Signup();
        when(signupRepo.findSignupByChannelId(CHANNEL_ID)).thenReturn(signup);
        when(signupRepo.findSignupByNameAndPattern("weekly", true)).thenReturn(oldPattern);

        assertThrows(EmbedCommandException.class, () -> signupService.saveSignupPattern(channel, "weekly"));

        verify(signupRepo).delete(oldPattern);
    }

    @Test
    void saveSignupPattern_throwsWhenNoNameAvailable() {
        Signup signup = signupWithRoles();
        when(signupRepo.findSignupByChannelId(CHANNEL_ID)).thenReturn(signup);

        EmbedCommandException ex = assertThrows(EmbedCommandException.class,
                () -> signupService.saveSignupPattern(channel, ""));

        assertEquals("You must setup pattern name before saving", ex.getMessage());
    }

    @Test
    void createSignup_deletesExistingSignupForChannel() throws EmbedCommandException {
        Signup oldSignup = signupWithRoles();
        when(signupRepo.findSignupByChannelId(CHANNEL_ID)).thenReturn(oldSignup);

        signupService.createSignup(channel, "");

        verify(signupRepo).delete(oldSignup);
    }

    @Test
    void createSignup_createsFreshSignupWhenNoPatternMatches() throws EmbedCommandException {
        when(signupRepo.findSignupByChannelId(CHANNEL_ID)).thenReturn(null);
        when(signupRepo.findSignupByNameAndPattern("raid", true)).thenReturn(null);

        signupService.createSignup(channel, "raid");

        ArgumentCaptor<Signup> captor = ArgumentCaptor.forClass(Signup.class);
        verify(signupRepo).save(captor.capture());
        Signup saved = captor.getValue();
        assertEquals("raid", saved.getName());
        assertEquals(CHANNEL_ID, saved.getChannelId());
        assertTrue(saved.getRoles().isEmpty());
    }

    @Test
    void createSignup_clonesFromPatternWhenNameMatchesExistingPattern() throws EmbedCommandException {
        Signup pattern = new Signup();
        pattern.setName("raid");
        pattern.setNote("bring potions");
        pattern.setRoles(Collections.singletonList(role("Tank", null)));
        when(signupRepo.findSignupByChannelId(CHANNEL_ID)).thenReturn(null);
        when(signupRepo.findSignupByNameAndPattern("raid", true)).thenReturn(pattern);

        signupService.createSignup(channel, "raid");

        ArgumentCaptor<Signup> captor = ArgumentCaptor.forClass(Signup.class);
        verify(signupRepo).save(captor.capture());
        Signup saved = captor.getValue();
        assertEquals("bring potions", saved.getNote());
        assertEquals(1, saved.getRoles().size());
        assertEquals("Tank", saved.getRoles().get(0).getRole());
        assertEquals(CHANNEL_ID, saved.getChannelId());
    }

    @Test
    void addUser_fillsExistingEmptyRoleSlot() throws EmbedCommandException {
        Signup signup = signupWithRoles(role("Tank", null));
        when(signupRepo.findSignupByChannelId(CHANNEL_ID)).thenReturn(signup);

        signupService.addUser(channel, "user-1", "Tank");

        assertEquals(1, signup.getRoles().size());
        assertEquals("user-1", signup.getRoles().get(0).getUser());
        verify(signupRepo).save(signup);
    }

    @Test
    void addUser_addsNewRoleWhenNoEmptySlotMatches() throws EmbedCommandException {
        Signup signup = signupWithRoles();
        when(signupRepo.findSignupByChannelId(CHANNEL_ID)).thenReturn(signup);

        signupService.addUser(channel, "user-1", "Healer");

        assertEquals(1, signup.getRoles().size());
        assertEquals("Healer", signup.getRoles().get(0).getRole());
        assertEquals("user-1", signup.getRoles().get(0).getUser());
    }

    @Test
    void addUser_usesDefaultRoleNameWhenRoleNameEmpty() throws EmbedCommandException {
        Signup signup = signupWithRoles();
        when(signupRepo.findSignupByChannelId(CHANNEL_ID)).thenReturn(signup);

        signupService.addUser(channel, "user-1", "");

        assertEquals("Flex", signup.getRoles().get(0).getRole());
    }

    @Test
    void setDate_parsesValidDateAndClearsNotificated() throws EmbedCommandException {
        Signup signup = signupWithRoles();
        signup.setNotificated(true);
        when(signupRepo.findSignupByChannelId(CHANNEL_ID)).thenReturn(signup);

        signupService.setDate(channel, "2030-01-15 20:00");

        assertEquals(false, signup.isNotificated());
        assertTrue(signup.getDate() != null);
        verify(signupRepo).save(signup);
    }

    @Test
    void setDate_throwsOnInvalidFormat() {
        Signup signup = signupWithRoles();
        when(signupRepo.findSignupByChannelId(CHANNEL_ID)).thenReturn(signup);

        EmbedCommandException ex = assertThrows(EmbedCommandException.class,
                () -> signupService.setDate(channel, "not-a-date"));
        assertEquals("bad date format use pattern yyyy-MM-dd HH:mm", ex.getMessage());
        verify(signupRepo, never()).save(any());
    }

    @Test
    void setNote_setsNoteAndSaves() throws EmbedCommandException {
        Signup signup = signupWithRoles();
        when(signupRepo.findSignupByChannelId(CHANNEL_ID)).thenReturn(signup);

        signupService.setNote(channel, "bring food");

        assertEquals("bring food", signup.getNote());
        verify(signupRepo).save(signup);
    }

    @Test
    void setImage_throwsWhenUrlDoesNotStartWithHttp() {
        EmbedCommandException ex = assertThrows(EmbedCommandException.class,
                () -> signupService.setImage(channel, "not-a-url"));
        assertEquals("Wrong link format. Please use correct link.", ex.getMessage());
    }

    @Test
    void setImage_throwsWhenUrlEmpty() {
        assertThrows(EmbedCommandException.class, () -> signupService.setImage(channel, ""));
    }

    @Test
    void setImage_setsImageLinkWhenValid() throws EmbedCommandException {
        Signup signup = signupWithRoles();
        when(signupRepo.findSignupByChannelId(CHANNEL_ID)).thenReturn(signup);

        signupService.setImage(channel, "http://example.com/img.png");

        assertEquals("http://example.com/img.png", signup.getImageLink());
    }

    @Test
    void setThumbnail_setsThumbnailLinkWhenValid() throws EmbedCommandException {
        Signup signup = signupWithRoles();
        when(signupRepo.findSignupByChannelId(CHANNEL_ID)).thenReturn(signup);

        signupService.setThumbnail(channel, "http://example.com/thumb.png");

        assertEquals("http://example.com/thumb.png", signup.getThumbnailLink());
    }

    @Test
    void addRole_appendsRoleWithCorrectedName() throws EmbedCommandException {
        Signup signup = signupWithRoles();
        when(signupRepo.findSignupByChannelId(CHANNEL_ID)).thenReturn(signup);

        signupService.addRole(channel, "Healer");

        assertEquals(1, signup.getRoles().size());
        assertEquals("Healer", signup.getRoles().get(0).getRole());
        assertNull(signup.getRoles().get(0).getUser());
    }

    @Test
    void addRole_initializesRolesListWhenNull() throws EmbedCommandException {
        Signup signup = new Signup();
        signup.setChannelId(CHANNEL_ID);
        signup.setRoles(null);
        when(signupRepo.findSignupByChannelId(CHANNEL_ID)).thenReturn(signup);

        signupService.addRole(channel, "Tank");

        assertEquals(1, signup.getRoles().size());
    }

    @Test
    void removeRole_removesEmptyRoleSlotFirst() throws EmbedCommandException {
        Signup signup = signupWithRoles(role("Tank", null), role("Tank", "user-1"));
        when(signupRepo.findSignupByChannelId(CHANNEL_ID)).thenReturn(signup);

        signupService.removeRole(channel, "Tank");

        assertEquals(1, signup.getRoles().size());
        assertEquals("user-1", signup.getRoles().get(0).getUser());
    }

    @Test
    void removeRole_removesUserAssignedRoleWhenNoEmptySlot() throws EmbedCommandException {
        Signup signup = signupWithRoles(role("Tank", "user-1"));
        when(signupRepo.findSignupByChannelId(CHANNEL_ID)).thenReturn(signup);

        signupService.removeRole(channel, "Tank");

        assertTrue(signup.getRoles().isEmpty());
    }

    @Test
    void removeRole_throwsWhenRoleNotFound() {
        Signup signup = signupWithRoles(role("Healer", null));
        when(signupRepo.findSignupByChannelId(CHANNEL_ID)).thenReturn(signup);

        EmbedCommandException ex = assertThrows(EmbedCommandException.class,
                () -> signupService.removeRole(channel, "Tank"));
        assertEquals("Role Tank is not found in current signup.", ex.getMessage());
    }

    @Test
    void removeUser_clearsUserFromMatchingRoleOnlyWhenSelectedRoleGiven() throws EmbedCommandException {
        Role tankRole = role("Tank", "user-1");
        Role healerRole = role("Healer", "user-1");
        Signup signup = signupWithRoles(tankRole, healerRole);
        when(signupRepo.findSignupByChannelId(CHANNEL_ID)).thenReturn(signup);

        signupService.removeUser(channel, "user-1", "Tank");

        assertNull(tankRole.getUser());
        assertEquals("user-1", healerRole.getUser());
        verify(roleRepo).save(tankRole);
        verify(roleRepo, never()).save(healerRole);
    }

    @Test
    void removeUser_clearsUserFromAllRolesWhenNoSelectedRole() throws EmbedCommandException {
        Role tankRole = role("Tank", "user-1");
        Role healerRole = role("Healer", "user-1");
        Signup signup = signupWithRoles(tankRole, healerRole);
        when(signupRepo.findSignupByChannelId(CHANNEL_ID)).thenReturn(signup);

        signupService.removeUser(channel, "user-1", "");

        assertNull(tankRole.getUser());
        assertNull(healerRole.getUser());
        verify(roleRepo, times(2)).save(any());
    }

    @Test
    void createEmbed_throwsWhenSignupIsNull() {
        EmbedCommandException ex = assertThrows(EmbedCommandException.class,
                () -> signupService.createEmbed(null));
        assertEquals("signup not found!!", ex.getMessage());
    }

    @Test
    void createEmbed_usesDefaultTitleWhenNameEmpty() throws EmbedCommandException {
        Signup signup = signupWithRoles();

        MessageEmbed embed = signupService.createEmbed(signup);

        assertEquals("signup", embed.getTitle());
    }

    @Test
    void createEmbed_includesNoteFieldWhenNotePresent() throws EmbedCommandException {
        Signup signup = signupWithRoles();
        signup.setNote("bring potions");

        MessageEmbed embed = signupService.createEmbed(signup);

        List<MessageEmbed.Field> fields = embed.getFields();
        assertTrue(fields.stream().anyMatch(f -> "Note".equals(f.getName()) && "bring potions".equals(f.getValue())));
    }

    @Test
    void createEmbed_descriptionListsRolesAndAssignedUsers() throws EmbedCommandException {
        Signup signup = signupWithRoles(role("Tank", "user-1"), role("Healer", null));

        MessageEmbed embed = signupService.createEmbed(signup);

        assertEquals("Tank - <@user-1>\nHealer - \n", embed.getDescription());
    }
}
