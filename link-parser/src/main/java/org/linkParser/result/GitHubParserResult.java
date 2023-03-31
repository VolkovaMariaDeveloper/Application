package org.linkParser.result;

import  javafx.util.Pair;

non-sealed public class GitHubParserResult implements ParserResult {
    public GitHubParserResult(Pair<String, String> result) {
        this.pairUserRepository = result;
    }

    public Pair<String, String> pairUserRepository;

}
