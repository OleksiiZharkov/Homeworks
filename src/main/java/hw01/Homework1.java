package hw01;
import java.util.Random;
import java.util.Scanner;

public class Homework1 {
    public static void main(String[] args) {

        Random random = new Random();
        int secretNumber = random.nextInt(101);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Let the game begin!");

        System.out.print("Please enter your name: ");
        String name = scanner.nextLine();

        // Створюємо масив для зберігання спроб гравця
        int[] guesses = new int[100]; // Максимум 100 спроб
        int attemptCount = 0; // Лічильник спроб

        // Створюємо нескінченний цикл гри
        while (true) {
            System.out.print("Guess the number (0-100): ");
            int userGuess = scanner.nextInt();

            // Зберігаємо спробу в масив
            guesses[attemptCount] = userGuess;
            attemptCount++;

            // Перевіряємо введене число
            if (userGuess < secretNumber) {
                System.out.println("Your number is too small. Please, try again.");
            } else if (userGuess > secretNumber) {
                System.out.println("Your number is too big. Please, try again.");
            } else {
                // Виводимо привітання, якщо число вгадано
                System.out.println("Congratulations, " + name + "!");
                break; // Виходимо з циклу
            }
        }

        // Додатковий вивід спроб
        System.out.print("Your guesses: ");
        for (int i = 0; i < attemptCount; i++) {
            System.out.print(guesses[i] + " ");
        }
        System.out.println();

        // Закриваємо
        scanner.close();
    }
}