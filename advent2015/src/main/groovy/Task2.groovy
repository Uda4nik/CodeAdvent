import groovy.transform.Canonical

class Task2 {

    Box parse(String input) {
        def split = input.split("x").collect { Integer.valueOf(it) }
        return new Box(split[0], split[1], split[2])
    }

    int calculatePaper(Box box) {
        box.getSquaresOfAllSides().sum() * 2 + box.getSquaresOfAllSides().min()
    }

    int calculateRibbonLength(Box box) {
        def ribbonForBow = box.getAllDimentions().inject(1, { acc, side -> acc * side })
        box.getTwoSmallestDimensions().sum() * 2 + ribbonForBow
    }

    @Canonical
    static class Box {
        int width, length, height

        Box(int width, int length, int height) {
            this.width = width
            this.length = length
            this.height = height
        }

        List getSquaresOfAllSides() {
            return [width * height, height * length, width * length]
        }

        List getAllDimentions() {
            return [width, length, height]
        }

        List getTwoSmallestDimensions() {
            def result = getAllDimentions()
            result.remove(getAllDimentions().max() as Object)
            return result
        }
    }
}
