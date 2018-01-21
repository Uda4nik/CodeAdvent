import java.util.function.Predicate

class Task3 {
    private static def eventBus = new EventBus()

    static void addEventListeners(MoveEventListener... listeners) {
        listeners.each { listener -> eventBus.addSubscriber(listener) }
    }

    static void processDirections(String input) {
        input.getChars()
                .collect { singleChar -> MoveEvent.of(singleChar) }
                .forEach { event -> eventBus.processEvent(event) }
    }

    static class EventBus {
        List subscribers = []

        void addSubscriber(MoveEventListener listener) {
            subscribers << listener
        }

        void processEvent(MoveEvent event) {
            subscribers.forEach { it -> it.consume(event) }
        }
    }

    private static class MoveEvent {
        static int eventCounter = 0
        Character direction
        int number

        static def of(char c) {
            return new MoveEvent(direction: c, number: eventCounter++)
        }
    }

    private static interface MoveEventListener {
        void consume(MoveEvent event)
    }

    static class PresentGiver implements MoveEventListener {
        def x = 0
        def y = 0
        List visitedLocations = [[0, 0]]
        Predicate <MoveEvent> eventFilter

        PresentGiver() {
            eventFilter = { _ -> true }
        }

        PresentGiver(Predicate eventFilter) {
            this.eventFilter = eventFilter
        }

        @Override
        void consume(MoveEvent event) {
            if (eventFilter.test(event))
                moveOneStep(event.direction)
        }

        List getCurrentLocation() {
            return [x, y]
        }

        List getVisitedLocations() {
            return visitedLocations.unique()
        }

        private void moveOneStep(Character direction) {
            switch (direction) {
                case "<": x--; break
                case ">": x++; break
                case "^": y++; break
                case "v": y--; break
            }
            visitedLocations << getCurrentLocation()
        }


    }
}
