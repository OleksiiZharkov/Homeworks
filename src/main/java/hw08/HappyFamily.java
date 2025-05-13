package hw08;

import java.util.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.*;

public class HappyFamily {

    enum Species {
        DOG, CAT, FISH, ROBOCAT, UNKNOWN
    }

    enum DayOfWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    interface Foulable {
        void foul();
    }

    static abstract class Pet {
        private Species species;
        private final String nickname;
        private final int age;
        private final int trickLevel;
        private final Set<String> habits;

        {
            species = Species.UNKNOWN;
        }

        public Pet(String nickname, int age, int trickLevel, Set<String> habits) {
            this.nickname = nickname;
            this.age = age;
            this.trickLevel = trickLevel;
            this.habits = new HashSet<>(habits);
        }

        public void eat() {
            System.out.println("Я їм!");
        }

        public abstract void respond();

        protected void setSpecies(Species species) {
            this.species = species;
        }

        public Species getSpecies() {
            return species;
        }

        public String getNickname() {
            return nickname;
        }

        public int getAge() {
            return age;
        }

        public int getTrickLevel() {
            return trickLevel;
        }

        public Set<String> getHabits() {
            return new HashSet<>(habits);
        }

        @Override
        public String toString() {
            return species.name() + "{nickname='" + nickname + "', age=" + age +
                    ", trickLevel=" + trickLevel + ", habits=" + habits + "}";
        }
    }

    static class Dog extends Pet implements Foulable {
        public Dog(String nickname, int age, int trickLevel, Set<String> habits) {
            super(nickname, age, trickLevel, habits);
            setSpecies(Species.DOG);
        }

        @Override
        public void respond() {
            System.out.println("Гав-гав! Я пес " + getNickname());
        }

        @Override
        public void foul() {
            System.out.println("Потрібно прибрати за мною у дворі...");
        }
    }

    static class DomesticCat extends Pet implements Foulable {
        public DomesticCat(String nickname, int age, int trickLevel, Set<String> habits) {
            super(nickname, age, trickLevel, habits);
            setSpecies(Species.CAT);
        }

        @Override
        public void respond() {
            System.out.println("Мяу-мяу! Я кіт " + getNickname());
        }

        @Override
        public void foul() {
            System.out.println("Потрібно замести сліди в квартирі...");
        }
    }

    static class Fish extends Pet {
        public Fish(String nickname, int age, int trickLevel, Set<String> habits) {
            super(nickname, age, trickLevel, habits);
            setSpecies(Species.FISH);
        }

        @Override
        public void respond() {
            System.out.println("Буль-буль! Я рибка " + getNickname());
        }
    }

    static class RoboCat extends Pet {
        public RoboCat(String nickname, int age, int trickLevel, Set<String> habits) {
            super(nickname, age, trickLevel, habits);
            setSpecies(Species.ROBOCAT);
        }

        @Override
        public void respond() {
            System.out.println("Біп-біп! Я робокіт " + getNickname());
        }
    }

    static class Human {
        private final String name;
        private final String surname;
        private final long birthDate;
        private int iq;
        private Map<DayOfWeek, String> schedule;
        private Family family;
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        public Human(String name, String surname, long birthDate) {
            this.name = name;
            this.surname = surname;
            this.birthDate = birthDate;
            this.schedule = new EnumMap<>(DayOfWeek.class);
        }

        public Human(String name, String surname, String birthDate, int iq) {
            this(name, surname, parseBirthDate(birthDate));
            this.iq = iq;
        }

        public Human(String name, String surname, long birthDate, int iq, Map<DayOfWeek, String> schedule) {
            this(name, surname, birthDate);
            this.iq = iq;
            this.schedule = new EnumMap<>(schedule);
        }

        private static long parseBirthDate(String dateStr) {
            try {
                LocalDate date = LocalDate.parse(dateStr, formatter);
                return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Невірний формат дати. Використовуйте dd/MM/yyyy");
            }
        }

        public String describeAge() {
            LocalDate birthDate = Instant.ofEpochMilli(this.birthDate)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate now = LocalDate.now();

            Period period = Period.between(birthDate, now);

            return String.format("%d років, %d місяців, %d днів",
                    period.getYears(), period.getMonths(), period.getDays());
        }

        public void greetPet() {
            if (family != null && !family.getPets().isEmpty()) {
                System.out.println("Привіт, " + family.getPets().iterator().next().getNickname() + "!");
            } else {
                System.out.println("Привіт, домашня тваринко!");
            }
        }

        public void describePet() {
            if (family != null && !family.getPets().isEmpty()) {
                Pet pet = family.getPets().iterator().next();
                String trickLevel = pet.getTrickLevel() > 50 ? "дуже хитрий" : "майже не хитрий";
                System.out.printf("У мене є %s, їй %d років, він %s\n",
                        pet.getSpecies(), pet.getAge(), trickLevel);
            }
        }

        public void setFamily(Family family) {
            this.family = family;
        }

