package hw4_11;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

class Logger {
    private static final String LOG_FILE = "application.log";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private void write(String level, String message) {
        String logMessage = String.format("%s [%s] %s\n",
                dateFormat.format(new Date()), level, message);

        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(logMessage);

        } catch (IOException e) {
            System.err.println("Помилка запису логу: " + e.getMessage());
        }
    }

    public void info(String message) {
        write("DEBUG", message);
    }

    public void error(String message) {
        write("ERROR", message);
    }
}

class FamilyOverflowException extends RuntimeException {
    public FamilyOverflowException(String message) {
        super(message);
    }
}

class Pet implements Serializable {
    private static final long serialVersionUID = 1L;

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

class Human implements Serializable {
    private static final long serialVersionUID = 2L;

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

        if (schedule != null) {
            this.schedule = new HashMap<>(schedule);
        } else {
            this.schedule = new HashMap<>();
        }
    }

    public String prettyFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String birthDateStr = birthDate != null ? dateFormat.format(birthDate) : "null";
        String scheduleStr = schedule != null && !schedule.isEmpty() ? schedule.toString() : "no schedule";

        return String.format("{name='%s', surname='%s', birthDate='%s', iq=%d, schedule=%s}",
                name, surname, birthDateStr, iq, scheduleStr);
    }

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

class Family implements Serializable {
    private static final long serialVersionUID = 3L;

    private Human mother;
    private Human father;
    private List<Human> children;
    private Set<Pet> pets;
    private static final int MAX_FAMILY_SIZE = 6;

    public Family(Human mother, Human father) {
        this.mother = mother;
        this.father = father;
        this.children = new ArrayList<>();
        this.pets = new HashSet<>();
    }

    public void addChild(Human child) {
        if (children.size() + 2 >= MAX_FAMILY_SIZE) {
            throw new FamilyOverflowException("Family cannot have more than " + MAX_FAMILY_SIZE + " members");
        }
        children.add(child);
    }

    public boolean deleteChild(int index) {
        if (index >= 0 && index < children.size()) {
            children.remove(index);
            return true;
        }
        return false;
    }

    public int countFamily() {
        return 2 + children.size();
    }

    public String prettyFormat() {
        StringBuilder sb = new StringBuilder();
        sb.append("family: \n");
        sb.append("\tmother: ").append(mother.prettyFormat()).append(",\n");
        sb.append("\tfather: ").append(father.prettyFormat()).append(",\n");

        sb.append("\tchildren: \n");
        if (children.isEmpty()) {
            sb.append("\t\tno children\n");
        } else {
            for (Human child : children) {
                String gender = (child.getName().endsWith("a") || child.getName().endsWith("я")) ? "girl" : "boy";
                sb.append("\t\t").append(gender).append(": ").append(child.prettyFormat()).append("\n");
            }
        }

        sb.append("\tpets: ");
        if (pets.isEmpty()) {
            sb.append("no pets\n");
        } else {
            sb.append(pets.stream().map(Pet::prettyFormat).collect(Collectors.toList())).append("\n");
        }

        return sb.toString();
    }

    public Human getMother() { return mother; }
    public void setMother(Human mother) { this.mother = mother; }
    public Human getFather() { return father; }
    public void setFather(Human father) { this.father = father; }
    public List<Human> getChildren() { return children; }
    public void setChildren(List<Human> children) { this.children = children; }
    public Set<Pet> getPets() { return pets; }
    public void setPets(Set<Pet> pets) { this.pets = pets; }
}

interface FamilyDao {
    List<Family> getAllFamilies();
    Family getFamilyById(int index);
    boolean saveFamily(Family family);
    boolean deleteFamily(int index);
    void loadData(List<Family> families);
    void saveDataToFile();
    void loadDataFromFile();
}

class CollectionFamilyDao implements FamilyDao {
    private List<Family> families = new ArrayList<>();
    private final Logger logger = new Logger();
    private final String FILE_NAME = "families.dat";

    @Override
    public List<Family> getAllFamilies() {
        logger.info("DAO: Отримання списку всіх сімей");
        return families;
    }

