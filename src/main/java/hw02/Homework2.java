package hw02;
import java.util.Random;
import java.util.Scanner;

public class Homework2 {
    public static void main(String[] args) {

        char[][] gameBoard = new char[5][5];

        // Створення стола для гри
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                gameBoard[i][j] = '-';
            }
        }
        // Вводимо рандом
        Random random = new Random();
        int targetRow = random.nextInt(5);
        int targetCol = random.nextInt(5);
        // Додаємо сканер
        Scanner scanner = new Scanner(System.in);
        System.out.println("All Set. Get ready to rumble!");
        // Будуємо функціонал
        while (true) {
            printGameBoard(gameBoard);

            int row = getCoordinate("Enter row (1-5): ", scanner);
            int col = getCoordinate("Enter column (1-5): ", scanner);

            if (row - 1 == targetRow && col - 1 == targetCol) {
                gameBoard[row - 1][col - 1] = 'x';
                printGameBoard(gameBoard);
                System.out.println("You have won!");
                break;
            } else if (gameBoard[row - 1][col - 1] == '*') {
                System.out.println("You already shot here! Try different coordinates.");
            } else {
                gameBoard[row - 1][col - 1] = '*';
                System.out.println("Miss! Try again.");
            }
        }
        scanner.close();
    }
    // Праця над виглядом
    public static void printGameBoard(char[][] board) {
        System.out.println("\n  1 2 3 4 5");
        System.out.println("  ---------");
        for (int i = 0; i < board.length; i++) {
            System.out.print((i + 1) + "|");
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(" " + board[i][j]);
            }
            System.out.println();
        }
    }

    public static int getCoordinate(String prompt, Scanner scanner) {
        while (true) {
            System.out.print(prompt);
            try {
                int coord = scanner.nextInt();
                if (coord >= 1 && coord <= 5) {
                    return coord;
                }
                System.out.println("Please enter number 1-5");
            } catch (Exception e) {
                System.out.println("Invalid input. Numbers only!");
                scanner.next();
            }
        }
    }
}