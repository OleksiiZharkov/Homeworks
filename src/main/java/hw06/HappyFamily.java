package hw06;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class HappyFamily {

    // Перерахування для видів тварин
    enum Species {
        DOG, CAT, FISH, ROBOCAT, UNKNOWN
    }

    // Перерахування для днів тижня
    enum DayOfWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    // Інтерфейс для тварин, які можуть бруднити
    interface Foulable {
        void foul();
    }

    // Абстрактний клас Pet
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
            this.habits = habits;
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
            return habits;
        }
    }

    // Класи тварин
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

    // Базовий клас Human
    static class Human {
        private Family family;

        public Human(String name, String surname, int year) {
        }

        public Human(String name, String surname, int year, int iq, Map<DayOfWeek, String> schedule) {
            this(name, surname, year);
        }

        public void greetPet() {
            System.out.println("Привіт, домашня тваринко!");
        }

        public void describePet() {
            if (family != null && family.getPet() != null) {
                Pet pet = family.getPet();
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
    }

    // Класи людей
    static final class Man extends Human {
        public Man(String name, String surname, int year) {
            super(name, surname, year);
        }

        public Man(String name, String surname, int year, int iq, Map<DayOfWeek, String> schedule) {
            super(name, surname, year, iq, schedule);
        }

        @Override
        public void greetPet() {
            System.out.println("Привіт, друже!");
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
            System.out.println("Привіт, солоденький!");
        }

        public void makeup() {
            System.out.println("Час накласти макіяж!");
        }
    }

    // Клас Family
    static class Family {
        private final List<Human> children;
        private Pet pet;

        public Family(Human mother, Human father) {
            this.children = new ArrayList<>();
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
            return 2 + children.size() + (pet != null ? 1 : 0);
        }

        public Pet getPet() {
            return pet;
        }

        public void setPet(Pet pet) {
            this.pet = pet;
        }

        public List<Human> getChildren() {
            return children;
        }
    }

    // Головний метод для тестування
    public static void main(String[] args) {
        // Ініціалізація тварин
        Set<String> dogHabits = new HashSet<>(Arrays.asList("гавкати", "бігати"));
        Dog dog = new Dog("Рекс", 3, 75, dogHabits);

        Set<String> catHabits = new HashSet<>(Arrays.asList("спати", "мурчати"));
        DomesticCat cat = new DomesticCat("Мурзик", 2, 60, catHabits);

        // Ініціалізація людей
        Man father = new Man("Іван", "Петров", 1980);
        Woman mother = new Woman("Марія", "Петрова", 1985);

        // Створення родини
        Family family = new Family(mother, father);
        family.setPet(dog);

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
        dog.respond();
        dog.eat();
        dog.foul();

        cat.respond();
        cat.eat();
        cat.foul();

        System.out.println("\n=== Унікальні методи ===");
        father.repairCar();
        mother.makeup();

        System.out.println("\n=== Інформація про родину ===");
        System.out.println("У родині " + family.countFamily() + " членів родини");
        father.describePet();
    }
}