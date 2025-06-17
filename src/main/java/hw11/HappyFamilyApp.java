package hw11;
import java.text.SimpleDateFormat;
import java.util.*;

// Власни виняток для переповнення сім'ї
class FamilyOverflowException extends RuntimeException {
    public FamilyOverflowException(String message) {
        super(message);
    }
}

// Клас Pet
class Pet {
    private String species;
    private String nickname;
    private int age;
    private int trickLevel;
    private Set<String> habits;

    public Pet(String species, String nickname, int age, int trickLevel, Set<String> habits) {
        this.species = species;
        this.nickname = nickname;
        this.age = age;
        this.trickLevel = trickLevel;
        this.habits = habits;
    }

    public String prettyFormat() {
        return String.format("{species=%s, nickname='%s', age=%d, trickLevel=%d, habits=%s}",
                species, nickname, age, trickLevel, habits);
    }

    // Геттери та сеттери
    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public int getTrickLevel() { return trickLevel; }
    public void setTrickLevel(int trickLevel) { this.trickLevel = trickLevel; }
    public Set<String> getHabits() { return habits; }
    public void setHabits(Set<String> habits) { this.habits = habits; }
}

// Клас Human
class Human {
    private String name;
    private String surname;
    private Date birthDate;
    private int iq;
    private Map<DayOfWeek, String> schedule;

    public Human(String name, String surname, Date birthDate, int iq, Map<DayOfWeek, String> schedule) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.iq = iq;
        this.schedule = schedule;
    }

    public String prettyFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String birthDateStr = birthDate != null ? dateFormat.format(birthDate) : "null";
        String scheduleStr = schedule != null ? schedule.toString() : "null";

        return String.format("{name='%s', surname='%s', birthDate='%s', iq=%d, schedule=%s}",
                name, surname, birthDateStr, iq, scheduleStr);
    }

    // Геттери та сеттери
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    public Date getBirthDate() { return birthDate; }
    public void setBirthDate(Date birthDate) { this.birthDate = birthDate; }
    public int getIq() { return iq; }
    public void setIq(int iq) { this.iq = iq; }
    public Map<DayOfWeek, String> getSchedule() { return schedule; }
    public void setSchedule(Map<DayOfWeek, String> schedule) { this.schedule = schedule; }
}

// Клас Family
class Family {
    private Human mother;
    private Human father;
    private List<Human> children;
    private Set<Pet> pets;
    private static final int MAX_FAMILY_SIZE = 6; // Максимальний розмір сім'ї

    public Family(Human mother, Human father) {
        this.mother = mother;
        this.father = father;
        this.children = new ArrayList<>();
        this.pets = new HashSet<>();
    }

    public void addChild(Human child) {
        if (children.size() + 2 >= MAX_FAMILY_SIZE) { // +2 для батьків
            throw new FamilyOverflowException("Family cannot have more than " + MAX_FAMILY_SIZE + " members");
        }
        children.add(child);
    }

    public void deleteChild(int index) {
        if (index >= 0 && index < children.size()) {
            children.remove(index);
        }
    }

    public int countFamily() {
        return 2 + children.size(); // Батьки + діти
    }

