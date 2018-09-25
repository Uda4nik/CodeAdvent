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
    }
}
