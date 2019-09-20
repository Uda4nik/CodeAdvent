import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class Task11Test extends Specification {

    def 'IncrementationStrategy can increment text'() {
        expect:
        new Task11.IncrementationStrategy().getNext(input) == expected

        where:
        input  | expected
        "aa"   | "ab"
        "ab"   | "ac"
        "az"   | "ba"
        "bzzz" | "caaa"
    }

    def 'NoBadLetters validator does not accept i, o and l'() {
        expect:
        new Task11.NoBadLetters().validate(input) == expected

        where:
        input                                           | expected
        "a,b,c,d,e,f,g,h,j,k,m,n,p,q,r,s,t,u,v,w,x,y,z" | true
        "i"                                             | false
        "o"                                             | false
        "l"                                             | false
    }

    def 'HasSequence validator should accept text with 3 consequent letters '() {
        expect:
        new Task11.HasSequence().validate(input) == expected

        where:
        input | expected
        "abc" | true
        "abd" | false
    }

    def 'HasPairs validator should accept text with 2 different duplications of letters'() {
        expect:
        new Task11.HasPairs().validate(input) == expected

        where:
        input  | expected
        "aabb" | true
        "aaaa" | false
    }


    def 'compound validator invokes all validators'() {
        given:
        def mock1 = Mock(Task11.Validator)
        def mock2 = Mock(Task11.Validator)
        def cut = new Task11.CompoundValidator([mock1, mock2])

        when:
        cut.validate("any")

        then:
        1 * mock1.validate("any") >> true
        1 * mock2.validate("any") >> true
    }
}