    @Override
    public Family getFamilyById(int index) {
        if (index >= 0 && index < families.size()) {
            logger.info("DAO: Отримання сім'ї за індексом " + index);
            return families.get(index);
        }
        logger.error("DAO: Сім'ю за індексом " + index + " не знайдено");
        return null;
    }

    @Override
    public boolean saveFamily(Family family) {
        logger.info("DAO: Збереження нової сім'ї");
        families.add(family);
        return true;
    }

    @Override
    public boolean deleteFamily(int index) {
        if (index >= 0 && index < families.size()) {
            logger.info("DAO: Видалення сім'ї за індексом " + index);
            families.remove(index);
            return true;
        }
        logger.error("DAO: Неможливо видалити. Сім'ю за індексом " + index + " не знайдено");
        return false;
    }

    @Override
    public void loadData(List<Family> families) {
        this.families.clear();
        this.families.addAll(families);
        logger.info("DAO: Нові дані завантажено. Кількість сімей: " + families.size());
    }

    @Override
    public void saveDataToFile() {
        logger.info("DAO: Початок збереження даних у файл " + FILE_NAME);
        try (FileOutputStream fos = new FileOutputStream(FILE_NAME);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(families);
            logger.info("DAO: Дані успішно збережено.");

        } catch (IOException e) {
            logger.error("DAO: Помилка збереження даних: " + e.getMessage());
            System.out.println("Помилка збереження файлу: " + e.getMessage());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadDataFromFile() {
        logger.info("DAO: Спроба завантаження даних з файлу " + FILE_NAME);
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            logger.info("DAO: Файл " + FILE_NAME + " не знайдено. Завантаження скасовано.");
            System.out.println("Файл даних ще не існує. Почніть роботу і збережіть дані.");
            return;
        }

        try (FileInputStream fis = new FileInputStream(FILE_NAME);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            Object obj = ois.readObject();
            if (obj instanceof List) {
                List<Family> loadedFamilies = (List<Family>) obj;
                loadData(loadedFamilies);
                logger.info("DAO: Дані успішно завантажено з файлу.");
                System.out.println("Дані успішно завантажено з файлу.");
            }

        } catch (FileNotFoundException e) {
            logger.info("DAO: Файл " + FILE_NAME + " не знайдено.");
            System.out.println("Файл даних не знайдено.");
        } catch (IOException | ClassNotFoundException e) {
            logger.error("DAO: Помилка завантаження даних: " + e.getMessage());
            System.out.println("Помилка читання файлу: " + e.getMessage());
        }
    }
}

class FamilyService {
    private final FamilyDao familyDao;
    private final Logger logger = new Logger();

    public FamilyService() {
        this.familyDao = new CollectionFamilyDao();
    }

    public void loadData(List<Family> families) {
        logger.info("SERVICE: Завантаження даних у DAO");
        familyDao.loadData(families);
    }

    public void saveData() {
        logger.info("SERVICE: Команда на збереження даних");
        familyDao.saveDataToFile();
    }

    public void loadDataFromFile() {
        logger.info("SERVICE: Команда на завантаження даних");
        familyDao.loadDataFromFile();
    }

