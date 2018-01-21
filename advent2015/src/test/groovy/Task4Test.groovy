import spock.lang.Specification
import spock.lang.Unroll

import static Task4.getHexBinary

class Task4Test extends Specification {

    @Unroll
    def 'should find number which added will make md5 to return hex with 5 leading zeros'() {
        when:
        int number = Task4.generate(given, { it -> it.startsWith("00000") })

        then:
        number == expectedResult

        and:
        getHexBinary(given + number).startsWith("00000")

        where:
        given      | expectedResult
        "abcdef"   | 609043
        "pqrstuv"  | 1048970
        "iwrupvqb" | 346386
    }

    def 'works for 6 zeros as well'() {
        expect:
        Task4.generate("iwrupvqb", { it -> it.startsWith("000000") }) == 9958218
    }

}
