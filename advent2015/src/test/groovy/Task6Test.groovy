import spock.lang.Specification
import spock.lang.Unroll

import static Task6.*

@Unroll
class Task6Test extends Specification {

    def 'can create board with zero brightness'() {
        expect:
        new LightBoard(1).getBrightness() == 0
    }

    def 'given command will pass itself to the apply method of command'() {
        given:
        def board = new LightBoard(2)
        def command = Mock(Command)

        when:
        board.processCommand command

        then:
        1 * command.apply(board)
    }

    def 'turn on command increases number of lid lamps'() {
        given:
        def board = new LightBoard(3)
        def onCommand = new TurnOn(startingPoint, endPoint)

        when:
        board.processCommand onCommand

        then:
        board.getBrightness() == expectedLampsLid

        where:
        startingPoint  | endPoint       | expectedLampsLid
        Point.of(0, 0) | Point.of(0, 0) | 1
        Point.of(0, 0) | Point.of(1, 1) | 4
        Point.of(0, 0) | Point.of(2, 2) | 9
    }

    def "turn on command invoked twice doesn't change the number of lid lamps"() {
        given:
        def board = new LightBoard(3)
        def onCommand = new TurnOn(Point.of(0, 0), Point.of(2, 2))

        when:
        board.processCommand onCommand
        board.processCommand onCommand

        then:
        board.getBrightness() == 9
    }

    def 'toggle command works the same as turn on for lamps which are off'() {
        given:
        def board = new LightBoard(3)
        def toggleCommand = new Toggle(startingPoint, endPoint)

        when:
        board.processCommand toggleCommand

        then:
        board.getBrightness() == expectedLampsLid

        where:
        startingPoint  | endPoint       | expectedLampsLid
        Point.of(0, 0) | Point.of(0, 0) | 1
        Point.of(0, 0) | Point.of(1, 1) | 4
        Point.of(0, 0) | Point.of(2, 2) | 9
    }

    def 'if toggled twice on turned of board it leaves lamps off'() {
        given:
        def board = new LightBoard(3)
        def toggleCommand = new Toggle(Point.of(0, 0), Point.of(2, 2))

        when:
        board.processCommand toggleCommand
        board.processCommand toggleCommand

        then:
        board.getBrightness() == 0
    }

    def 'if toggled twice on turned on board it leaves lamps on'() {
        given:
        def board = new LightBoard(3)
        def onCommand = new TurnOn(Point.of(0, 0), Point.of(2, 2))
        def toggleCommand = new Toggle(Point.of(0, 0), Point.of(2, 2))

        when:
        board.processCommand onCommand
        board.processCommand toggleCommand
        board.processCommand toggleCommand

        then:
        board.getBrightness() == 9
    }

    def 'toggle can change state of the lamp'() {
        given:
        def board = new LightBoard(3)
        def onCommand = new TurnOn(Point.of(0, 0), Point.of(2, 2))
        def toggleCommand = new Toggle(Point.of(0, 0), Point.of(0, 0))

        when:
        board.processCommand onCommand
        board.processCommand toggleCommand

        then:
        board.getBrightness() == 8
    }

    def 'turn off command does nothing for turn off lamps'() {
        given:
        def board = new LightBoard(3)
        def offCommand = new TurnOff(Point.of(0, 0), Point.of(2, 2))

        when:
        board.processCommand offCommand

        then:
        board.getBrightness() == 0
    }

    def "turn on command turns off lamps which were turned on"() {
        given:
        def board = new LightBoard(3)
        def onCommand = new TurnOn(Point.of(0, 0), Point.of(2, 2))
        def offCommand = new TurnOff(Point.of(0, 0), Point.of(2, 0))

        when:
        board.processCommand onCommand
        board.processCommand offCommand

        then:
        board.getBrightness() == 6
    }

    def 'can create command from string'() {
        given:
        def factory = new CommandFactory()

        when:
        def created = factory.create(givenText)

        then:
        created == expectedCommand

        where:
        givenText                          | expectedCommand
        "turn on 0,0 through 999,999"      | new Task6.TurnOn(Point.of(0, 0), Point.of(999, 999))
        "toggle 0,0 through 999,0"         | new Task6.Toggle(Point.of(0, 0), Point.of(999, 0))
        "turn off 499,499 through 500,500" | new Task6.TurnOff(Point.of(499, 499), Point.of(500, 500))
    }

    def 'integration test 1'() {
        given:
        def board = new LightBoard(1000)
        def factory = new CommandFactory()
        def lines = getClass().getResourceAsStream("Task6.text").readLines()

        when:
        lines.stream().map() { factory.create it }.forEach() { board.processCommand it }

        then:
        board.getBrightness() == 400410
    }

    def "change command works as show in examples"() {
        given:
        def board = new LightBoard(1000)
        def changeCommand = new ChangeBrightness(startingPoint, endPoint, amount)

        when:
        board.processCommand changeCommand

        then:
        board.getBrightness() == expectedBrightness

        where:
        startingPoint  | endPoint           | amount | expectedBrightness
        Point.of(0, 0) | Point.of(2, 2)     | 1      | 9
        Point.of(0, 0) | Point.of(2, 2)     | 2      | 18
        Point.of(0, 0) | Point.of(2, 2)     | -1     | 0
        Point.of(0, 0) | Point.of(0, 0)     | 1      | 1
        Point.of(0, 0) | Point.of(999, 999) | 2      | 2000000
    }

    def "change command invoked twice is ok"() {
        given:
        def board = new LightBoard(1000)
        def changeCommand = new ChangeBrightness(startingPoint, endPoint, amount)

        when:
        board.processCommand new TurnOn(Point.of(0, 0),Point.of(0, 0))
        board.processCommand changeCommand
        board.processCommand changeCommand

        then:
        board.getBrightness() == expectedBrightness

        where:
        startingPoint  | endPoint           | amount | expectedBrightness
        Point.of(0, 0) | Point.of(2, 2)     | 1      | 19
        Point.of(0, 0) | Point.of(2, 2)     | -1     | 0
        Point.of(0, 0) | Point.of(999, 999) | 2      | 4000001
    }

    def 'updated factory can create command from string'() {
        given:
        def factory = new CommandFactoryUpdated()

        when:
        def created = factory.create(givenText)

        then:
        created == expectedCommand

        where:
        givenText                          | expectedCommand
        "turn on 0,0 through 999,999"      | new ChangeBrightness(Point.of(0, 0), Point.of(999, 999), 1)
        "toggle 0,0 through 999,0"         | new ChangeBrightness(Point.of(0, 0), Point.of(999, 0), 2)
        "turn off 499,499 through 500,500" | new ChangeBrightness(Point.of(499, 499), Point.of(500, 500), -1)
    }

    def 'integration test 2'() {
        given:
        def board = new LightBoard(1000)
        def factory = new CommandFactoryUpdated()
        def lines = getClass().getResourceAsStream("Task6.text").readLines()

        when:
        lines.stream().map() { factory.create it }.forEach() { board.processCommand it }

        then:
        board.getBrightness() == 15343601
    }
}
