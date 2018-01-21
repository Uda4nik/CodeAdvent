import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Predicate

@Unroll
class Task5Test extends Specification {

    def 'filter for vowels accept word with 3 vowels'() {
        expect:
        new Task5.VowelsFilter().test(given) == expected

        where:
        given              | expected
        "a"                | false
        "aa"               | false
        "aab"              | false
        "aaa"              | true
        "bbb"              | false
        "eee"              | true
        "iii"              | true
        "ooo"              | true
        "uuu"              | true
        "ugknbfddgicrmopn" | true

    }

    def 'filter for double encounter of letter'() {
        expect:
        new Task5.DoubledLetterFilter().test(given) == expected

        where: " contationt at least 3 vowels, any from - aeiou"
        given              | expected
        "ax"               | false
        "aa"               | true
        "aba"              | false
        "baa"              | true
        "ugknbfddgicrmopn" | true
    }

    def 'filter for exclusion of ab, cd, pq, xy'() {
        expect:
        new Task5.ExclusionFilter().test(given) == expected

        where: " contationt at least 3 vowels, any from - aeiou"
        given              | expected
        "a"                | true
        "ab"               | false
        "cd"               | false
        "pq"               | false
        "xy"               | false
        "ugknbfddgicrmopn" | true
    }

    def 'compound filter 1 works on examples'() {
        given:
        def compound = createCompoundFilterForTask1()

        when:
        def result = compound.test(given)

        then:
        result == expectedResult

        where:
        given              | expectedResult
        "ugknbfddgicrmopn" | true
        "aaa"              | true
        "jchzalrnumimnmhp" | false
        "haegwjzuvuyypxyu" | false
        "dvszwmarrgswjxmb" | false
    }

    def 'integration test 1'() {
        given:
        def lines = getClass().getResourceAsStream("Task5.text").readLines()
        Predicate<String> filter = createCompoundFilterForTask1()

        when:
        def niceWordsCount = lines.stream()
                .filter() { word -> filter.test(word) }
                .count()

        then:
        niceWordsCount == 255

    }

    def 'filter for repeated pair'() {
        expect:
        new Task5.RepeatedPairFilter().test(given) == expected

        where:
        given              | expected
        "a"                | false
        "aa"               | false
        "aaa"              | false
        "aaaa"             | true
        "xyxy"             | true
        "ieodomkazucvgmuy" | false
    }

    def 'filter for repeated letter with letter in between'() {
        expect:
        new Task5.RepeatedLetterWithOneLetterBetween().test(given) == expected

        where:
        given | expected
        "a"   | false
        "ab"  | false
        "abc" | false
        "aba" | true
        "aaa" | true
    }

    def 'compound filter 2 works on examples'() {
        given:
        def compound = createCompoundFilterForTask2()

        expect:
        compound.test(given) == expectedResult

        where:
        given              | expectedResult
        "qjhvhtzxzqqjkmpb" | true
        "xxyxx"            | true
        "uurcxstgmygtbstg" | false
        "ieodomkazucvgmuy" | false
    }

    def 'integration test 2'() {
        given:
        def lines = getClass().getResourceAsStream("Task5.text").readLines()
        Predicate<String> filter = createCompoundFilterForTask2()

        when:
        def niceWordsCount = lines.stream()
                .filter() { word -> filter.test(word) }
                .count()

        then:
        niceWordsCount == 55

    }

    private Task5.CompoundFilter createCompoundFilterForTask1() {
        new Task5.CompoundFilter(
                new Task5.VowelsFilter(),
                new Task5.DoubledLetterFilter(),
                new Task5.ExclusionFilter()
        )
    }

    private Task5.CompoundFilter createCompoundFilterForTask2() {
        new Task5.CompoundFilter(
                new Task5.RepeatedPairFilter(),
                new Task5.RepeatedLetterWithOneLetterBetween()
        )
    }
}
