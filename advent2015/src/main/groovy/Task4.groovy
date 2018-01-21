import javax.xml.bind.DatatypeConverter
import java.security.MessageDigest
import java.util.function.Predicate

class Task4 {
    static MessageDigest md5 = MessageDigest.getInstance("MD5")

    static int generate(String input, Predicate<String> condition) {
        int number = 0
        while (condition.negate().test(getHexBinary(input + number))) {
            number++
        }
        return number
    }


    static String getHexBinary(String stringWithNumber) {
        md5.reset()
        md5.update(stringWithNumber.getBytes("UTF-8"))
        DatatypeConverter.printHexBinary(md5.digest())
    }
}
