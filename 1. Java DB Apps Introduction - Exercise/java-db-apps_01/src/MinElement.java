import java.util.ArrayList;
import java.util.List;

public class MinElement {
    public static void main(String[] args) {

        int n = 3;

        List<Integer> numbers = new ArrayList<>(5);
        numbers.add(11);
        numbers.add(4);
        numbers.add(5);
        numbers.add(1);
        numbers.add(10);

        for (int round = 0; round < n; round++) {
            for (int i = 0; i < numbers.size() - 1; i++) {
                int currentNum = numbers.get(i);
                int nextNum = numbers.get(i + 1);

                if (nextNum < currentNum) {
                    numbers.set(i, nextNum);
                    numbers.set(i + 1, currentNum);
                    i = -1;
                }
            }
//            numbers.remove(0);
        }

        for (Integer number : numbers) {
            System.out.println(number);
        }
    }
}
