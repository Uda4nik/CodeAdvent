import spock.lang.Specification
import spock.lang.Unroll

import static Task7.WireNet

@Unroll
class Task7Test extends Specification {

    def 'can process signals'() {
        when:
        WireNet cut = WireNet.from(input)

        then:
        cut.getSignalOn(wire) == output

        where:
        input                              | wire | output
        "123 -> x"                         | "x"  | 123
        "123 -> xy"                        | "xy" | 123
        "123 -> x\nx -> y"                 | "y"  | 123
        "123 -> x\n456 -> y\nx AND y -> d" | "d"  | 72
        "456 -> y\n123 AND y -> d"         | "d"  | 72
        "123 -> x\nx AND 456 -> d"         | "d"  | 72
        "123 -> x\n456 -> y\nx OR y -> e"  | "e"  | 507
        "123 -> x\nx LSHIFT 2 -> f"        | "f"  | 492
        "456 -> y\ny RSHIFT 2 -> g"        | "g"  | 114
        "123 -> x\nNOT x -> h"             | "h"  | 65412
        "456 -> y\nNOT y -> i"             | "i"  | 65079
    }

    def 'integration test 1'() {
        given:
        String input = getClass().getResourceAsStream("Task7.text").text
        WireNet cut = WireNet.from(input)

        when:
        int result = cut.getSignalOn("a")

        then:
        result == 3176
    }

    def 'integration test 2'() {
        given:
        String input = getClass().getResourceAsStream("Task7.text").text
        WireNet cut = WireNet.from(input)

        and:
        cut.overrideSignal("b", cut.getSignalOn("a"))

        when:
        int result = cut.getSignalOn("a")

        then:
        result == 14710
    }
}
