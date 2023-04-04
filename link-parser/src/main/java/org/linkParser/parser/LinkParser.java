package org.linkParser.parser;

import org.linkParser.result.ParserResult;

public class LinkParser {

    public static ParserResult parseLink(String link) {

        StackOverflowParser stackOverflowParser = new StackOverflowParser(null);
        GitHubParser gitHubParser = new GitHubParser(stackOverflowParser);
        return gitHubParser.parse(link);

    }


}