    public String prettyFormat() {
        StringBuilder sb = new StringBuilder();
        sb.append("family: \n");
        sb.append("\tmother: ").append(mother.prettyFormat()).append(",\n");
        sb.append("\tfather: ").append(father.prettyFormat()).append(",\n");

        sb.append("\tchildren: \n");
        for (Human child : children) {
            String gender = child.getName().endsWith("a") ? "girl" : "boy";
            sb.append("\t\t").append(gender).append(": ").append(child.prettyFormat()).append("\n");
        }

        sb.append("\tpets: [");
        List<Pet> petList = new ArrayList<>(pets);
        for (int i = 0; i < petList.size(); i++) {
            sb.append(petList.get(i).prettyFormat());
            if (i < petList.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");

        return sb.toString();
    }

    // Геттери та сеттери
    public Human getMother() { return mother; }
    public void setMother(Human mother) { this.mother = mother; }
    public Human getFather() { return father; }
    public void setFather(Human father) { this.father = father; }
    public List<Human> getChildren() { return children; }
    public void setChildren(List<Human> children) { this.children = children; }
    public Set<Pet> getPets() { return pets; }
    public void setPets(Set<Pet> pets) { this.pets = pets; }
}

// Сервіс для роботи з сім'ями
class FamilyService {
    private final List<Family> families;

    public FamilyService() {
        this.families = new ArrayList<>();
    }

    public void fillWithTestData() {
        // Створення тестових даних
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            Human mother1 = new Human("Kate", "Bibo", sdf.parse("03/03/1991"), 95,
                    Map.of(DayOfWeek.FRIDAY, "fitness", DayOfWeek.MONDAY, "fitness"));
            Human father1 = new Human("Karl", "Bibo", sdf.parse("10/12/1990"), 90,
                    Map.of(DayOfWeek.FRIDAY, "library", DayOfWeek.MONDAY, "library"));

            Family family1 = new Family(mother1, father1);
            family1.addChild(new Human("Donna", "Bibo", sdf.parse("23/10/2018"), 92, null));
            family1.addChild(new Human("Sun", "Bibo", sdf.parse("23/10/2018"), 92, null));
            family1.addChild(new Human("Kurt", "Kobein", sdf.parse("05/05/2003"), 85,
                    Map.of(DayOfWeek.FRIDAY, "music")));

            family1.getPets().add(new Pet("DOG", "Jack", 3, 35, Set.of("sleep")));
            family1.getPets().add(new Pet("CAT", "Oscar", 5, 81, Set.of("eat", "play")));

            families.add(family1);

            // Додамо ще одну сім'ю для прикладу
            Human mother2 = new Human("Anna", "Smith", sdf.parse("15/05/1985"), 100, null);
            Human father2 = new Human("John", "Smith", sdf.parse("20/08/1980"), 105, null);
            Family family2 = new Family(mother2, father2);
            families.add(family2);

        } catch (Exception e) {
            System.out.println("Помилка при створенні тестових даних: " + e.getMessage());
        }
    }

    public List<Family> getAllFamilies() {
        return families;
    }

    public void displayAllFamilies() {
        if (families.isEmpty()) {
            System.out.println("Немає жодної сім'ї в базі даних.");
            return;
        }

        for (int i = 0; i < families.size(); i++) {
            System.out.println("Сім'я #" + (i + 1));
            System.out.println(families.get(i).prettyFormat());
            System.out.println();
        }
    }

    public List<Family> getFamiliesBiggerThan(int count) {
        List<Family> result = new ArrayList<>();
        for (Family family : families) {
            if (family.countFamily() > count) {
                result.add(family);
            }
        }
        return result;
    }

    public List<Family> getFamiliesLessThan(int count) {
        List<Family> result = new ArrayList<>();
        for (Family family : families) {
            if (family.countFamily() < count) {
                result.add(family);
            }
        }
        return result;
    }

    public int countFamiliesWithMemberNumber(int count) {
        int result = 0;
        for (Family family : families) {
            if (family.countFamily() == count) {
                result++;
            }
        }
        return result;
    }

    public void createNewFamily(Human mother, Human father) {
        Family family = new Family(mother, father);
        families.add(family);
    }

    public boolean deleteFamilyByIndex(int index) {
        if (index >= 0 && index < families.size()) {
            families.remove(index);
            return true;
        }
        return false;
    }

    public Family getFamilyById(int index) {
        if (index >= 0 && index < families.size()) {
            return families.get(index);
        }
        return null;
    }

    public void adoptChild(int familyIndex, Human child) {
        Family family = getFamilyById(familyIndex);
        if (family != null) {
            family.addChild(child);
        }
    }

    public void deleteAllChildrenOlderThan(int age) {
        Calendar now = Calendar.getInstance();
        int currentYear = now.get(Calendar.YEAR);

        for (Family family : families) {
            List<Human> childrenToRemove = new ArrayList<>();

            for (Human child : family.getChildren()) {
                Calendar birthDate = Calendar.getInstance();
                birthDate.setTime(child.getBirthDate());
                int childAge = currentYear - birthDate.get(Calendar.YEAR);

                if (childAge > age) {
                    childrenToRemove.add(child);
                }
            }

            family.getChildren().removeAll(childrenToRemove);
        }
    }
}

// Головний клас програми
public class HappyFamilyApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final FamilyService familyService = new FamilyService();

