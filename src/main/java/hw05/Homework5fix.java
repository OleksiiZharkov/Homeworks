package hw05;
import java.util.*;
import java.util.stream.Collectors;
public class Homework5fix {

    enum Species {
        CAT, DOG, BIRD, FISH, HAMSTER
    }

    enum DayOfWeek {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;

        public String getDisplayName() {
            return name().charAt(0) + name().substring(1).toLowerCase();
        }
    }

    static class Pet {
        private final Species species;
        private final String nickname;
        private final int age;
        private final int trickLevel;
        private final Set<String> habits;

        public Pet(Species species, String nickname, int age, int trickLevel, Set<String> habits) {
            this.species = Objects.requireNonNull(species);
            this.nickname = Objects.requireNonNull(nickname);
            this.age = age;
            this.trickLevel = trickLevel;
            this.habits = Collections.unmodifiableSet(new HashSet<>(habits));
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
            System.out.printf("%s %s is eating\n", species, nickname);
        }

        public void respond() {
            System.out.printf("%s %s says: Hello owner!\n", species, nickname);
        }

        @Override
        public String toString() {
            return String.format("%s{nickname='%s', age=%d, trickLevel=%d, habits=%s}",
                    species, nickname, age, trickLevel, habits);
        }
    }

    static class Human {
        private final String name;
        private final String surname;
        private final int birthYear;
        private final int iq;
        private final Map<DayOfWeek, String> schedule;
        private Family family;

        public Human(String name, String surname, int birthYear) {
            this(name, surname, birthYear, 100, new EnumMap<>(DayOfWeek.class));
        }

        public Human(String name, String surname, int birthYear, int iq, Map<DayOfWeek, String> schedule) {
            this.name = Objects.requireNonNull(name);
            this.surname = Objects.requireNonNull(surname);
            this.birthYear = birthYear;
            this.iq = iq;
            this.schedule = new EnumMap<>(schedule);
        }

        public String getName() {
            return name;
        }

        public String getSurname() {
            return surname;
        }

        public int getBirthYear() {
            return birthYear;
        }

        public int getIq() {
            return iq;
        }

        public Map<DayOfWeek, String> getSchedule() {
            return Collections.unmodifiableMap(schedule);
        }

        public Family getFamily() {
            return family;
        }

        void setFamily(Family family) {
            this.family = family;
        }

        public void greetPet() {
            if (family != null && family.getPet() != null) {
                System.out.printf("%s: Hello, %s!\n", name, family.getPet().getNickname());
            }
        }

        public void describePet() {
            if (family != null && family.getPet() != null) {
                Pet pet = family.getPet();
                String cleverness = pet.getTrickLevel() > 50 ? "very clever" : "not very clever";
                System.out.printf("%s's pet %s is %d years old, it's %s\n",
                        name, pet.getNickname(), pet.getAge(), cleverness);
            }
        }

        @Override
        public String toString() {
            return String.format("Human{name='%s', surname='%s', birthYear=%d, iq=%d}",
                    name, surname, birthYear, iq);
        }
    }

    static class Family {
        private final Human mother;
        private final Human father;
        private final List<Human> children;
        private Pet pet;

        public Family(Human mother, Human father) {
            this.mother = Objects.requireNonNull(mother);
            this.father = Objects.requireNonNull(father);
            this.children = new ArrayList<>();
            this.mother.setFamily(this);
            this.father.setFamily(this);
        }

        public Human getMother() {
            return mother;
        }

        public Human getFather() {
            return father;
        }

        public List<Human> getChildren() {
            return Collections.unmodifiableList(children);
        }

        public Pet getPet() {
            return pet;
        }

        public void setPet(Pet pet) {
            this.pet = pet;
        }

        public void addChild(Human child) {
            children.add(Objects.requireNonNull(child));
            child.setFamily(this);
        }

        public void deleteChild(Human child) {
            boolean removed = children.remove(child);
            if (removed) {
                child.setFamily(null);
            }
        }

        public Human deleteChild(int index) {
            if (index < 0 || index >= children.size()) {
                return null;
            }
            Human child = children.remove(index);
            child.setFamily(null);
            return child;
        }

        public int countFamilyMembers() {
            return 2 + children.size() + (pet != null ? 1 : 0);
        }

        @Override
        public String toString() {
            return String.format("Family{\n  mother=%s,\n  father=%s,\n  children=%s,\n  pet=%s\n}",
                    mother, father,
                    children.stream().map(Human::toString).collect(Collectors.joining(",\n    ")),
                    pet != null ? pet : "none");
        }
    }

    public static class HappyFamily {
        public static void main(String[] args) {
            // 1. Create pets
            Set<String> catHabits = Set.of("sleep", "eat", "play");
            Pet cat = new Pet(Species.CAT, "Whiskers", 3, 75, catHabits);

            Set<String> dogHabits = Set.of("bark", "run", "play");
            Pet dog = new Pet(Species.DOG, "Buddy", 5, 90, dogHabits);

            // 2. Create schedule
            Map<DayOfWeek, String> parentSchedule = new EnumMap<>(DayOfWeek.class);
            parentSchedule.put(DayOfWeek.MONDAY, "Work");
            parentSchedule.put(DayOfWeek.TUESDAY, "Gym");
            parentSchedule.put(DayOfWeek.WEDNESDAY, "Meetings");
            parentSchedule.put(DayOfWeek.THURSDAY, "Shopping");
            parentSchedule.put(DayOfWeek.FRIDAY, "Movie night");

            // 3. Create humans
            Human mother = new Human("Anna", "Smith", 1985, 110, parentSchedule);
            Human father = new Human("John", "Smith", 1980, 120, parentSchedule);

            Human child1 = new Human("Emma", "Smith", 2010);
            Human child2 = new Human("Michael", "Smith", 2012);
            Human child3 = new Human("Sophia", "Smith", 2015);

            // 4. Create family
            Family smithFamily = new Family(mother, father);
            smithFamily.setPet(cat);
            smithFamily.addChild(child1);
            smithFamily.addChild(child2);
            smithFamily.addChild(child3);

            // 5. Demonstrate functionality
            System.out.println("=== Family Information ===");
            System.out.println(smithFamily);
            System.out.println("Total family members: " + smithFamily.countFamilyMembers());

            System.out.println("\n=== Interactions ===");
            mother.greetPet();
            father.describePet();
            cat.eat();
            dog.respond();

            // 6. Modify family
            System.out.println("\n=== Family Changes ===");
            smithFamily.deleteChild(child2);
            smithFamily.setPet(dog);
            System.out.println("After changes:\n" + smithFamily);
            System.out.println("Total family members now: " + smithFamily.countFamilyMembers());
        }
    }
}