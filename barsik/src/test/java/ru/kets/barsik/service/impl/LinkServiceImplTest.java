package ru.kets.barsik.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kets.barsik.repo.LinkRepo;
import ru.kets.barsik.repo.pojo.Link;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LinkServiceImplTest {

    @Mock
    private LinkRepo linkRepo;

    @InjectMocks
    private LinkServiceImpl linkService;

    @Test
    void createAndSaveLink_parsesTypeDescriptionAndLink() {
        String result = linkService.createAndSaveLink("[yes] {some description} http://example.com");

        ArgumentCaptor<Link> captor = ArgumentCaptor.forClass(Link.class);
        verify(linkRepo).save(captor.capture());
        Link saved = captor.getValue();
        assertEquals(Link.Type.YES, saved.getType());
        assertEquals("some description", saved.getDescription());
        assertEquals("http://example.com", saved.getLink());
        assertEquals("Link added", result);
    }

    @Test
    void createAndSaveLink_isCaseInsensitiveForType() {
        linkService.createAndSaveLink("[Maybe] {d} http://example.com");

        ArgumentCaptor<Link> captor = ArgumentCaptor.forClass(Link.class);
        verify(linkRepo).save(captor.capture());
        assertEquals(Link.Type.MAYBE, captor.getValue().getType());
    }

    @Test
    void removeLink_deletesById() {
        String result = linkService.removeLink("42");

        verify(linkRepo).deleteById(eq(42L));
        assertEquals("Link removed", result);
    }

    @Test
    void removeLink_throwsOnNonNumericId() {
        assertThrows(NumberFormatException.class, () -> linkService.removeLink("not-a-number"));
    }

    @Test
    void showAll_returnsEmptyStringWhenNoLinks() {
        when(linkRepo.findAll()).thenReturn(Collections.emptyList());

        assertEquals("", linkService.showAll());
    }

    @Test
    void showAll_formatsEachLinkOnItsOwnLine() {
        Link first = new Link();
        first.setId(1L);
        first.setDescription("first desc");
        first.setType(Link.Type.YES);
        first.setLink("http://a.test");

        Link second = new Link();
        second.setId(2L);
        second.setDescription("second desc");
        second.setType(Link.Type.NO);
        second.setLink("http://b.test");

        when(linkRepo.findAll()).thenReturn(Arrays.asList(first, second));

        String result = linkService.showAll();

        assertEquals(
                "Id = 1, description = first desc, type = YES, link = http://a.test\n"
                        + "Id = 2, description = second desc, type = NO, link = http://b.test",
                result);
    }
}
