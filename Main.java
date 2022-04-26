import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while (true) {
            System.out.println("Enter two numbers in format: {source base} {target base} (To quit type /exit)");
            Scanner scanner = new Scanner(System.in);
            String str = scanner.nextLine();
            if (str.equals("/exit")) return;
            String[] arr = str.split(" ");
            int sourceBase = Integer.parseInt(arr[0]);
            int targetBase = Integer.parseInt(arr[1]);
            while (true) {
                System.out.printf("Enter number in base %s to convert to base %s (To go back type /back)", arr[0], arr[1]);
                String number = scanner.nextLine();
                if (number.equals("/back")) break;
                String[] array = number.split("\\.");
                if (array.length == 2) {
                    String decimalNumber = new BigInteger(array[0], sourceBase).toString();
                    String s1 = new BigInteger(decimalNumber).toString(targetBase);
                    String s2= "";
                    decimalNumber = new BigInteger(array[1],sourceBase).toString();
                    BigDecimal bigDecimal = new BigDecimal(decimalNumber).
                            divide(new BigDecimal(sourceBase).pow(array[1].length()),6, RoundingMode.HALF_DOWN);
                    BigDecimal t = new BigDecimal(targetBase);
                    int n = 0;
                    List<String> list = new ArrayList<>();
                    while (n < 5) {
                        bigDecimal = bigDecimal.multiply(t);
                        array = bigDecimal.toString().split("\\.");
                        list.add(array[0]);
                        bigDecimal = new BigDecimal("0." + array[1]);
                        n++;
                    }
                    for (int i = 0; i < list.size(); i++) {
                        s2 += Integer.toString(Integer.parseInt(list.get(i)), targetBase);
                    }
                    System.out.println("Conversion result: " + s1 + "." + s2);
                } else {
                    String decimalNumber = new BigInteger(number, sourceBase).toString();
                    String result = new BigInteger(decimalNumber).toString(targetBase);
                    System.out.println("Conversion result: " + result);
                }
            }
        }
    }
}
