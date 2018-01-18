import spock.lang.Specification
import spock.lang.Unroll

import static Task2.Box

@Unroll
class Task2Test extends Specification {
    def cut = new Task2()

    def 'box can be created from string'() {
        expect:
        cut.parse(input) == box

        where:
        input   | box
        "1x1x1" | new Box(1, 1, 1)
        "2x2x2" | new Box(2, 2, 2)
        "1x2x3" | new Box(1, 2, 3)
    }

    def 'paper quantity can be calculated'() {
        expect:
        cut.calculatePaper(new Box(w, l, h)) == expectedQuantity

        where:
        w | l | h  | expectedQuantity
        1 | 1 | 1  | 7
        2 | 3 | 4  | 58
        1 | 1 | 10 | 43
    }

    def 'ribbon length can be calculated'() {
        expect:
        cut.calculateRibbonLength(new Box(w, l, h)) == expectedLength

        where:
        w | l | h  | expectedLength
        1 | 1 | 1  | 5
        2 | 3 | 4  | 34
        1 | 1 | 10 | 14
    }

    def 'integration test'() {
        given:
        def task2Text = getClass().getResourceAsStream("Task2.text").readLines()
        def boxes = task2Text.collect { line -> cut.parse(line) }
        expect:
        boxes.collect { box -> cut.calculatePaper(box) }.sum() == 1606483
        boxes.collect { box -> cut.calculateRibbonLength(box) }.sum() == 3842356

    }


}
