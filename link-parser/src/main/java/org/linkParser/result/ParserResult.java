package org.linkParser.result;

public sealed interface ParserResult
        permits GitHubParserResult, StackOverflowParserResult {
}
