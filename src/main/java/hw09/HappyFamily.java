package hw09;
import hw04.Homework4;
import hw06.Human;
import hw06.Man;
import hw06.Woman;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.stream.Collectors;
import java.util.*;
import java.util.stream.*;
import java.time.*;
import java.time.format.*;

public class HappyFamily {

    // ... (попередні класи enum, інтерфейси та класи Pet, Human, Man, Woman, Family залишаються без змін)

    static class FamilyService {
        private final List<Homework4.Family> families;

        public FamilyService(List<Homework4.Family> families) {
            this.families = new ArrayList<>(families);
        }

        // Виводить всі родини з індексацією
        public void displayAllFamilies() {
            IntStream.range(0, families.size())
                    .forEach(i -> System.out.println((i + 1) + ". " + families.get(i)));
        }

        // Знаходить родини з кількістю людей більшою за задану
        public List<Homework4.Family> getFamiliesBiggerThan(int count) {
            return families.stream()
                    .filter(f -> f.countFamily() > count)
                    .collect(Collectors.toList());
        }

        // Знаходить родини з кількістю людей меншою за задану
        public List<Homework4.Family> getFamiliesLessThan(int count) {
            return families.stream()
                    .filter(f -> f.countFamily() < count)
                    .collect(Collectors.toList());
        }

        // Підраховує кількість родин з заданою кількістю членів
        public long countFamiliesWithMemberNumber(int count) {
            return families.stream()
                    .filter(f -> f.countFamily() == count)
                    .count();
        }

        public void deleteAllChildrenOlderThan(int age) {
            families.forEach(family -> {
                List<Homework4.Human> childrenToRemove = family.getChildren().stream()
                        .filter(child -> {
                            // Using the long timestamp stored in child.birthDate
                            LocalDate birthDate = Instant.ofEpochMilli(child.birthDate)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate();
                            int childAge = Period.between(birthDate, LocalDate.now()).getYears();
                            return childAge > age;
                        })
                        .toList().reversed();

                childrenToRemove.forEach(family::deleteChild);
            });
        }

        // Додатковий метод для тестування
        public void printFamilyMembersCount() {
            families.forEach(f ->
                    System.out.println("Родина з " + f.countFamily() + " членами")
            );
        }
    }

    public static void main(String[] args) {
        // Створення тестових даних
        List<Homework4.Family> families = new ArrayList<>();

        // Родина 1
        Man father1 = new Man("Іван", "Петров", "15/05/1980", 90);
        Woman mother1 = new Woman("Марія", "Петрова", "20/08/1985", 95);
        Homework4.Family family1 = new Homework4.Family(mother1, father1);
        family1.addChild(new Man("Олег", "Петров", "10/02/2010", 85));
        family1.addChild(new Woman("Ольга", "Петрова", "15/07/2012", 88));
        families.add(family1);

        // Родина 2
        Man father2 = new Man("Андрій", "Іванов", "10/03/1975", 92);
        Woman mother2 = new Woman("Наталія", "Іванова", "15/09/1980", 94);
        Homework4.Family family2 = new Homework4.Family(mother2, father2);
        family2.addChild(new Man("Михайло", "Іванов", "05/05/2005", 82));
        families.add(family2);

        // Родина 3 (без дітей)
        Man father3 = new Man("Сергій", "Сидоров", "20/11/1990", 89);
        Woman mother3 = new Woman("Тетяна", "Сидорова", "25/12/1992", 91);
        Homework4.Family family3 = new Homework4.Family(mother3, father3);
        families.add(family3);

        // Створення сервісу
        FamilyService familyService = new FamilyService(families);

        // Тестування методів
        System.out.println("=== Всі родини ===");
        familyService.displayAllFamilies();

        System.out.println("\n=== Родини з більш ніж 3 членами ===");
        familyService.getFamiliesBiggerThan(3).forEach(System.out::println);

        System.out.println("\n=== Родини з менш ніж 4 членами ===");
        familyService.getFamiliesLessThan(4).forEach(System.out::println);

        System.out.println("\n=== Кількість родин з 4 членами ===");
        System.out.println(familyService.countFamiliesWithMemberNumber(4));

        System.out.println("\n=== Видалення дітей старше 10 років ===");
        familyService.deleteAllChildrenOlderThan(10);
        familyService.displayAllFamilies();
    }
}