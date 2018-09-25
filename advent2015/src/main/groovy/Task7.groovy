import groovy.transform.Canonical

class Task7 {

    static class WireNet {
        Map<String, Signal> wires = [:]
        private LineParser parser = new LineParser()

        static WireNet from(String input) {
            new WireNet(input)
        }

        private WireNet(String input) {
            input.readLines()
                    .collect() { line -> parser.parse(line) }
                    .each { pair -> wires.put(pair.wire, pair.signal) }
        }

        int getSignalOn(String wire) {
            Signal signal = wires[wire]
            if (signal instanceof ValueSignal) return Integer.valueOf(signal.value)
            0
        }
    }

    static class LineParser {
        Pair parse(String input) {
            String[] split = input.split(" -> ")
            return new Pair(signal: SignalFactory.from(split[0]), wire: split[1])
        }
    }

    @Canonical
    static class Pair {
        Signal signal
        String wire
    }

    static interface Signal {
        Signal findBaseSignal()
    }

    static class SignalFactory {
        static Signal from(String input) {
            if (input.isInteger()) return new ValueSignal(input)
            null
        }
    }

    @Canonical
    static class ValueSignal implements Signal {
        def value

        @Override
        Signal findBaseSignal() {
            return this
        }
    }

}
