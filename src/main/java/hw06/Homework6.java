package hw06;
import hw04.Homework4;
import java.time.DayOfWeek;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Homework6 {
    public static void main(String[] args) {

        // Перерахування для видів тварин
        enum Species {
            DOG, CAT, FISH, ROBOCAT, UNKNOWN;
        }

        // Інтерфейс для тварин, які можуть бруднити
        interface Foulable {
            void foul();
        }

        // Абстрактний клас Pet
        abstract class Pet {
            private Species species;
            private final String nickname;
            private final int age;
            private final int trickLevel;
            private final Set<String> habits;

            static {
                System.out.println("Pet class is being loaded");
            }

            {
                this.species = Species.UNKNOWN;
                System.out.println("Pet object is being created");
            }

            public Pet(String nickname, int age, int trickLevel, Set<String> habits) {
                this.nickname = nickname;
                this.age = age;
                this.trickLevel = trickLevel;
                this.habits = habits;
            }

            public Species getSpecies() {
                return species;
            }

            protected void setSpecies(Species species) {
                this.species = species;
            }

            // Реалізація методу eat залишається в абстрактному класі
            public void eat() {
                System.out.println("Я їм!");
            }

            // Абстрактний метод respond
            public abstract void respond();

            @Override
            public String toString() {
                return species.name() + "{nickname='" + nickname + "', age=" + age +
                        ", trickLevel=" + trickLevel + ", habits=" + habits + "}";
            }
        }

        // Клас Fish
         class Fish extends Pet {
            public Fish(String nickname, int age, int trickLevel, Set<String> habits) {
                super(nickname, age, trickLevel, habits);
                setSpecies(Species.FISH);
            }

            @Override
            public void respond() {
                System.out.println("Буль-буль!");
            }
        }

        // Клас DomesticCat
         class DomesticCat extends Pet implements Foulable {
            public DomesticCat(String nickname, int age, int trickLevel, Set<String> habits) {
                super(nickname, age, trickLevel, habits);
                setSpecies(Species.CAT);
            }

            @Override
            public void respond() {
                System.out.println("Мяу-мяу!");
            }

            @Override
            public void foul() {
                System.out.println("Потрібно добре замести сліди...");
            }
        }

        // Клас Dog
         class Dog extends Pet implements Foulable {
            public Dog(String nickname, int age, int trickLevel, Set<String> habits) {
                super(nickname, age, trickLevel, habits);  // Виправлено nicknote на nickname
                setSpecies(Species.DOG);
            }

            @Override
            public void respond() {
                System.out.println("Гав-гав!");
            }

            @Override
            public void foul() {
                System.out.println("Потрібно добре замести сліди...");
            }
        }

        // Клас RoboCat
        class RoboCat extends Pet {
            public RoboCat(String nickname, int age, int trickLevel, Set<String> habits) {
                super(nickname, age, trickLevel, habits);
                setSpecies(Species.ROBOCAT);
            }

            @Override
            public void respond() {
                System.out.println("Біп-біп! Я робокіт!");
            }
        }
    }
}


