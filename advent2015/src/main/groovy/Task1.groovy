class Task1 {
    int findEnterBasementMovePosition(String directions) {
        if (directions == null) return 0
        int currentFloor = 0
        int index = 1
        for (String seq : directions.toCharArray()) {
            currentFloor += decodeMove(seq.charAt(0))
            if (currentFloor < 0)
                return index
            index++
        }
        return -1
    }

    Integer moveSanta(String directions) {
        if (directions == null) return 0
        int currentFloor = 0
        for (String seq : directions.toCharArray()) {
            currentFloor += decodeMove(seq.charAt(0))
        }
        return currentFloor
    }

    private int decodeMove(char givenMove) {
        if (givenMove == '(')
            return 1
        if (givenMove == ')')
            return -1
        throw new UnknowMoveException()
    }

    static class UnknowMoveException extends RuntimeException {}
}