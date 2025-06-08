package hw10;
import java.util.Calendar;
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


class FamilyService {
    private final FamilyDao familyDao;

    public FamilyService(FamilyDao familyDao) {
        this.familyDao = familyDao;
    }

    public List<Family> getAllFamilies() {
        return familyDao.getAllFamilies();
    }

    public void displayAllFamilies() {
        List<Family> families = familyDao.getAllFamilies();
        for (int i = 0; i < families.size(); i++) {
            System.out.println(i + ": " + families.get(i));
        }
    }

    public void createNewFamily(Human father, Human mother) {
        familyDao.saveFamily(new Family(mother, father));
    }

    public void bornChild(Family family, String masculineName, String feminineName) {
        boolean isMale = new Random().nextBoolean();
        String name = isMale ? masculineName : feminineName;
        Human child = isMale ?
                new Man(name, family.getFather().getSurname(), Calendar.getInstance().get(Calendar.YEAR)) :
                new Woman(name, family.getFather().getSurname(), Calendar.getInstance().get(Calendar.YEAR));

        family.addChild(child);
        familyDao.saveFamily(family);
    }

    public void deleteAllChildrenOlderThan(int age) {
        familyDao.getAllFamilies().forEach(family -> {
            family.getChildren().removeIf(child -> child.getAge() > age);
            familyDao.saveFamily(family);
        });
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
    public static void main(String[] args) {
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
    }
}