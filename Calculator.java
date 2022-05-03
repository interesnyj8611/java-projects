import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Calculator {
    static Map<String, BigInteger> map = new HashMap<>();
    public static void getResult(String str) {
        String[] arr = str.trim().split("\\s+");
        Deque<BigInteger> stack = new ArrayDeque<>();
        for (var elem: arr) {
            if (elem.matches("\\d+")) {
                stack.offer(new BigInteger(elem));
            } else if (elem.matches("[A-Za-z]+")) {
                if(map.get(elem) == null) {
                    System.out.println("Unknown variable");
                    return;
                }
                stack.offer(map.get(elem));
            } else {
                BigInteger num1 = stack.pollLast();
                BigInteger num2 = stack.pollLast();
                switch (elem) {
                    case "+":
                        stack.offer(num2.add(num1));
                        break;
                    case "-":
                        stack.offer(num2.subtract(num1));
                        break;
                    case "*":
                        stack.offer(num2.multiply(num1));
                        break;
                    case "/":
                        stack.offer(num2.divide(num1));
                        break;
                }
            }
        }
        System.out.println(stack.peekLast());
    }

    public static void getRPN(String str) {
        if (!Pattern.compile("=+").matcher(str).find()) {
            Map<String, Integer> map = new HashMap<>(
                    Map.of("+", 0, "-", 0, "*", 1, "/", 1, "(", 2, ")", 2));
            List<String> list = new ArrayList<>(Arrays.asList(str.split("(?=[\\-+*/\\(\\)])|(?<=[\\-+/*\\(\\)])")))
                    .stream().map((s) -> s.trim()).filter((s) -> !s.equals("")).collect(Collectors.toList());
            Pattern pattern = Pattern.compile("[+\\-*/]+");
            String s = String.join(",", list).replaceAll("(-,-,)+", "+,")
                    .replaceAll("(\\+,)+", "+,")
                    .replaceAll("(\\+,-,)+", "-,");
            if (Pattern.compile("(\\*,\\*,)+").matcher(s).find() ||
                    Pattern.compile("(/,/,)+").matcher(s).find()) {
                System.out.println("Invalid expression");
                return;
            }
            if (s.substring(0,1).equals("-")) {
             s = "0," + s;
            }
            var arr = s.split(",");
            String result = "";
            Pattern p = Pattern.compile("[^\\-+*/\\(\\)]+");
            Deque<String> stack = new ArrayDeque<>();
            for (var elem : arr) {
                if (p.matcher(elem).matches()) {
                    result = result + " " + elem;
                } else if (stack.isEmpty() || stack.peekLast().equals("(")) {
                    stack.offer(elem);
                } else if (elem.equals("(")) {
                    stack.offer(elem);
                } else if (elem.equals(")")) {
                    while (true) {
                        result = result + " " + stack.pollLast();
                        if (stack.isEmpty()) {
                            System.out.println("Invalid expression");
                            return;
                        }
                        if (stack.peekLast().equals("(")) {
                            stack.pollLast();
                            break;
                        }
                    }
                } else if (map.get(elem) > map.get(stack.peekLast())) {
                    stack.offer(elem);
                } else if (map.get(elem) <= map.get(stack.peekLast())) {
                    while (true) {
                        result = result + " " + stack.pollLast();
                        if (stack.isEmpty() || map.get(elem) > map.get(stack.peekLast()) || stack.peekLast().equals("(")) {
                            stack.offer(elem);
                            break;
                        }
                    }
                }
            }
            if (stack.contains("(") || stack.contains(")")) {
                System.out.println("Invalid expression");
                return;
            } else {
                while (!stack.isEmpty()) {
                    result = result + " " + stack.pollLast();
                }
            }
            getResult(result);
        }
    }
    public static boolean getVariabl(String str) {
        String[] mas = str.split("=");
        if (mas.length == 2) {
            String key = mas[0].trim();
            String value = mas[1].trim();
            Matcher matcher1 = Pattern.compile("[^A-Za-z]+").matcher(key);
            if (matcher1.find()) {
                System.out.println("Invalid identifier");
                return false;
            }
            Matcher matcher2 = Pattern.compile("([^\\d]+) && ([^-]+)").matcher(value);
            Matcher matcher3 = Pattern.compile("[a-zA-Z]+").matcher(value);
            Matcher matcher4 = Pattern.compile("[\\d]+").matcher(value);
            if ((matcher3.find() && matcher4.find()) || (matcher3.matches() && !map.containsKey(value))) {
                System.out.println("Invalid assignment");
                return false;
            }
            if (matcher2.find() && !map.containsKey(value)) {
                System.out.println("Unknown variable");
                return false;
            }
            if (value.matches("[^\\d]+")) {
                map.put(key, map.get(value));
            } else {
                map.put(mas[0].trim(), new BigInteger(mas[1].trim()));
            }
        } else if (mas.length > 2) {
            System.out.println("Invalid assignment");
            return false;
        }
        return true;
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String str = scanner.nextLine();
            if (str.equals("/exit")) {
                System.out.println("Bye!");
                map.clear();
                break;
            }
            if (str.equals("/help")) {
                System.out.println("Program Calculator");
                continue;
            }
            if (str.length() == 0) {
                continue;
            }
            if (str.charAt(0) == '/') {
                System.out.println("Unknown command");
                continue;
            }
            if (getVariabl(str)) {
                getRPN(str);
            }
        }
    }
}
