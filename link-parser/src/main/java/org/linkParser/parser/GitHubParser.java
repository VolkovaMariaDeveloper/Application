package org.linkParser.parser;

import javafx.util.Pair;
import org.linkParser.result.GitHubParserResult;
import org.linkParser.result.ParserResult;

public class GitHubParser implements Parser {

    private final Parser nextParser;
    private static final int allowableLength = 5;
    private static final int userIndex = 3;
    private static final int repoIndex = 4;

    public GitHubParser(Parser nextParser) {
        this.nextParser = nextParser;
    }

    @Override
    public ParserResult parse(String link) {
        if (checkLinkGitHub(link)) {
            String[] array = link.split("/");

            if (array.length >= allowableLength) {
                Pair<String, String> pairUserRepository = new Pair<>(array[userIndex], array[repoIndex]);

                return new GitHubParserResult(pairUserRepository);
            }
        } else if (nextParser != null) {
            return nextParser.parse(link);
        }
        return null;
    }

    public boolean checkLinkGitHub(String link) {
        String d = "https://github.com/[\\w-]*/[\\w-]*";
        return link.matches(d);
    }
}

