package banking;

import java.util.Scanner;


public class UI {

    private final Functions functions;
    Database database;
    private final Scanner scanner = new Scanner(System.in);
    int id = 1;
    public UI(String urlAddress) {
        functions = new Functions(urlAddress);
        database = new Database(urlAddress);
    }
    private String createAccount() {

        String tempNum = functions.createNewCard();
        int pinNum = functions.generatePin();
        database.insertNew(new Account(tempNum,pinNum,0));
        id++;
        return ("\n" +
                "Your card has been created\n" +
                "Your card number:\n" +
                tempNum
                +"\n" +
                "Your card PIN:\n" +
                pinNum);
    }

    public void menu(){
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("""
                    1. Create an account
                    2. Log into account
                    0. Exit""");
            int command = scanner.nextInt();
            switch (command) {
                case 1 -> System.out.println(createAccount());
                case 2 -> logInAccount();
                case 0 -> {
                    System.out.println("\nBye!");
                    System.exit(0);
                }
            }
        }
    }
    private void logInAccount(){

        System.out.println("\nEnter your card number:");
        String cardNum = scanner.next();
        System.out.println("Enter your PIN:");
        int pinNum = scanner.nextInt();
        if (!functions.findCorrectnessNumAndPin(cardNum, String.valueOf(pinNum))) {
            System.out.println(("""

                        Wrong card number or PIN!
                        """));
        }

        else if (functions.findCorrectnessNumAndPin(cardNum, String.valueOf(pinNum))) {
            loggedIn(cardNum);
        }

    }

    private void loggedIn(String cardNUm){
        System.out.println("""

                You have successfully logged in!
                """);
        boolean checking = true;
        while(checking) {
            System.out.println("""

                    1. Balance
                    2. Add income
                    3. Do transfer
                    4. Close Account
                    5. Log out
                    0. Exit""");
            int commandInput = scanner.nextInt();

            switch (commandInput) {
                case 1 -> System.out.println(
                        "\nBalance " + database.getBalance(cardNUm) + "\n");
                case 2 -> {
                    System.out.println("Enter income:\n");
                    Integer incomeToAdd = Integer.valueOf(scanner.next());
                    database.addBalance(cardNUm,incomeToAdd);
                    System.out.println("Income was added!");
                }
                case 3 -> {
                    System.out.println("Transfer\nEnter card number:");
                    Scanner scanner1 = new Scanner(System.in);
                    String cardNumToTransfer = scanner1.nextLine();
                    if (!functions.checksum(cardNumToTransfer)) {
                        System.out.println("Probably you made a mistake in the card number. Please try again!");
                    }
                    else if (!database.checkIfExist(cardNumToTransfer)) {
                        System.out.println("Such a card does not exist.");
                    }
                    else {
                        System.out.println("Enter how much money you want to transfer:\n");
                        Integer moneyToAdd = scanner1.nextInt();
                        if (database.getBalance(cardNUm) - moneyToAdd >= 0) {
                            database.minusBalance(cardNUm,moneyToAdd);
                            database.addBalance(cardNumToTransfer,moneyToAdd);
                            System.out.println("Success!");
                        }
                        else {
                            System.out.println("Not enough money!");
                        }
                    }
                }
                case 4 -> {
                    database.deleteAccount(cardNUm);
                    System.out.println("The account has been closed!");
                    checking = false;
                }
                case 5 -> {
                    System.out.println("You have successfully logged out!");
                    checking = false;
                }
                case 0 -> {
                    System.out.println("\nBye!");
                    System.exit(0);
                }
            }
        }
    }

}