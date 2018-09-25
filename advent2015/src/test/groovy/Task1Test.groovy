import spock.lang.Specification

class Task1Test extends Specification {

    def cut = new Task1()

    def "given empty input Santa stays on the zero floor"() {
        expect:
        cut.moveSanta(null) == 0
        cut.moveSanta("") == 0
    }

    def "Santa can move up and down"() {
        expect:
        cut.moveSanta("(") == 1
        cut.moveSanta(")") == -1
    }

    def "exception is thown on illigal move"() {
        when:
        cut.moveSanta("x")

        then:
        thrown(Task1.UnknowMoveException)
    }

    def "Santa can move twice"() {
        expect:
        cut.moveSanta("((") == 2
        cut.moveSanta("))") == -2
    }

    def "Santa can move in different directions"() {
        expect:
        cut.moveSanta("()") == 0
        cut.moveSanta(")()(") == 0
    }

    def "character moved Santa to -1 is found"() {
        expect:
        cut.findEnterBasementMovePosition(")") == 1
        cut.findEnterBasementMovePosition("())") == 3
    }

    def "integration tests"() {
        given:
        def task1Text = getClass().getResourceAsStream("Task1.text").getText()

        expect:
        cut.moveSanta(task1Text) == 280
        cut.findEnterBasementMovePosition(task1Text) == 1797
    }
}
