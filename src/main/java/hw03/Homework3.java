package hw03;
import java.util.Scanner;
public class Homework3 {
    public static void main(String[] args) {
        String [][] schedule = new String[7][2];
        schedule[0][0] = "Sunday";
        schedule[0][1] = "do home work";
        schedule[1][0] = "Monday";
        schedule[1][1] = "go to courses;watch film";
        schedule[2][0] = "Tuesday";
        schedule[2][1] = "go to the gym";
        schedule[3][0] = "Wednesday";
        schedule[3][1] = "attend meeting; call parents";
        schedule[4][0] = "Thursday";
        schedule[4][1] = "study Java; read book";
        schedule[5][0] = "Friday";
        schedule[5][1] = "go out with friends";
        schedule[6][0] = "Saturday";
        schedule[6][1] = "relax; do hobby projects";

        Scanner scanner = new Scanner (System.in);

        while (true) {
            System.out.print("Please, input the day of the week: ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("exit")){
                break;
            }
            String dayFound= null;
            String tasks = null;

            for (String [] dayTasks : schedule) {
                if (dayTasks[0].equalsIgnoreCase(input)){
                    dayFound = dayTasks[0];
                    tasks = dayTasks[1];
                    break;

                }
            }
            if (dayFound !=null){
                System.out.println("Your tasks for" + dayFound + ": " + tasks + ".");
            }
            else {
                System.out.println("Sorry, I don't understand you, please try again.");
            }
        }
        scanner.close();
        System.out.println("Goodbye!");
    }
}
