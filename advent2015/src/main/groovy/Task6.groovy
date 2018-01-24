import groovy.transform.Canonical
import groovy.transform.PackageScope

class Task6 {

    static class LightBoard {
        int[][] lampsBrightness

        LightBoard(int size) {
            this.lampsBrightness = new int[size][size]
        }

        void toggle(int x, int y) {
            if (lampsBrightness[x][y] == 1) {
                lampsBrightness[x][y] = 0
            } else {
                lampsBrightness[x][y] = 1
            }
        }

        void turnOn(int x, int y) {
            lampsBrightness[x][y] = 1
        }

        void increase(int x, int y, int amount) {
            if (lampsBrightness[x][y] + amount >= 0) {
                lampsBrightness[x][y] += amount
            }
        }

        void turnOff(int x, int y) {
            lampsBrightness[x][y] = 0
        }

        int getBrightness() {
            int brightness = 0
            for (int row = 0; row < lampsBrightness.length; row++) {
                for (int col = 0; col < lampsBrightness.length; col++) {
                    brightness += lampsBrightness[row][col]
                }
            }
            return brightness
        }

        void processCommand(Command command) {
            command.apply(this)
        }
    }

    static class CommandFactory {
        Command create(String text) {
            def command = extractCommand(text)
            def startingPoint = extractStartingPoint(text, command)
            def endPoint = extractEndPoint(text)
            switch (command) {
                case "turn on": return new TurnOn(startingPoint, endPoint)
                case "toggle": return new Toggle(startingPoint, endPoint)
                case "turn off": return new TurnOff(startingPoint, endPoint)
                default: throw new RuntimeException("Command not expected ${command}")
            }

        }

        @PackageScope
        Point extractStartingPoint(String text, String command) {
            def coordinates = text.substring(command.length() + 1, text.indexOf("through") - 1).split(",")
            return Point.of(coordinates[0], coordinates[1])
        }

        @PackageScope
        Point extractEndPoint(String text) {
            def coordinates = text.substring(text.indexOf("through") + 8).split(",")
            return Point.of(coordinates[0], coordinates[1])
        }

        @PackageScope
        String extractCommand(String text) {
            text.split("\\s\\d")[0]
        }
    }

    static class CommandFactoryUpdated extends CommandFactory {
        @Override
        Command create(String text) {
            def command = extractCommand(text)
            def startingPoint = extractStartingPoint(text, command)
            def endPoint = extractEndPoint(text)
            switch (command) {
                case "turn on": return new ChangeBrightness(startingPoint, endPoint, 1)
                case "toggle": return new ChangeBrightness(startingPoint, endPoint, 2)
                case "turn off": return new ChangeBrightness(startingPoint, endPoint, -1)
                default: throw new RuntimeException("Command not expected ${command}")
            }

        }
    }

    @Canonical
    static class TurnOn implements Command {

        Point startingPoint
        Point endPoint

        @Override
        void apply(LightBoard board) {
            for (int x = startingPoint.x; x <= endPoint.x; x++) {
                for (int y = startingPoint.y; y <= endPoint.y; y++) {
                    board.turnOn(x, y)
                }
            }
        }
    }

    @Canonical
    static class TurnOff implements Command {

        Point startingPoint
        Point endPoint

        @Override
        void apply(LightBoard board) {
            for (int x = startingPoint.x; x <= endPoint.x; x++) {
                for (int y = startingPoint.y; y <= endPoint.y; y++) {
                    board.turnOff(x, y)
                }
            }
        }
    }

    @Canonical
    static class Toggle implements Command {

        Point startingPoint
        Point endPoint

        @Override
        void apply(LightBoard board) {
            for (int x = startingPoint.x; x <= endPoint.x; x++) {
                for (int y = startingPoint.y; y <= endPoint.y; y++) {
                    board.toggle(x, y)
                }
            }
        }
    }

    @Canonical
    static class ChangeBrightness implements Command {

        Point startingPoint
        Point endPoint
        int amount

        @Override
        void apply(LightBoard board) {
            for (int x = startingPoint.x; x <= endPoint.x; x++) {
                for (int y = startingPoint.y; y <= endPoint.y; y++) {
                    board.increase(x, y, amount)
                }
            }
        }
    }

    static interface Command {
        void apply(LightBoard board)
    }

    @Canonical
    static class Point {

        static Point of(x, y) {
            return new Point(x as Integer, y as Integer)
        }

        int x, y
    }
}
