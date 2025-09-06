package hw10;

import java.io.IOException;
import java.util.*;

class Human {
    private final String name;
    private final String surname;
    private final int birthYear;

    public Human(String name, String surname, int birthYear) {
        this.name = name;
        this.surname = surname;
        this.birthYear = birthYear;
    }

    public String getName() { return name; }
    public String getSurname() { return surname; }
    public int getBirthYear() { return birthYear; }

    public int getAge() {
        return Calendar.getInstance().get(Calendar.YEAR) - birthYear;
    }

    @Override
    public String toString() {
        return name + " " + surname + " (" + getAge() + " лет)";
    }
}

class Man extends Human {
    public Man(String name, String surname, int birthYear) {
        super(name, surname, birthYear);
    }
}

class Woman extends Human {
    public Woman(String name, String surname, int birthYear) {
        super(name, surname, birthYear);
    }
}

interface Pet {
    String getName();
}

class Dog implements Pet {
    private final String name;

    public Dog(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Собака: " + name;
    }
}

class Family {
    private final Human mother;
    private final Human father;
    private final List<Human> children;
    private final Set<Pet> pets;

    public Family(Human mother, Human father) {
        this.mother = mother;
        this.father = father;
        this.children = new ArrayList<>();
        this.pets = new HashSet<>();
    }

    public Human getMother() { return mother; }
    public Human getFather() { return father; }
    public List<Human> getChildren() { return children; }
    public Set<Pet> getPets() { return pets; }

    public void addChild(Human child) {
        children.add(child);
    }

    public boolean deleteChild(int index) {
        if (index >= 0 && index < children.size()) {
            children.remove(index);
            return true;
        }
        return false;
    }

    public void addPet(Pet pet) {
        pets.add(pet);
    }

    public int count() {
        return 2 + children.size(); // Родители + дети
    }

    @Override
    public String toString() {
        return "Семья:\n" +
                "Мать: " + mother + "\n" +
                "Отец: " + father + "\n" +
                "Дети: " + children + "\n" +
                "Питомцы: " + pets;
    }
}

interface FamilyDao {
    List<Family> getAllFamilies();
    Family getFamilyByIndex(int index);
    boolean deleteFamily(int index);
    boolean deleteFamily(Family family);
    void saveFamily(Family family);
}

class CollectionFamilyDao implements FamilyDao {
    private final List<Family> families = new ArrayList<>();

    @Override
    public List<Family> getAllFamilies() {
        return new ArrayList<>(families);
    }

    @Override
    public Family getFamilyByIndex(int index) {
        return (index >= 0 && index < families.size()) ? families.get(index) : null;
    }

    @Override
    public boolean deleteFamily(int index) {
        if (index >= 0 && index < families.size()) {
            families.remove(index);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteFamily(Family family) {
        return families.remove(family);
    }

    @Override
    public void saveFamily(Family family) {
        int index = families.indexOf(family);
        if (index != -1) {
            families.set(index, family);
        } else {
            families.add(family);
        }
    }
}

class FamilyController {
    private final FamilyService familyService;

    public FamilyController(FamilyService familyService) {
        this.familyService = familyService;
    }

    public void displayAllFamilies() {
        familyService.displayAllFamilies();
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        // Инициализация компонентов
        FamilyDao dao = new CollectionFamilyDao();
        FamilyService service = new FamilyService(dao);
        FamilyController controller = new FamilyController(service);

        Human father = new Man("Иван", "Иванов", 1980);
        Human mother = new Woman("Мария", "Иванова", 1985);
        service.createNewFamily(father, mother);

        Family family = service.getAllFamilies().get(0);
        service.bornChild(family, "Алексей", "Анна");

        family.addPet(new Dog("Шарик"));

        System.out.println("Все семьи:");
        controller.displayAllFamilies();

        // Test the new methods
        System.out.println("\nСемьи с более чем 3 членами: " +
                service.getFamiliesBiggerThan(3).size());
        System.out.println("Количество семей с 3 членами: " +
                service.countFamiliesWithMemberNumber(3));
        System.out.println("Имена всех детей: " +
                service.getAllChildrenNames());
    }
}