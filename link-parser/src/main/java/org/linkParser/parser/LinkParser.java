package org.linkParser.parser;

import org.linkParser.result.ParserResult;

public final class LinkParser {
    private LinkParser() {
    }

    public static ParserResult parseLink(String link) {

        StackOverflowParser stackOverflowParser = new StackOverflowParser(null);
        GitHubParser gitHubParser = new GitHubParser(stackOverflowParser);
        return gitHubParser.parse(link);

    }

}

