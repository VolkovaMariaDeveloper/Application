package org.linkParser.parser;

import javafx.util.Pair;
import org.linkParser.result.GitHubParserResult;
import org.linkParser.result.ParserResult;

public class GitHubParser implements Parser {

    private final Parser nextParser;

    public GitHubParser(Parser nextParser) {
        this.nextParser = nextParser;
    }

    @Override
    public ParserResult parse(String link) {
        if (checkLinkGitHub(link)) {
            String[] array = link.split("/");
            if(array.length>=5){
            Pair<String, String> pairUserRepository = new Pair<>(array[3], array[4]);

            return new GitHubParserResult(pairUserRepository);}
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

