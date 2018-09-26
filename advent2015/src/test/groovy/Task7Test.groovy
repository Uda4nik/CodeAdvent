import spock.lang.Specification
import spock.lang.Unroll

import static Task7.WireNet

@Unroll
class Task7Test extends Specification {

    def 'can process signal'() {
        when:
        WireNet cut = WireNet.from(input)

        then:
        cut.getSignalOn(wire) == output

        where:
        input                              | wire | output
        "123 -> x"                         | "x"  | 123
        "123 -> x\n456 -> y\nx AND y -> d" | "d"  | 72
        "123 -> x\n456 -> y\nx OR y -> e"  | "e"  | 507
        "123 -> x\nx LSHIFT 2 -> f"        | "f"  | 492
        "456 -> y\ny RSHIFT 2 -> g"        | "g"  | 114
        "123 -> x\nNOT x -> h"             | "h"  | 65412
        "456 -> y\nNOT y -> i"             | "i"  | 65079
    }
}
