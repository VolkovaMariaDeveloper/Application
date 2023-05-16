package org.linkParser.parser;

import org.linkParser.result.ParserResult;
import org.linkParser.result.StackOverflowParserResult;

public class StackOverflowParser implements Parser {
    private final Parser nextParser;

    public StackOverflowParser(Parser nextParser) {
        this.nextParser = nextParser;
    }

    @Override
    public ParserResult parse(String link) {
        if (checkLinkStackOverflow(link)) {
            String[] array = link.split("/");
            int idQuestionIndex = 4;
            String idQuestion = array[idQuestionIndex];
            return new StackOverflowParserResult(idQuestion);
        } else if (nextParser != null) {
            return nextParser.parse(link);
        }
        return null;
    }

    public boolean checkLinkStackOverflow(String link) {
        String d = "https://stackoverflow.com/questions/\\d*/.*";
        return link.matches(d);
    }
}
