package com.example.rssfeed;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class XmlToHtmlController {

    @GetMapping("/xmltohtml")
    public String xmlToHtml() throws IOException {
        Path xmlPath = new ClassPathResource("input.xml").getFile().toPath();
        String xmlContent = Files.readString(xmlPath);
        Document xmlDoc = Jsoup.parse(xmlContent, "", Parser.xmlParser());
        Document htmlDoc = new Document("");
    
        // Parse and sort items by publication date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, dd MMM yyyy HH:mm:ss Z");
        List<Element> items = xmlDoc.select("channel > item")
                .stream()
                .sorted(Comparator.comparing(
                        item -> LocalDateTime.parse(item.selectFirst("pubDate").text(), formatter),
                        Comparator.reverseOrder()))
                .collect(Collectors.toList());
    
        Element html = htmlDoc.appendElement("html").attr("lang", "en");
        Element head = html.appendElement("head");
        head.appendElement("meta").attr("charset", "UTF-8");
        head.appendElement("meta").attr("name", "viewport").attr("content", "width=device-width, initial-scale=1.0");
        head.appendElement("title").text(xmlDoc.selectFirst("channel > title").text());
        head.appendElement("link").attr("rel", "stylesheet").attr("href", "styles.css");
    
        Element body = html.appendElement("body");
        Element header = body.appendElement("header");
        header.appendElement("h1").text(xmlDoc.selectFirst("channel > title").text());
        header.appendElement("p").text(xmlDoc.selectFirst("channel > description").text());
    
        Element main = body.appendElement("main");
        for (Element item : items) {
            Element section = main.appendElement("section");
            section.appendElement("h2").text(item.selectFirst("title").text());
            section.appendElement("p").text("Date: " + item.selectFirst("pubDate").text());
            section.appendElement("a").attr("href", item.selectFirst("link").text()).text("Link");
            section.appendElement("p").text(item.selectFirst("description").text());
        }
    
        return htmlDoc.outerHtml();
    }
    
}
