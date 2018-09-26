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
            if (signal instanceof AddSignal) return getSignalOn(signal.wire1) & getSignalOn(signal.wire2)
            if (signal instanceof OrSignal) return getSignalOn(signal.wire1) | getSignalOn(signal.wire2)
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



    static class SignalFactory {
        static Signal from(String input) {
            if (input.isInteger()) return new ValueSignal(input)
            if (input.contains("AND")) return newAddSignal(input)
            if (input.contains("OR")) return newOrSignal(input)
            null
        }

        private static Signal newAddSignal(input){
            String[] split = input.split(" AND ")
            new AddSignal(wire1: split[0], wire2: split[1])
        }

        private static Signal newOrSignal(input){
            String[] split = input.split(" OR ")
            new OrSignal(wire1: split[0], wire2: split[1])
        }
    }

    static interface Signal {}
    @Canonical
    static class ValueSignal implements Signal {
        def value

    }

    @Canonical
    static class AddSignal implements Signal {
        String wire1, wire2
    }

    @Canonical
    static class OrSignal implements Signal {
        String wire1, wire2
    }
}
