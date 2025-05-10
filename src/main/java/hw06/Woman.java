package hw06;

import java.time.DayOfWeek;
import java.util.Map;

// Фінальний клас Woman
public final class Woman extends Human {
    public Woman(String name, String surname, int year) {
        super(name, surname, year);
    }

    public Woman(String name, String surname, int year, int iq,
                 Map<DayOfWeek, String> schedule) {
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
