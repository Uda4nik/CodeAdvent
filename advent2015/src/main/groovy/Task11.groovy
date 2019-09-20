class Task11 {

    static class IncrementationStrategy {
        String getNext(String text) {
            def textWithoutLast = text.substring(0, text.length() - 1)
            def lastChar = text.getAt(text.length() - 1)
            if (lastChar == 'z') {
                return getNext(textWithoutLast) + "a"
            }

            return textWithoutLast + (++lastChar)
        }
    }

    static interface Validator {
        boolean validate(text)
    }

    static class CompoundValidator implements Validator {
        private List<Validator> validators = []

        CompoundValidator(List<Validator> validators) {
            this.validators = validators
        }

        @Override
        boolean validate(text) {
            for (each in validators) {
                if (!each.validate(text)) {
                    return false
                }
            }
            true
        }
    }

    static class NoBadLetters implements Validator {
        boolean validate(text) {
            return !text.contains("i") && !text.contains("o") && !text.contains("l")
        }
    }

    static class HasSequence implements Validator {
        boolean validate(text) {
            for (int i = 0; i < text.length() - 2; i++) {
                String toCheck = text.substring(i, i + 3)
                if (toCheck.charAt(0) + 1 == toCheck.charAt(1) && toCheck.charAt(1) + 1 == toCheck.charAt(2)) {
                    return true
                }
            }
            return false
        }
    }

    static class HasPairs implements Validator {
        boolean validate(text) {
            Set<String> pairs = []

            for (int i = 0; i < text.length() - 1; i++) {
                String toCheck = text.substring(i, i + 2)
                if (toCheck.charAt(0) == toCheck.charAt(1)) {
                    pairs.add(toCheck)
                    i++;
                }
            }
            return pairs.size() > 1
        }
    }
}
