public class Item4 {

    private Item4() {
        System.out.println("constructor called");
    }

    public static int convertStringToInt(String input) {
        return Integer.parseInt(input);
    }

    public static void main(String[] args) {
        Item4 instance = new Item4();
        System.out.println(Item4.convertStringToInt("1"));
    }
}

