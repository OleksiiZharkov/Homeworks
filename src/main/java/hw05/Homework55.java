package hw05;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import java.lang.ref.Cleaner;
import java.util .*;
import static org.junit.Assert.assertEquals;
@RunWith(Enclosed.class)
public class Homework55 {

    // Enums
    enum Species {
        CAT, DOG, BIRD, FISH, HAMSTER, TURTLE, RABBIT
    }

    enum DayOfWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    // Pet class with Cleaner
    static class Pet {
        private static final Cleaner cleaner = Cleaner.create();

        private final Species species;
        private final String nickname;
        private final int age;
        private final int trickLevel;
        private final Set<String> habits;

        public Pet(Species species, String nickname, int age, int trickLevel, Set<String> habits) {
            this.species = species;
            this.nickname = nickname;
            this.age = age;
            this.trickLevel = trickLevel;
            this.habits = habits;

            Cleaner.Cleanable cleanable = cleaner.register(this, () -> {
                System.out.printf("Pet object %s (%s) is being cleaned%n", nickname, species);
            });
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

        public void eat() {
            System.out.println("Я їм!");
        }

        public void respond() {
            System.out.printf("Привіт, хазяїн. Я - %s. Я скучив!%n", nickname);
        }

        public void foul() {
            System.out.println("Потрібно добре замести сліди...");
        }

        @Override
        public String toString() {
            return String.format("%s{nickname='%s', age=%d, trickLevel=%d, habits=%s}",
                    species, nickname, age, trickLevel, habits);
        }
    }

    // Human class with Cleaner
    static class Human {
        private static final Cleaner cleaner = Cleaner.create();

        private final String name;
        private final String surname;
        private final int year;
        private int iq;
        private Map<DayOfWeek, String> schedule;
        private Family family;

        public Human(String name, String surname, int year) {
            this.name = name;
            this.surname = surname;
            this.year = year;

            Cleaner.Cleanable cleanable = cleaner.register(this, () -> {
                System.out.printf("Human object %s %s is being cleaned%n", name, surname);
            });
        }

        public Human(String name, String surname, int year, int iq, Map<DayOfWeek, String> schedule) {
            this(name, surname, year);
            this.iq = iq;
            this.schedule = schedule;
        }

        public String getName() {
            return name;
        }

        public String getSurname() {
            return surname;
        }

        public int getYear() {
            return year;
        }

        public int getIq() {
            return iq;
        }

        public Map<DayOfWeek, String> getSchedule() {
            return schedule;
        }

        public Family getFamily() {
            return family;
        }

        public void setFamily(Family family) {
            this.family = family;
        }

        public void greetPet() {
            System.out.printf("Привіт, %s%n", family.getPet().getNickname());
        }

        public void describePet() {
            String trickLevel = family.getPet().getTrickLevel() > 50 ? "дуже хитрий" : "майже не хитрий";
            System.out.printf("У мене є %s, їй %d років, він %s%n",
                    family.getPet().getSpecies(), family.getPet().getAge(), trickLevel);
        }

        @Override
        public String toString() {
            return String.format("Human{name='%s', surname='%s', year=%d, iq=%d, schedule=%s}",
                    name, surname, year, iq, schedule);
        }
    }

    // Family class with Cleaner
    static class Family {
        private static final Cleaner cleaner = Cleaner.create();

        private final Human mother;
        private final Human father;
        private Human[] children;
        private Pet pet;

        public Family(Human mother, Human father) {
            this.mother = mother;
            this.father = father;
            this.children = new Human[0];
            mother.setFamily(this);
            father.setFamily(this);

            Cleaner.Cleanable cleanable = cleaner.register(this, () -> {
                System.out.printf("Family object with %d members is being cleaned%n", countFamily());
            });
        }

        public Human getMother() {
            return mother;
        }

        public Human getFather() {
            return father;
        }

        public Human[] getChildren() {
            return children;
        }

        public Pet getPet() {
            return pet;
        }

        public void setPet(Pet pet) {
            this.pet = pet;
        }

        public void addChild(Human child) {
            child.setFamily(this);
            Human[] newChildren = Arrays.copyOf(children, children.length + 1);
            newChildren[newChildren.length - 1] = child;
            children = newChildren;
        }

        public void deleteChild(int index) {
            if (index < 0 || index >= children.length) {
                return;
            }

            Human[] newChildren = new Human[children.length - 1];
            System.arraycopy(children, 0, newChildren, 0, index);
            System.arraycopy(children, index + 1, newChildren, index, children.length - index - 1);
            children = newChildren;
        }

