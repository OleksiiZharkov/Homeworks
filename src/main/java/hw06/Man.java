package hw06;

import java.time.DayOfWeek;
import java.util.Map;

// Фінальний клас Man
public final class Man extends Human {
    public Man(String name, String surname, int year) {
        super(name, surname, year);
    }

    public Man(String name, String surname, int year, int iq,
               Map<DayOfWeek, String> schedule) {
        super(name, surname, year, iq, schedule);
    }

    public Man(String сергій, String сидоров, String s, int i) {
        super();
    }

    @Override
    public void greetPet() {
        System.out.println("Привіт, друже!");
    }

    public void repairCar() {
        System.out.println("Час полагодити машину!");
    }
}
