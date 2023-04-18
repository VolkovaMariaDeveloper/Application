package ru.tinkoff.edu.java.link_patser.parser;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.linkParser.parser.LinkParser;
import org.linkParser.result.GitHubParserResult;
import org.linkParser.result.ParserResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;


class GithubLinkParserTest {
    @Test
    public void parse_correctly_link() {
        String link = "https://github.com/VolkovaMariaDeveloper/Application/";

        GitHubParserResult gitHubResult = new GitHubParserResult(new Pair<>("VolkovaMariaDeveloper", "Application"));
        ParserResult request = LinkParser.parseLink(link);
        Pair<String, String> result = ((GitHubParserResult) request).pairUserRepository;
        assertThat(result).isEqualTo(gitHubResult.pairUserRepository);
    }


    @Test
    public void parse_link_without_repo() {
        String link = "https://github.com/VolkovaMariaDeveloper/";

        ParserResult request = LinkParser.parseLink(link);
        assertNull(request);
    }
    @Test
    public void parse_broken_link (){
        String link = "htttps://github.com/VolkovaMariaDeveloper/Application/";

        ParserResult request = LinkParser.parseLink(link);
        assertNull(request);
    }

    @Test
    public void parse_link_with_other_domain (){
        String link = "https://gitlub.com/VolkovaMariaDeveloper/Application/";

        ParserResult request = LinkParser.parseLink(link);
        assertNull(request);
    }

}

