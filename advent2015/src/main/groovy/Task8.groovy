import groovy.transform.Canonical

import java.util.regex.Matcher
import java.util.regex.Pattern

class Task8 {
    private static final String QUOTE = "\""
    private static final String ESCAPE = $/\\/$
    private static final String ESCAPED_ESCAPE = ESCAPE + ESCAPE

    @Canonical
    static class Literal {
        String underlying
        private final static Pattern CHAR_PATTERN = ~/\\x[a-f,0-9]{2}/

        Literal(String underlying) {
            this.underlying = underlying.replaceAll(" ", "")
        }

        int getCharLength() {
            underlying.toCharArray().length
        }

        int getStringLength() {
            String stripped = dropQuotes()
            Matcher matcher = stripped =~ CHAR_PATTERN
            int encoded = 0
            while (matcher.find()) {
                encoded++
            }
            String withOutEncodedChars = stripped.replaceAll(CHAR_PATTERN.pattern(), "")
            String droppedDoubleEscape = withOutEncodedChars.replaceAll(ESCAPED_ESCAPE, "a")
            String pureText = droppedDoubleEscape.replaceAll(ESCAPE, "")
            pureText.length() + encoded
        }

        String dropQuotes() {
            underlying.substring(1, underlying.length() -1)
        }
    }
}
