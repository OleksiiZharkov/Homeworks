package hw06;

import hw04.Homework4;

import java.time.DayOfWeek;
import java.util.Map;

public class Human {
    private Homework4.Family family;

    static {
        System.out.println("Human class is being loaded");
    }

    {
        System.out.println("Human object is being created");
    }

    public Human(String name, String surname, int year) {
    }

    public Human(String name, String surname, int year, int iq,
                 Map<DayOfWeek, String> schedule) {
        this(name, surname, year);
    }

    public Human() {

    }

    public void greetPet() {
        System.out.println("Привіт, домашня тваринко!");
    }

    public void describePet() {
        if (family != null && family.getPet() != null) {
            Homework4.Pet pet = family.getPet();
            String trickLevel = pet.getTrickLevel() > 50 ? "дуже хитрий" : "майже не хитрий";
            System.out.printf("У мене є %s, їй %d років, він %s\n",
                    pet.getSpecies(), pet.getAge(), trickLevel);
        }
    }

    // Гетери та сетери
    // equals, hashCode, toString
}
