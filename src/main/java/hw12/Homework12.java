package hw12;

import java.util.*;
import java.util.stream.Collectors;

public class Homework12 {

    // Human class
    static class Human {
        private final String name;
        private final String surname;
        private final int year;
        private final int iq;
        private final Map<String, String> schedule;
        private Family family;

        public Human(String name, String surname, int year, int iq,
                     Map<String, String> schedule, Family family) {
            this.name = name;
            this.surname = surname;
            this.year = year;
            this.iq = iq;
            this.schedule = schedule != null ? schedule : new HashMap<>();
            this.family = family;
        }

        public String getName() { return name; }
        public String getSurname() { return surname; }
        public int getYear() { return year; }
        public int getIq() { return iq; }
        public Map<String, String> getSchedule() { return schedule; }
        public Family getFamily() { return family; }

        public void setFamily(Family family) { this.family = family; }

        @Override
        public String toString() {
            return name + " " + surname + " (" + year + ")";
        }
    }

    // Man class
    static class Man extends Human {
        public Man(String name, String surname, int year, int iq,
                   Map<String, String> schedule, Family family) {
            super(name, surname, year, iq, schedule, family);
        }
    }

    // Woman class
    static class Woman extends Human {
        public Woman(String name, String surname, int year, int iq,
                     Map<String, String> schedule, Family family) {
            super(name, surname, year, iq, schedule, family);
        }
    }

    // Family class
    static class Family {
        private final Human mother;
        private final Human father;
        private final List<Human> children;

        public Family(Human mother, Human father) {
            this.mother = mother;
            this.father = father;
            this.children = new ArrayList<>();
            if (mother != null) mother.setFamily(this);
            if (father != null) father.setFamily(this);
        }

        public void addChild(Human child) {
            if (child != null) {
                children.add(child);
                child.setFamily(this);
            }
        }

        public Human getMother() { return mother; }
        public Human getFather() { return father; }
        public List<Human> getChildren() { return children; }

        public int countMembers() {
            int count = 0;
            if (mother != null) count++;
            if (father != null) count++;
            count += children.size();
            return count;
        }

        @Override
        public String toString() {
            return "Family: " + (mother != null ? mother.getSurname() : "Unknown") +
                    " (Mother: " + (mother != null ? mother.getName() : "None") +
                    ", Father: " + (father != null ? father.getName() : "None") +
                    ", Children: " + children.size() + ")";
        }
    }

    // FamilyService class
    static class FamilyService {
        private final List<Family> families;

        public FamilyService() {
            this.families = new ArrayList<>();
        }

        public void addFamily(Family family) {
            if (family != null) {
                families.add(family);
            }
        }

        public void displayAllFamilies() {
            families.forEach(System.out::println);
        }

        public List<Family> getFamiliesBiggerThan(int size) {
            return families.stream()
                    .filter(f -> f.countMembers() > size)
                    .collect(Collectors.toList());
        }

        public long countFamiliesWithMemberNumber(int size) {
            return families.stream()
                    .filter(f -> f.countMembers() == size)
                    .count();
        }

        public List<String> getAllChildrenNames() {
            return families.stream()
                    .flatMap(f -> f.getChildren().stream())
                    .map(Human::getName)
                    .sorted()
                    .collect(Collectors.toList());
        }
    }

    // Main Demo class
    public static class FamilyDemo {
        public static void main(String[] args) {
            FamilyService familyService = new FamilyService();

            // Create test data with proper Map initialization
            Map<String, String> emptySchedule = Collections.emptyMap();

            Human mother1 = new Woman("Олена", "Іванова", 1980, 110, emptySchedule, null);
            Human father1 = new Man("Іван", "Іванов", 1978, 105, emptySchedule, null);
            Family family1 = new Family(mother1, father1);

            family1.addChild(new Woman("Марія", "Іванова", 2010, 100, emptySchedule, null));
            family1.addChild(new Man("Петро", "Іванов", 2015, 95, emptySchedule, null));

            Human mother2 = new Woman("Наталія", "Петрова", 1985, 115, emptySchedule, null);
            Human father2 = new Man("Олег", "Петров", 1983, 108, emptySchedule, null);
            Family family2 = new Family(mother2, father2);

            family2.addChild(new Woman("Анна", "Петрова", 2018, 102, emptySchedule, null));

            familyService.addFamily(family1);
            familyService.addFamily(family2);

            // Test methods using Java 8 features
            System.out.println("=== Всі сім'ї ===");
            familyService.displayAllFamilies();

            System.out.println("\n=== Сім'ї з більш ніж 3 членами ===");
            familyService.getFamiliesBiggerThan(3).forEach(family ->
                    System.out.println(family.toString() + ": " + family.countMembers() + " членів")
            );

            System.out.println("\n=== Кількість сімей з 3 членами: " +
                    familyService.countFamiliesWithMemberNumber(3));

            System.out.println("\n=== Всі імена дітей (відсортовані): " +
                    familyService.getAllChildrenNames());
        }
    }
}