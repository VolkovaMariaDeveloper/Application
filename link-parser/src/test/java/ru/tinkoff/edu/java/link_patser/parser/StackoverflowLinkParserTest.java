package ru.tinkoff.edu.java.link_patser.parser;

import org.junit.jupiter.api.Test;
import org.linkParser.parser.LinkParser;
import org.linkParser.result.ParserResult;
import org.linkParser.result.StackOverflowParserResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StackoverflowLinkParserTest {
    @Test
    public void parse_correctly_link() {
        String link = "https://stackoverflow.com/questions/52653836/maven-shade-javafx-runtime-components-are-missing";

        String stackOverflowResult = "52653836";
        StackOverflowParserResult request =  (StackOverflowParserResult) LinkParser.parseLink(link);
        String result = request.idQuestion;
        assertThat(result).isEqualTo(stackOverflowResult);
    }


    @Test
    public void parse_invalid_link() {
        String link = "https://stackoverflow.com/search?q=unsupported%20link";

        ParserResult request = LinkParser.parseLink(link);
        assertNull(request);
    }

    @Test
    public void parse_broken_link() {
        String link = "httpss://stackoverflow.com/search?q=unsupported%20link";

        ParserResult request = LinkParser.parseLink(link);
        assertNull(request);
    }

    @Test
    public void parse_link_withOut_idQuestion() {
        String link = "https://stackoverflow.com/questions/";

        ParserResult request = LinkParser.parseLink(link);
        assertNull(request);
    }

    @Test
    public void parse_link_with_negative_idQuestion() {
        String link = "https://stackoverflow.com/questions/-46437";

        ParserResult request = LinkParser.parseLink(link);
        assertNull(request);
    }

}
