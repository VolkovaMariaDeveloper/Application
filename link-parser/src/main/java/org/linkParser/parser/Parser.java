package org.linkParser.parser;

import org.linkParser.result.ParserResult;

public interface Parser {
    ParserResult parse(String link);
}
