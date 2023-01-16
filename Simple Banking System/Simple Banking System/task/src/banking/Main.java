package banking;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        UI ui = new UI(args[Arrays.asList(args).indexOf("-fileName") + 1]);
        ui.menu();
    }
}