        public Family getFamily() {
            return family;
        }

        public Map<DayOfWeek, String> getSchedule() {
            return new EnumMap<>(schedule);
        }

        public String getFormattedBirthDate() {
            return Instant.ofEpochMilli(birthDate)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                    .format(formatter);
        }

        @Override
        public String toString() {
            return "Human{" +
                    "name='" + name + '\'' +
                    ", surname='" + surname + '\'' +
                    ", birthDate=" + getFormattedBirthDate() +
                    ", iq=" + iq +
                    '}';
        }
    }

    static final class Man extends Human {
        public Man(String name, String surname, long birthDate) {
            super(name, surname, birthDate);
        }

        public Man(String name, String surname, String birthDate, int iq) {
            super(name, surname, birthDate, iq);
        }

        public Man(String name, String surname, long birthDate, int iq, Map<DayOfWeek, String> schedule) {
            super(name, surname, birthDate, iq, schedule);
        }

        @Override
        public void greetPet() {
            if (getFamily() != null && !getFamily().getPets().isEmpty()) {
                System.out.println("Привіт, друже " + getFamily().getPets().iterator().next().getNickname() + "!");
            } else {
                System.out.println("Привіт, друже!");
            }
        }

        public void repairCar() {
            System.out.println("Час полагодити машину!");
        }
    }

    static final class Woman extends Human {
        public Woman(String name, String surname, long birthDate) {
            super(name, surname, birthDate);
        }

        public Woman(String name, String surname, String birthDate, int iq) {
            super(name, surname, birthDate, iq);
        }

        public Woman(String name, String surname, long birthDate, int iq, Map<DayOfWeek, String> schedule) {
            super(name, surname, birthDate, iq, schedule);
        }

        @Override
        public void greetPet() {
            if (getFamily() != null && !getFamily().getPets().isEmpty()) {
                System.out.println("Привіт, солоденький " + getFamily().getPets().iterator().next().getNickname() + "!");
            } else {
                System.out.println("Привіт, солоденький!");
            }
        }

        public void makeup() {
            System.out.println("Час накласти макіяж!");
        }
    }

    static class Family {
        private final List<Human> children;
        private final Set<Pet> pets;

        public Family(Human mother, Human father) {
            this.children = new ArrayList<>();
            this.pets = new HashSet<>();
            mother.setFamily(this);
            father.setFamily(this);
        }

        public void addChild(Human child) {
            children.add(child);
            child.setFamily(this);
        }

        public boolean deleteChild(int index) {
            if (index < 0 || index >= children.size()) return false;
            children.get(index).setFamily(null);
            children.remove(index);
            return true;
        }

        public boolean deleteChild(Human child) {
            if (!children.remove(child)) return false;
            child.setFamily(null);
            return true;
        }

        public int countFamily() {
            return 2 + children.size() + pets.size();
        }

        public void addPet(Pet pet) {
            pets.add(pet);
        }

        public boolean removePet(Pet pet) {
            return pets.remove(pet);
        }

        public Set<Pet> getPets() {
            return new HashSet<>(pets);
        }

        public List<Human> getChildren() {
            return new ArrayList<>(children);
        }
    }

    public static void main(String[] args) {
        // Створення тварин
        Set<String> dogHabits = new HashSet<>(Arrays.asList("гавкати", "бігати"));
        Dog dog = new Dog("Рекс", 3, 75, dogHabits);

        // Створення розкладу
        Map<HappyFamily.DayOfWeek, String> schedule = new EnumMap<>(DayOfWeek.class);
        schedule.put(DayOfWeek.MONDAY, "Працювати");
        schedule.put(DayOfWeek.TUESDAY, "Вчитися");

        // Створення батьків
        Man father = new Man("Іван", "Петров", "15/05/1980", 90);
        Woman mother = new Woman("Марія", "Петрова", "20/08/1985", 95);

        // Створення родини
        Family family = new Family(mother, father);
        family.addPet(dog);

        // Додавання усиновленої дитини
        Man adoptedSon = new Man("Олег", "Петров", "10/02/2010", 85);
        family.addChild(adoptedSon);

        // Додавання звичайної дитини
        long daughterBirthDate = LocalDate.of(2012, 7, 15)
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
        Woman daughter = new Woman("Ольга", "Петрова", daughterBirthDate);
        family.addChild(daughter);

        // Тестування
        System.out.println("\n=== Інформація про вік ===");
        System.out.println("Батько: " + father.describeAge());
        System.out.println("Мати: " + mother.describeAge());
        System.out.println("Син: " + adoptedSon.describeAge());
        System.out.println("Донька: " + daughter.describeAge());

        System.out.println("\n=== Інформація про родину ===");
        System.out.println(father);
        System.out.println(mother);
        System.out.println(adoptedSon);
        System.out.println(daughter);

        System.out.println("\n=== Тестування методів ===");
        father.greetPet();
        mother.greetPet();
        adoptedSon.greetPet();
        daughter.greetPet();
    }
}