    public void fillWithTestData() {
        logger.info("SERVICE: Заповнення тестовими даними");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Map<DayOfWeek, String> schedule1 = new HashMap<>();
            schedule1.put(DayOfWeek.FRIDAY, "fitness");
            schedule1.put(DayOfWeek.MONDAY, "fitness");
            Human mother1 = new Human("Kate", "Bibo", sdf.parse("03/03/1991"), 95, schedule1);

            Map<DayOfWeek, String> schedule2 = new HashMap<>();
            schedule2.put(DayOfWeek.FRIDAY, "library");
            schedule2.put(DayOfWeek.MONDAY, "library");
            Human father1 = new Human("Karl", "Bibo", sdf.parse("10/12/1990"), 90, schedule2);

            Family family1 = new Family(mother1, father1);
            family1.addChild(new Human("Donna", "Bibo", sdf.parse("23/10/2018"), 92, null));
            family1.addChild(new Human("Sun", "Bibo", sdf.parse("23/10/2018"), 92, null));

            Map<DayOfWeek, String> schedule3 = new HashMap<>();
            schedule3.put(DayOfWeek.FRIDAY, "music");
            family1.addChild(new Human("Kurt", "Kobein", sdf.parse("05/05/2003"), 85, schedule3));

            family1.getPets().add(new Pet("DOG", "Jack", 3, 35, Set.of("sleep")));
            family1.getPets().add(new Pet("CAT", "Oscar", 5, 81, Set.of("eat", "play")));

            familyDao.saveFamily(family1);

            Human mother2 = new Human("Anna", "Smith", sdf.parse("15/05/1985"), 100, null);
            Human father2 = new Human("John", "Smith", sdf.parse("20/08/1980"), 105, null);
            Family family2 = new Family(mother2, father2);

            familyDao.saveFamily(family2);

            System.out.println("Тестові дані успішно створені.");

        } catch (ParseException e) {
            logger.error("SERVICE: Помилка парсингу дати при створенні тестових даних: " + e.getMessage());
            System.out.println("Помилка при створенні тестових даних: " + e.getMessage());
        } catch (Exception e) {
            logger.error("SERVICE: Інша помилка при створенні тестових даних: " + e.getMessage());
            System.out.println("Помилка при створенні тестових даних: " + e.getMessage());
        }
    }

    public List<Family> getAllFamilies() {
        logger.info("SERVICE: Отримання списку всіх сімей");
        return familyDao.getAllFamilies();
    }

    public void displayAllFamilies() {
        logger.info("SERVICE: Відображення всіх сімей");
        List<Family> families = familyDao.getAllFamilies();
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
        logger.info("SERVICE: Пошук сімей, більших за " + count);
        return familyDao.getAllFamilies().stream()
                .filter(family -> family.countFamily() > count)
                .collect(Collectors.toList());
    }

    public List<Family> getFamiliesLessThan(int count) {
        logger.info("SERVICE: Пошук сімей, менших за " + count);
        return familyDao.getAllFamilies().stream()
                .filter(family -> family.countFamily() < count)
                .collect(Collectors.toList());
    }

    public int countFamiliesWithMemberNumber(int count) {
        logger.info("SERVICE: Підрахуток сімей з кількістю членів " + count);
        return (int) familyDao.getAllFamilies().stream()
                .filter(family -> family.countFamily() == count)
                .count();
    }

    public void createNewFamily(Human mother, Human father) {
        logger.info("SERVICE: Створення нової сім'ї");
        Family family = new Family(mother, father);
        familyDao.saveFamily(family);
    }

    public boolean deleteFamilyByIndex(int index) {
        logger.info("SERVICE: Видалення сім'ї за індексом " + index);
        return familyDao.deleteFamily(index);
    }

    public Family getFamilyById(int index) {
        logger.info("SERVICE: Отримання сім'ї за індексом " + index);
        return familyDao.getFamilyById(index);
    }

    public void adoptChild(int familyIndex, Human child) {
        Family family = familyDao.getFamilyById(familyIndex);
        if (family != null) {
            logger.info("SERVICE: Усиновлення дитини в сім'ю " + familyIndex);
            family.addChild(child);
        } else {
            logger.error("SERVICE: Не вдалося усиновити дитину, сім'ю " + familyIndex + " не знайдено");
        }
    }

    public void deleteAllChildrenOlderThan(int age) {
        logger.info("SERVICE: Видалення всіх дітей, старших за " + age);
        Calendar now = Calendar.getInstance();
        int currentYear = now.get(Calendar.YEAR);

        List<Family> allFamilies = familyDao.getAllFamilies();

        for (Family family : allFamilies) {
            List<Human> childrenToRemove = new ArrayList<>();
            for (Human child : family.getChildren()) {
                Calendar birthDate = Calendar.getInstance();
                birthDate.setTime(child.getBirthDate());
                int childAge = currentYear - birthDate.get(Calendar.YEAR);

                if (childAge > age) {
                    childrenToRemove.add(child);
                }
            }
            if (!childrenToRemove.isEmpty()) {
                logger.info("SERVICE: Видалення " + childrenToRemove.size() + " дітей з сім'ї");
                family.getChildren().removeAll(childrenToRemove);
            }
        }
    }
}

