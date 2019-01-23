import java.util.function.Supplier;

public class Item5 {

    public static void main(String[] args) {

        Supplier<String> s = () -> "Hello World";
        String result = s.get();
        System.out.println(result);

    }
}
