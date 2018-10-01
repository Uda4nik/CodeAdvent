import spock.lang.Specification
import spock.lang.Unroll

import static Task8.Literal

@Unroll
class Task8Test extends Specification {
    private static final String QUOTE = "\""
    private static final String ESCAPE = "\\"

    def 'santa literals size can be retrived from given string'() {
        given:
        Literal literal = new Literal(given)

        expect:
        literal.getCharLength() == expectedCharLength
        literal.getStringLength() == expectedStringLengh

        where:
        given                                          | expectedCharLength | expectedStringLengh
        QUOTE + QUOTE                                  | 2                  | 0
        QUOTE + ESCAPE + ESCAPE + QUOTE                | 4                  | 1
        QUOTE + "abc" + QUOTE                          | 5                  | 3
        QUOTE + "aaa" + ESCAPE + QUOTE + "aaa" + QUOTE | 10                 | 7
        QUOTE + ESCAPE + "x27" + QUOTE                 | 6                  | 1
    }

    def 'integration test 1'() {
        given:
        def lines = getClass().getResourceAsStream("Task8.text").readLines()

        when:
        int result = lines.stream()
                .collect() { it -> new Literal(it) }
                .collect() { it -> it.getCharLength() - it.getStringLength() }
                .sum()
        then:
        result == 1350
    }
}
