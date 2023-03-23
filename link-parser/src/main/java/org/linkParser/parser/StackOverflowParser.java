package org.linkParser.parser;

import org.linkParser.result.ParserResult;
import org.linkParser.result.StackOverflowParserResult;

public class StackOverflowParser implements Parser{
    private Parser nextParser;

    public StackOverflowParser(Parser nextParser) {
        this.nextParser = nextParser;
    }

    @Override
    public ParserResult parse(String link) {
        if (checkLinkStackOverflow(link)) {
            String[] array = link.split("/");
            String idQuestion = array[4];
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
