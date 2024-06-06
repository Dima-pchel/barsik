package ru.kets.barsik.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.kets.barsik.repo.LinkRepo;
import ru.kets.barsik.repo.pojo.Link;
import ru.kets.barsik.service.LinkService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LinkServiceImpl implements LinkService {

    @Resource
    private LinkRepo linkRepo;

    @Override
    public String createAndSaveLink(String linkPattern) {
        //    [yes] {description} http://link.test
        Link link = new Link();
        link.setType(extractType(linkPattern));
        link.setDescription(extractDescription(linkPattern));
        link.setLink(extractLink(linkPattern));

        linkRepo.save(link);

        return "Link added";
    }

    @Override
    public String removeLink(String id) {
        linkRepo.deleteById(Long.valueOf(id));
        return "Link removed";
    }

    @Override
    public String showAll() {
        List<Link> links = linkRepo.findAll();
        return links.stream()
                .map(link -> String.format("Id = %s, description = %s, type = %s, link = %s", link.getId(), link.getDescription(), link.getType(), link.getLink()))
                .collect(Collectors.joining("\n"));
    }

    private String extractLink(String linkPattern) {
        String link = linkPattern.substring(linkPattern.indexOf("}") + 1);
        return StringUtils.trim(link);
    }

    private String extractDescription(String linkPattern) {
        return linkPattern.substring(linkPattern.indexOf("{") + 1, linkPattern.indexOf("}"));
    }

    private Link.Type extractType(String linkPattern) {
        String typeString = linkPattern.substring(linkPattern.indexOf("[") + 1, linkPattern.indexOf("]"));
        return Link.Type.valueOf(typeString.toUpperCase());
    }
}
