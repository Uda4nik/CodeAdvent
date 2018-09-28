import groovy.transform.Canonical

class Task7 {

    static class WireNet {
        Map<String, Signal> wires = [:]
        Map<Signal, Integer> cache = [:]
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
            if(cache[signal] == null){
                int result= signal.applyOnWire(this)
                cache[signal] = result
                return result
            } else {
                return cache[signal]
            }

        }

        void overrideSignal(String wire, int newValue) {
            wires.put(wire, new ValueSignal(newValue))
            cache = [:]
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
            if (input.isNumber()) return new ValueSignal(Integer.valueOf(input))
            if (input.contains("AND")) return newAddSignal(input)
            if (input.contains("OR")) return newOrSignal(input)
            if (input.contains("SHIFT")) return newShiftSignal(input)
            if (input.contains("NOT")) return newNotSignal(input)
            if (!input.isNumber()) return new TransitiveSignal(input)
            throw new UnsupportedOperationException("Can't parse the command ${input}")
        }

        private static Signal newAddSignal(input) {
            String[] split = input.split(" AND ")
            new BitSignal(wire1: split[0], wire2: split[1], type: BitCommandType.AND)
        }

        private static Signal newOrSignal(input) {
            String[] split = input.split(" OR ")
            new BitSignal(wire1: split[0], wire2: split[1], type: BitCommandType.OR)
        }

        private static Signal newShiftSignal(String input) {
            String[] split = input.split(" .SHIFT ")
            ShiftType type = input.contains("LSHIFT") ? ShiftType.LEFT : ShiftType.RIGHT
            new ShiftSignal(wire: split[0], type: type, bitsToShift: Integer.valueOf(split[1]))
        }

        private static Signal newNotSignal(String input) {
            String wire = input.substring("NOT ".length())
            new NotSignal(wire: wire)
        }
    }

    static interface Signal {
        int applyOnWire(WireNet net)
    }

    @Canonical
    static class ValueSignal implements Signal {
        int value

        @Override
        int applyOnWire(WireNet net) {
            return value
        }
    }

    @Canonical
    static class BitSignal implements Signal {
        String wire1, wire2
        BitCommandType type

        @Override
        int applyOnWire(WireNet net) {
            int leftWire = getValueOf(wire1, net)
            int rightWire = getValueOf(wire2, net)
            return type == BitCommandType.AND ? leftWire & rightWire : leftWire | rightWire
        }

        private getValueOf(String wire, WireNet net) {
            int result
            if (wire.isNumber()) {
                result = Integer.valueOf(wire)
            } else {
                result = net.getSignalOn(wire)
            }
            result
        }
    }

    @Canonical
    static class ShiftSignal implements Signal {
        String wire
        ShiftType type
        int bitsToShift

        @Override
        int applyOnWire(WireNet net) {
            int signalFromNet = net.getSignalOn(wire)
            return type == ShiftType.LEFT ? signalFromNet << bitsToShift : signalFromNet >> bitsToShift
        }
    }

    @Canonical
    static class NotSignal implements Signal {
        String wire

        @Override
        int applyOnWire(WireNet net) {
            int signalFromNet = net.getSignalOn(wire)
            return Short.toUnsignedInt((short) ~signalFromNet)
        }
    }

    @Canonical
    static class TransitiveSignal implements Signal {
        String wire

        @Override
        int applyOnWire(WireNet net) {
            return net.getSignalOn(wire)
        }
    }

    enum BitCommandType {
        AND, OR
    }

    enum ShiftType {
        LEFT, RIGHT
    }
}
