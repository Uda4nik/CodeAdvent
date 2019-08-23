class Task10 {

    static class NumberFormatter {

        String format(String input, int times) {
            String temp = input
            for (int i = 0; i < times; i++) {
                temp = format(temp)
            }
            temp
        }

        String format(String input) {
            StringBuilder result = new StringBuilder();
            def currentNumber = input.charAt(0)
            def repetition = 0
            for (String seq : input.toCharArray()) {
                if (seq != currentNumber && repetition > 0) {
                    result.append(repetition);
                    result.append(currentNumber);
                    currentNumber = seq;
                    repetition = 1;
                } else {
                    repetition++;
                }
            }
            if (repetition > 0) {
                result.append(repetition);
                result.append(currentNumber);
            }
            return result.toString();
        }
    }
}