    public static void main(String[] args) {
        System.out.println("Вітаємо в програмі 'Щаслива родина'!");

        while (true) {
            printMainMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        familyService.fillWithTestData();
                        System.out.println("Тестові дані успішно завантажені.");
                        break;
                    case "2":
                        familyService.displayAllFamilies();
                        break;
                    case "3":
                        displayFamiliesBiggerThan();
                        break;
                    case "4":
                        displayFamiliesLessThan();
                        break;
                    case "5":
                        countFamiliesWithMemberNumber();
                        break;
                    case "6":
                        createNewFamily();
                        break;
                    case "7":
                        deleteFamilyByIndex();
                        break;
                    case "8":
                        editFamilyMenu();
                        break;
                    case "9":
                        deleteChildrenOlderThan();
                        break;
                    case "exit":
                        System.out.println("Дякуємо за використання програми. До побачення!");
                        return;
                    default:
                        System.out.println("Невідома команда. Будь ласка, спробуйте ще раз.");
                }
            } catch (FamilyOverflowException e) {
                System.out.println("Помилка: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Сталася помилка: " + e.getMessage());
            }

            System.out.println("\nНатисніть Enter, щоб продовжити...");
            scanner.nextLine();
        }
    }

    private static void printMainMenu() {
        System.out.println("\nГоловне меню:");
        System.out.println("1. Заповнити тестовими даними");
        System.out.println("2. Відобразити весь список сімей");
        System.out.println("3. Відобразити список сімей, де кількість людей більша за задану");
        System.out.println("4. Відобразити список сімей, де кількість людей менша за задану");
        System.out.println("5. Підрахувати кількість сімей, де кількість членів дорівнює");
        System.out.println("6. Створити нову родину");
        System.out.println("7. Видалити сім'ю за індексом");
        System.out.println("8. Редагувати сім'ю за індексом");
        System.out.println("9. Видалити всіх дітей старше віку");
        System.out.println("exit - Вийти з програми");
        System.out.print("Виберіть опцію: ");
    }

    private static void displayFamiliesBiggerThan() {
        System.out.print("Введіть мінімальну кількість членів сім'ї: ");
        int count = Integer.parseInt(scanner.nextLine());

        List<Family> families = familyService.getFamiliesBiggerThan(count);
        if (families.isEmpty()) {
            System.out.println("Не знайдено сімей з кількістю членів більше " + count);
        } else {
            System.out.println("Сім'ї з кількістю членів більше " + count + ":");
            for (Family family : families) {
                System.out.println(family.prettyFormat());
                System.out.println();
            }
        }
    }

    private static void displayFamiliesLessThan() {
        System.out.print("Введіть максимальну кількість членів сім'ї: ");
        int count = Integer.parseInt(scanner.nextLine());

        List<Family> families = familyService.getFamiliesLessThan(count);
        if (families.isEmpty()) {
            System.out.println("Не знайдено сімей з кількістю членів менше " + count);
        } else {
            System.out.println("Сім'ї з кількістю членів менше " + count + ":");
            for (Family family : families) {
                System.out.println(family.prettyFormat());
                System.out.println();
            }
        }
    }

    private static void countFamiliesWithMemberNumber() {
        System.out.print("Введіть кількість членів сім'ї: ");
        int count = Integer.parseInt(scanner.nextLine());

        int result = familyService.countFamiliesWithMemberNumber(count);
        System.out.println("Кількість сімей з " + count + " членами: " + result);
    }

    private static void createNewFamily() {
        try {
            System.out.println("Створення нової сім'ї:");

            System.out.print("Ім'я матері: ");
            String motherName = scanner.nextLine();
            System.out.print("Прізвище матері: ");
            String motherSurname = scanner.nextLine();
            System.out.print("Рік народження матері: ");
            int motherYear = Integer.parseInt(scanner.nextLine());
            System.out.print("Місяць народження матері (1-12): ");
            int motherMonth = Integer.parseInt(scanner.nextLine());
            System.out.print("День народження матері: ");
            int motherDay = Integer.parseInt(scanner.nextLine());
            System.out.print("IQ матері: ");
            int motherIq = Integer.parseInt(scanner.nextLine());

            Calendar motherBirthDate = Calendar.getInstance();
            motherBirthDate.set(motherYear, motherMonth - 1, motherDay);
            Human mother = new Human(motherName, motherSurname, motherBirthDate.getTime(), motherIq, null);

            System.out.print("Ім'я батька: ");
            String fatherName = scanner.nextLine();
            System.out.print("Прізвище батька: ");
            String fatherSurname = scanner.nextLine();
            System.out.print("Рік народження батька: ");
            int fatherYear = Integer.parseInt(scanner.nextLine());
            System.out.print("Місяць народження батька (1-12): ");
            int fatherMonth = Integer.parseInt(scanner.nextLine());
            System.out.print("День народження батька: ");
            int fatherDay = Integer.parseInt(scanner.nextLine());
            System.out.print("IQ батька: ");
            int fatherIq = Integer.parseInt(scanner.nextLine());

            Calendar fatherBirthDate = Calendar.getInstance();
            fatherBirthDate.set(fatherYear, fatherMonth - Calendar.JANUARY, fatherDay);
            Human father = new Human(fatherName, fatherSurname, fatherBirthDate.getTime(), fatherIq, null);

            familyService.createNewFamily(mother, father);
            System.out.println("Нова сім'я успішно створена!");
        } catch (Exception e) {
            System.out.println("Помилка при створенні сім'ї: " + e.getMessage());
        }
    }

    private static void deleteFamilyByIndex() {
        try {
            System.out.print("Введіть індекс сім'ї для видалення: ");
            int index = Integer.parseInt(scanner.nextLine()) - 1;

            if (familyService.deleteFamilyByIndex(index)) {
                System.out.println("Сім'я успішно видалена.");
            } else {
                System.out.println("Не вдалося знайти сім'ю з таким індексом.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Будь ласка, введіть коректний номер.");
        }
    }

    private static void editFamilyMenu() {
        try {
            System.out.print("Введіть індекс сім'ї для редагування: ");
            int familyIndex = Integer.parseInt(scanner.nextLine()) - 1;

            Family family = familyService.getFamilyById(familyIndex);
            if (family == null) {
                System.out.println("Сім'я з таким індексом не знайдена.");
                return;
            }

            while (true) {
                System.out.println("\nМеню редагування сім'ї:");
                System.out.println("1. Народити дитину");
                System.out.println("2. Усиновити дитину");
                System.out.println("3. Повернутися до головного меню");
                System.out.print("Виберіть опцію: ");

                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        bornChild(familyIndex);
                        break;
                    case "2":
                        adoptChild(familyIndex);
                        break;
                    case "3":
                        return;
                    default:
                        System.out.println("Невідома команда. Будь ласка, спробуйте ще раз.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Будь ласка, введіть коректний номер.");
        }
    }

    private static void bornChild(int familyIndex) {
        try {
            System.out.print("Введіть ім'я для хлопчика: ");
            String boyName = scanner.nextLine();
            System.out.print("Введіть ім'я для дівчинки: ");
            String girlName = scanner.nextLine();

            // Визначаємо стать дитини випадковим чином
            Random random = new Random();
            boolean isBoy = random.nextBoolean();
            String childName = isBoy ? boyName : girlName;

            // Використовуємо прізвище матері
            Family family = familyService.getFamilyById(familyIndex);
            String surname = family.getMother().getSurname();

            // Дата народження - поточна дата
            Human child = new Human(childName, surname, new Date(), 50 + random.nextInt(50), null);

            familyService.adoptChild(familyIndex, child);
            System.out.println("Дитина успішно додана до сім'ї!");
        } catch (FamilyOverflowException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Помилка при додаванні дитини: " + e.getMessage());
        }
    }

    private static void adoptChild(int familyIndex) {
        try {
            System.out.print("Введіть ім'я дитини: ");
            String name = scanner.nextLine();
            System.out.print("Введіть прізвище дитини: ");
            String surname = scanner.nextLine();
            System.out.print("Введіть рік народження: ");
            int year = Integer.parseInt(scanner.nextLine());
            System.out.print("Введіть місяць народження (1-12): ");
            int month = Integer.parseInt(scanner.nextLine());
            System.out.print("Введіть день народження: ");
            int day = Integer.parseInt(scanner.nextLine());
            System.out.print("Введіть IQ дитини: ");
            int iq = Integer.parseInt(scanner.nextLine());

            Calendar birthDate = Calendar.getInstance();
            birthDate.set(year, month - 1, day);

            Human child = new Human(name, surname, birthDate.getTime(), iq, null);
            familyService.adoptChild(familyIndex, child);
            System.out.println("Дитина успішно усиновлена!");
        } catch (FamilyOverflowException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("Помилка при усиновленні дитини: " + e.getMessage());
        }
    }

    private static void deleteChildrenOlderThan() {
        try {
            System.out.print("Введіть вік (усі діти старше цього віку будуть видалені): ");
            int age = Integer.parseInt(scanner.nextLine());

            familyService.deleteAllChildrenOlderThan(age);
            System.out.println("Операція завершена успішно.");
        } catch (NumberFormatException e) {
            System.out.println("Будь ласка, введіть коректний вік.");
        }
    }
}

enum DayOfWeek {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}