public class HappyFamilyApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final FamilyService familyService = new FamilyService();
    private static final Logger logger = new Logger();

    public static void main(String[] args) {
        System.out.println("Вітаємо в програмі 'Щаслива родина'!");

        while (true) {
            printMainMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        familyService.loadDataFromFile();
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
                    case "10":
                        familyService.saveData();
                        System.out.println("Дані успішно збережено у файл.");
                        break;
                    case "11":
                        familyService.fillWithTestData();
                        break;
                    case "exit":
                        System.out.println("Дякуємо за використання програми. До побачення!");
                        return;
                    default:
                        System.out.println("Невідома команда. Будь ласка, спробуйте ще раз.");
                }
            } catch (FamilyOverflowException e) {
                logger.error("Переповнення сім'ї: " + e.getMessage());
                System.out.println("Помилка: " + e.getMessage());
            } catch (Exception e) {
                logger.error("Неочікувана помилка: " + e.getMessage());
                System.out.println("Сталася помилка: " + e.getMessage());
            }

            System.out.println("\nНатисніть Enter, щоб продовжити...");
            scanner.nextLine();
        }
    }

    private static void printMainMenu() {
        System.out.println("\nГоловне меню:");
        System.out.println("1. Завантажити дані з файлу");
        System.out.println("2. Відобразити весь список сімей");
        System.out.println("3. Відобразити список сімей, де кількість людей більша за задану");
        System.out.println("4. Відобразити список сімей, де кількість людей менша за задану");
        System.out.println("5. Підрахувати кількість сімей, де кількість членів дорівнює");
        System.out.println("6. Створити нову родину");
        System.out.println("7. Видалити сім'ю за індексом");
        System.out.println("8. Редагувати сім'ю за індексом");
        System.out.println("9. Видалити всіх дітей старше віку");
        System.out.println("10. Зберегти дані у файл");
        System.out.println("11. Заповнити тестовими даними (ОБЕРЕЖНО: додасть дублікати, якщо дані вже є)");
        System.out.println("exit - Вийти з програми");
        System.out.print("Виберіть опцію: ");
    }

    private static void displayFamiliesBiggerThan() {
        try {
            System.out.print("Введіть мінімальну кількість членів сім'ї: ");
            int count = Integer.parseInt(scanner.nextLine());

            List<Family> families = familyService.getFamiliesBiggerThan(count);
            if (families.isEmpty()) {
                System.out.println("Не знайдено сімей з кількістю членів більше " + count);
            } else {
                System.out.println("Сім'ї з кількістю членів більше " + count + ":");
                families.forEach(f -> System.out.println(f.prettyFormat() + "\n"));
            }
        } catch (NumberFormatException e) {
            logger.error("Некоректне введення (displayFamiliesBiggerThan): " + e.getMessage());
            System.out.println("Будь ласка, введіть коректне число.");
        }
    }

    private static void displayFamiliesLessThan() {
        try {
            System.out.print("Введіть максимальну кількість членів сім'ї: ");
            int count = Integer.parseInt(scanner.nextLine());

            List<Family> families = familyService.getFamiliesLessThan(count);
            if (families.isEmpty()) {
                System.out.println("Не знайдено сімей з кількістю членів менше " + count);
            } else {
                System.out.println("Сім'ї з кількістю членів менше " + count + ":");
                families.forEach(f -> System.out.println(f.prettyFormat() + "\n"));
            }
        } catch (NumberFormatException e) {
            logger.error("Некоректне введення (displayFamiliesLessThan): " + e.getMessage());
            System.out.println("Будь ласка, введіть коректне число.");
        }
    }

    private static void countFamiliesWithMemberNumber() {
        try {
            System.out.print("Введіть кількість членів сім'ї: ");
            int count = Integer.parseInt(scanner.nextLine());

            int result = familyService.countFamiliesWithMemberNumber(count);
            System.out.println("Кількість сімей з " + count + " членами: " + result);
        } catch (NumberFormatException e) {
            logger.error("Некоректне введення (countFamiliesWithMemberNumber): " + e.getMessage());
            System.out.println("Будь ласка, введіть коректне число.");
        }
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
            fatherBirthDate.set(fatherYear, fatherMonth - 1, fatherDay);
            Human father = new Human(fatherName, fatherSurname, fatherBirthDate.getTime(), fatherIq, null);

            familyService.createNewFamily(mother, father);
            System.out.println("Нова сім'я успішно створена!");

        } catch (NumberFormatException e) {
            logger.error("Некоректне введення (createNewFamily): " + e.getMessage());
            System.out.println("Помилка: Введено некоректне число. Спробуйте ще раз.");
        } catch (Exception e) {
            logger.error("Помилка створення сім'ї: " + e.getMessage());
            System.out.println("Помилка при створенні сім'ї: " + e.getMessage());
        }
    }

    private static void deleteFamilyByIndex() {
        try {
            System.out.print("Введіть індекс сім'ї для видалення (починаючи з 1): ");
            int index = Integer.parseInt(scanner.nextLine()) - 1;

            if (familyService.deleteFamilyByIndex(index)) {
                System.out.println("Сім'я успішно видалена.");
            } else {
                System.out.println("Не вдалося знайти сім'ю з таким індексом.");
            }
        } catch (NumberFormatException e) {
            logger.error("Некоректне введення (deleteFamilyByIndex): " + e.getMessage());
            System.out.println("Будь ласка, введіть коректний номер.");
        }
    }

    private static void editFamilyMenu() {
        try {
            System.out.print("Введіть індекс сім'ї для редагування (починаючи з 1): ");
            int familyIndex = Integer.parseInt(scanner.nextLine()) - 1;

            Family family = familyService.getFamilyById(familyIndex);
            if (family == null) {
                System.out.println("Сім'я з таким індексом не знайдена.");
                return;
            }

            while (true) {
                System.out.println("\nМеню редагування сім'ї #" + (familyIndex + 1));
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
                        System.out.println("Невідома команда.");
                }
            }
        } catch (NumberFormatException e) {
            logger.error("Некоректне введення (editFamilyMenu): " + e.getMessage());
            System.out.println("Будь ласка, введіть коректний номер.");
        }
    }

    private static void bornChild(int familyIndex) {
        Family family = familyService.getFamilyById(familyIndex);
        if (family == null) return;

        System.out.print("Введіть ім'я для хлопчика: ");
        String boyName = scanner.nextLine();
        System.out.print("Введіть ім'я для дівчинки: ");
        String girlName = scanner.nextLine();

        Random random = new Random();
        boolean isBoy = random.nextBoolean();
        String childName = isBoy ? boyName : girlName;
        String surname = family.getFather().getSurname();
        int iq = (family.getMother().getIq() + family.getFather().getIq()) / 2;

        Human child = new Human(childName, surname, new Date(), iq, null);

        familyService.adoptChild(familyIndex, child);
        System.out.println("Дитина успішно додана до сім'ї!");
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

        } catch (NumberFormatException e) {
            logger.error("Некоректне введення (adoptChild): " + e.getMessage());
            System.out.println("Помилка: Введено некоректне число. Спробуйте ще раз.");
        }
    }

    private static void deleteChildrenOlderThan() {
        try {
            System.out.print("Введіть вік (усі діти старше цього віку будуть видалені): ");
            int age = Integer.parseInt(scanner.nextLine());

            familyService.deleteAllChildrenOlderThan(age);
            System.out.println("Операція завершена успішно.");
        } catch (NumberFormatException e) {
            logger.error("Некоректне введення (deleteChildrenOlderThan): " + e.getMessage());
            System.out.println("Будь ласка, введіть коректний вік.");
        }
    }
}
enum DayOfWeek {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}