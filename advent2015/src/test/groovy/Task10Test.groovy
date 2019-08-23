import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class Task10Test extends Specification {

//    1 becomes 11 (1 copy of digit 1).
//    11 becomes 21 (2 copies of digit 1).
//    21 becomes 1211 (one 2 followed by one 1).
//    1211 becomes 111221 (one 1, one 2, and two 1s).
//    111221 becomes 312211 (three 1s, two 2s, and one 1).

//    1113222113  x 40
//    1113222113  x 50

    def "returns #output for #input"() {
        expect:
        new Task10.NumberFormatter().format(input) == output

        where:
        input    | output
        "1"      | "11"
        "11"     | "21"
        "11111"  | "51"
        "1211"   | "111221"
        "111221" | "312211"
    }

    def "can chain formatting"() {
        def stubFormatter = Spy(Task10.NumberFormatter)

        when:
        def result = stubFormatter.format("111", 3)

        then:
        result == "ZZZZ"
        1 * stubFormatter.format("111") >> "XXXX"
        1 * stubFormatter.format("XXXX") >> "YYYY"
        1 * stubFormatter.format("YYYY") >> "ZZZZ"
    }

    def "integration tests"() {
        given:
        def formatter= new Task10.NumberFormatter()

        when:
        def result = formatter.format("1113222113", 40)
        def result1 = formatter.format(result, 10)

        then:
        result.length() == 252594
        result1.length() == 3579328
    }
}