        public boolean deleteChild(Human child) {
            for (int i = 0; i < children.length; i++) {
                if (children[i].equals(child)) {
                    deleteChild(i);
                    return true;
                }
            }
            return false;
        }

        public int countFamily() {
            return 2 + children.length + (pet != null ? 1 : 0);
        }

        @Override
        public String toString() {
            return String.format("Family{mother=%s %s, father=%s %s, children=%s, pet=%s}",
                    mother.getName(), mother.getSurname(),
                    father.getName(), father.getSurname(),
                    Arrays.toString(children),
                    pet != null ? pet.getNickname() : "null");
        }
    }

    // Main class
    public static class HappyFamily {
        public static void main(String[] args) {
            // Create pet
            Set<String> habits = new HashSet<>(Arrays.asList("спати", "їсти"));
            Pet cat = new Pet(Species.CAT, "Мурзик", 3, 75, habits);

            // Create schedule
            Map<DayOfWeek, String> schedule = new EnumMap<>(DayOfWeek.class);
            schedule.put(DayOfWeek.MONDAY, "Працювати");
            schedule.put(DayOfWeek.TUESDAY, "Вчитися");
            schedule.put(DayOfWeek.WEDNESDAY, "Спорт");
            schedule.put(DayOfWeek.THURSDAY, "Відпочинок");
            schedule.put(DayOfWeek.FRIDAY, "Кіно");
            schedule.put(DayOfWeek.SATURDAY, "Шопінг");
            schedule.put(DayOfWeek.SUNDAY, "Зустрічі");

            // Create parents
            Human mother = new Human("Олена", "Петренко", 1980, 110, schedule);
            Human father = new Human("Петро", "Петренко", 1978, 120, schedule);

            // Create family
            Family petrenkoFamily = new Family(mother, father);
            petrenkoFamily.setPet(cat);

            // Add children
            Human child1 = new Human("Анна", "Петренко", 2005);
            Human child2 = new Human("Іван", "Петренко", 2007);
            petrenkoFamily.addChild(child1);
            petrenkoFamily.addChild(child2);

            System.out.println(petrenkoFamily);
            System.out.println("Розмір сім'ї: " + petrenkoFamily.countFamily());

            // Test child removal
            petrenkoFamily.deleteChild(0);
            System.out.println("Після видалення першої дитини: " + petrenkoFamily.countFamily());

            // Test garbage collection
            System.out.println("Створення об'єктів для тестування очищення...");
            for (int i = 0; i < 100000; i++) {
                new Human("Temp", "Human" + i, 2000);
            }
            System.gc(); // Suggest GC to run

            // Demonstrate methods
            mother.greetPet();
            mother.describePet();
        }
    }

    // Test classes
    public static class happyFamily {
        private Human createSampleHuman() {
            Map<DayOfWeek, String> schedule = new EnumMap<>(DayOfWeek.class);
            schedule.put(DayOfWeek.MONDAY, "Work");
            return new Human("Test", "Human", 1990, 100, schedule);
        }

        private Pet createSamplePet() {
            Set<String> habits = new HashSet<>(List.of("sleep"));
            return new Pet(Species.DOG, "Buddy", 5, 60, habits);
        }

        @Test
        public void testAddChild() {
            Family family = new Family(createSampleHuman(), createSampleHuman());
            Human child = createSampleHuman();

            int initialSize = family.getChildren().length;
            family.addChild(child);

            assertEquals(initialSize + 1, family.getChildren().length);
            assertEquals(child, family.getChildren()[family.getChildren().length - 1]);
            assertEquals(family, child.getFamily());
        }

        // ... other test methods
    }

    public static class HumanTest {
        @Test
        public void testToString() {
            Map<DayOfWeek, String> schedule = new EnumMap<>(DayOfWeek.class);
            schedule.put(DayOfWeek.MONDAY, "Work");
            Human human = new Human("John", "Doe", 1990, 100, schedule);

            String expected = "Human{name='John', surname='Doe', year=1990, iq=100, schedule={MONDAY=Work}}";
            assertEquals(expected, human.toString());
        }
    }

    public static class PetTest {
        @Test
        public void testToString() {
            Set<String> habits = new HashSet<>(List.of("sleep"));
            Pet pet = new Pet(Species.CAT, "Whiskers", 3, 70, habits);

            String expected = "CAT{nickname='Whiskers', age=3, trickLevel=70, habits=[sleep]}";
            assertEquals(expected, pet.toString().replaceAll("\\d+", "3"));
        }
    }
}
