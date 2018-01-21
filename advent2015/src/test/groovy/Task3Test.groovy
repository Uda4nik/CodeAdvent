import spock.lang.Specification
import spock.lang.Unroll

class Task3Test extends Specification {

    def 'santa starts at starting location'() {
        expect:
        new Task3.PresentGiver().getCurrentLocation() == [0, 0]
    }

    @Unroll
    def 'santa can move one step'() {
        given:
        def santa = new Task3.PresentGiver()
        Task3.addEventListeners(santa)

        when:
        Task3.processDirections(command)

        then:
        santa.getCurrentLocation() == expectedLocation

        where:
        command | expectedLocation
        "<"     | [-1, 0]
        ">"     | [1, 0]
        "^"     | [0, 1]
        "v"     | [0, -1]
    }

    @Unroll
    def 'santa can move more then one step'() {
        given:
        def santa = new Task3.PresentGiver()
        Task3.addEventListeners(santa)

        when:
        Task3.processDirections(commands)

        then:
        santa.getCurrentLocation() == expectedLocation

        where:
        commands | expectedLocation
        "<<<"    | [-3, 0]
        "><"     | [0, 0]
        "^^"     | [0, 2]
        "vvv"    | [0, -3]
        "<>^v"   | [0, 0]
    }

    @Unroll
    def 'santa keeps visited locations'() {
        given:
        def santa = new Task3.PresentGiver()
        Task3.addEventListeners(santa)

        when:
        Task3.processDirections(commands)

        then:
        santa.getVisitedLocations() == expectedLocation

        where:
        commands | expectedLocation
        "<<<"    | [[0, 0], [-1, 0], [-2, 0], [-3, 0]]
        "><"     | [[0, 0], [1, 0]]
        "^^"     | [[0, 0], [0, 1], [0, 2]]
        "vvv"    | [[0, 0], [0, -1], [0, -2], [0, -3]]
        "<^>v"   | [[0, 0], [-1, 0], [-1, 1], [0, 1]]
    }

    def 'integration test 1'(){
        given:
        def santa = new Task3.PresentGiver()
        def task3Text = getClass().getResourceAsStream("Task3.text").getText()
        Task3.addEventListeners(santa)

        when:
        Task3.processDirections(task3Text)

        then:
        santa.getVisitedLocations().size() == 2592
    }

    def 'integration test 2'(){
        given:
        def santa = new Task3.PresentGiver({event -> event.number%2 == 0})
        def robot = new Task3.PresentGiver({event -> event.number%2 == 1})
        def task3Text = getClass().getResourceAsStream("Task3.text").getText()
        Task3.addEventListeners(santa, robot)

        when:
        Task3.processDirections(task3Text)

        then:
        Set set = []
        set.addAll(santa.getVisitedLocations())
        set.addAll(robot.getVisitedLocations())
        set.size() == 2360
    }
}
