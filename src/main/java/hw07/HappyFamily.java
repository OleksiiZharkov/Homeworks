package hw07;
import java.util.*;

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
        private final Set<String> habits; // Використовуємо Set для звичок

        {
            species = Species.UNKNOWN;
        }

        public Pet(String nickname, int age, int trickLevel, Set<String> habits) {
            this.nickname = nickname;
            this.age = age;
            this.trickLevel = trickLevel;
            this.habits = new HashSet<>(habits); // Захищене копіювання
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
            return new HashSet<>(habits); // Повертаємо копію
        }

        public void addHabit(String habit) {
            habits.add(habit);
        }

        public boolean removeHabit(String habit) {
            return habits.remove(habit);
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
        private Map<DayOfWeek, String> schedule; // Використовуємо Map для розкладу
        private Family family;

        public Human(String name, String surname, int year) {
            this.schedule = new EnumMap<>(DayOfWeek.class);
        }

        public Human(String name, String surname, int year, int iq, Map<DayOfWeek, String> schedule) {
            this(name, surname, year);
            this.schedule = new EnumMap<>(schedule); // Захищене копіювання
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

        public void addScheduleItem(DayOfWeek day, String activity) {
            schedule.put(day, activity);
        }

        public String getActivity(DayOfWeek day) {
            return schedule.get(day);
        }
    }

    static final class Man extends Human {
        public Man(String name, String surname, int year) {
            super(name, surname, year);
        }

        public Man(String name, String surname, int year, int iq, Map<DayOfWeek, String> schedule) {
            super(name, surname, year, iq, schedule);
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
        public Woman(String name, String surname, int year) {
            super(name, surname, year);
        }

        public Woman(String name, String surname, int year, int iq, Map<DayOfWeek, String> schedule) {
            super(name, surname, year, iq, schedule);
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
        private final List<Human> children; // List для дітей
        private final Set<Pet> pets; // Set для домашніх тварин

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
        // Тестування реалізації з колекціями
        Set<String> dogHabits = new HashSet<>(Arrays.asList("гавкати", "бігати", "гратися"));
        Dog dog = new Dog("Рекс", 3, 75, dogHabits);

        Set<String> catHabits = new HashSet<>(Arrays.asList("спати", "мурчати", "ловити мишей"));
        DomesticCat cat = new DomesticCat("Мурзик", 2, 60, catHabits);

        // Створення розкладу
        Map<DayOfWeek, String> schedule = new EnumMap<>(DayOfWeek.class);
        schedule.put(DayOfWeek.MONDAY, "Працювати");
        schedule.put(DayOfWeek.TUESDAY, "Вчитися");
        schedule.put(DayOfWeek.WEDNESDAY, "Відпочивати");

        // Ініціалізація людей
        Man father = new Man("Іван", "Петров", 1980, 90, schedule);
        Woman mother = new Woman("Марія", "Петрова", 1985, 95, schedule);

        // Створення родини
        Family family = new Family(mother, father);
        family.addPet(dog);
        family.addPet(cat);

        // Додавання дітей
        Man son = new Man("Олег", "Петров", 2010);
        Woman daughter = new Woman("Ольга", "Петрова", 2012);
        family.addChild(son);
        family.addChild(daughter);

        // Тестування
        System.out.println("=== Тестування методів ===");
        father.greetPet();
        mother.greetPet();
        son.greetPet();
        daughter.greetPet();

        System.out.println("\n=== Поведінка тварин ===");
        for (Pet pet : family.getPets()) {
            pet.respond();
            pet.eat();
            if (pet instanceof Foulable) {
                ((Foulable) pet).foul();
            }
        }

        System.out.println("\n=== Унікальні методи ===");
        father.repairCar();
        mother.makeup();

        System.out.println("\n=== Інформація про родину ===");
        System.out.println("У родині " + family.countFamily() + " членів родини");
        System.out.println("Домашні тварини: " + family.getPets().size());
        System.out.println("Діти: " + family.getChildren().size());
        father.describePet();

        // Тестування модифікації колекцій
        System.out.println("\n=== Тестування колекцій ===");
        System.out.println("Звички собаки: " + dog.getHabits());
        dog.addHabit("нести палицю");
        System.out.println("Оновлені звички собаки: " + dog.getHabits());

        System.out.println("Розклад батька у вівторок: " + father.getActivity(DayOfWeek.TUESDAY));
        father.addScheduleItem(DayOfWeek.THURSDAY, "Похід у кіно");
        System.out.println("Оновлений розклад: " + father.getSchedule());
    }
}