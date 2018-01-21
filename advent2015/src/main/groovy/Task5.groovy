import java.util.function.Predicate

class Task5 {


    static class CompoundFilter implements Predicate<String> {

        private Predicate<String>[] predicates

        CompoundFilter(Predicate<String>... predicates) {
            this.predicates = predicates
        }

        @Override
        boolean test(String word) {
            return Arrays.stream(predicates)
                    .map() { x -> x.test(word) }
                    .allMatch() { res -> res == true }

        }
    }

    static class VowelsFilter implements Predicate<String> {
        private VOWELS = ["a", "e", "i", "o", "u"]

        @Override
        boolean test(String word) {
            int numberOfVowels = 0
            for (def letter : word) {
                if (VOWELS.contains(letter)) {
                    numberOfVowels++
                }
            }
            return numberOfVowels > 2
        }
    }

    static class DoubledLetterFilter implements Predicate<String> {

        @Override
        boolean test(String word) {
            def previousLetter = ""
            for (def letter : word) {
                if (previousLetter == letter) return true
                previousLetter = letter
            }
            return false
        }
    }

    static class ExclusionFilter implements Predicate<String> {
        private EXCLUDED = ["ab", "cd", "pq", "xy"]

        @Override
        boolean test(String word) {
            for (def excluded : EXCLUDED) {
                if (word.contains(excluded)) return false
            }
            return true
        }
    }

    static class RepeatedPairFilter implements Predicate<String> {

        @Override
        boolean test(String word) {
            def previousLetter = ""
            Map<String, String> previousPairsMap = [:]
            int pos = 0

            for (def letter : word) {
                if (pos > 0) {
                    String pair = previousLetter + letter
                    if (previousPairsMap.keySet().contains(pair)) {
                        def previousPositions = getPositions(previousPairsMap, pair)
                        if (!previousPositions.contains("" + pos)) {
                            return true
                        }
                    } else {
                        previousPairsMap.put(pair, "" + pos + "=" + (pos + 1))
                    }

                }
                previousLetter = letter
                pos++
            }
            return false
        }

        private String[] getPositions(LinkedHashMap<String, String> previousPairsMap, String pair) {
            previousPairsMap.get(pair).split("=")
        }

        private String createPositionsString(int letterPosition) {
            "" + letterPosition + "=" + (letterPosition + 1)
        }
    }

    static class RepeatedLetterWithOneLetterBetween implements Predicate<String> {

        @Override
        boolean test(String word) {
            int pos = 0
            Map map = [:]

            for (def letter : word) {
                if (pos >= 2) {
                    def letterBefore = map.get(pos - 2)
                    if (letterBefore == letter) return true
                }
                map.put(pos, letter)
                pos++
            }
            return false
        }
    }
}
