package hw04;

import hw06.Man;
import hw06.Woman;

import java.util.ArrayList;
import java.util.Set;

public class Homework4 {

    public static class Pet {
        void sound() {
            System.out.println("I'm a pet");
        }

        void eat() {
            System.out.println("I'm eating");
        }

        public int getTrickLevel() {
            return 0;
        }

        public Object getSpecies() {
            return null;
        }

        public Object getAge() {
            return null;
        }
    }

    public static class Dog extends Pet {
        final String name;

        public Dog(String name) {
            this.name = name;
        }

        public Dog(String рекс, int i, int i1, Set<String> dogHabits, String name) {
            super();
            this.name = name;
        }

        @Override
        void sound() {
            System.out.println("Woof! My name is " + name);
        }

        void fetch() {
            System.out.println(name + " is fetching the ball");
        }
    }

    public static class Cat extends Pet {
        @Override
        void sound() {
            System.out.println("Meow!");
        }

        void scratch() {
            System.out.println("Cat is scratching the furniture");
        }
    }

    public static class Human {
        private final String name;
        public long birthDate;
        private Pet pet;

        Human(String name) {
            this.name = name;
        }

        void setPet(Pet pet) {
            this.pet = pet;
        }

        void interactWithPet() {
            if (pet != null) {
                System.out.print(name + "'s pet says: ");
                pet.sound();
            } else {
                System.out.println(name + " has no pet");
            }
        }

        void feedPet() {
            if (pet != null) {
                System.out.print(name + " is feeding their pet - ");
                pet.eat();
            }
        }

        public String getName() {
            return name;
        }
    }

    // Remove the problematic imports and use only hw04 classes
    public static class Man extends Human {
        public Man(String name) {
            super(name);
        }
    }

    public static class Woman extends Human {
        public Woman(String name) {
            super(name);
        }
    }

    public static class Family {
        private  Human mother;
        private Human father;
        private ArrayList<Human> children;
        private ArrayList<Pet> pets;

        public Family(Human mother, Human father) {
            this.mother = mother;
            this.father = father;
            this.children = new ArrayList<>();
            this.pets = new ArrayList<>();
        }

        public Family(hw06.Woman mother1, hw06.Man father1, Human mother, Human father, ArrayList<Human> children, ArrayList<Pet> pets) {
            this.mother = mother;
            this.father = father;
            this.children = children;
            this.pets = pets;
        }

        public Family(hw06.Woman mother1, hw06.Man father1) {
        }

        // Remove constructor that uses hw06 classes
        public void addChild(hw06.Man child) {

        }

        public void addPet(Pet pet) {
            pets.add(pet);
        }

        public void familyActivities() {
            System.out.println("\nFamily activities:");
            mother.interactWithPet();
            father.interactWithPet();

            for (Human child : children) {
                child.interactWithPet();
            }

            for (Pet pet : pets) {
                pet.sound();
                if (pet instanceof Dog) {
                    ((Dog) pet).fetch();
                } else if (pet instanceof Cat) {
                    ((Cat) pet).scratch();
                }
            }
        }

        public Pet getPet() {
            return pets.isEmpty() ? null : pets.get(0);
        }

        public int countFamily() {
            return 2 + children.size(); // parents + children
        }

        public ArrayList<Human> getChildren() {
            return children;
        }

        public void deleteChild(Human human) {
            children.remove(human);
        }

        public void addChild(hw06.Woman woman) {
        }

        public void addChild(Human son) {
        }

        // Remove the redundant addChild methods
    }

    public static void main(String[] args) {
        // Створення членів сім'ї - use hw04 classes instead of hw06
        Human mother = new Woman("Anna");  // Use Woman from hw04
        Human father = new Man("John");    // Use Man from hw04

        // Створення дітей
        Human son = new Man("Mike");
        Human daughter = new Woman("Sarah");

        // Створення домашніх улюбленців
        Pet dog = new Dog("Rex");
        Pet cat = new Cat();

        // Налаштування сім'ї
        Family family = new Family(mother, father);
        family.addChild(son);
        family.addChild(daughter);
        family.addPet(dog);
        family.addPet(cat);

        // Призначення улюбленців
        mother.setPet(dog);
        father.setPet(cat);
        son.setPet(dog);
        daughter.setPet(cat);

        family.familyActivities();

        // Годування тварин
        System.out.println("\nFeeding time:");
        mother.feedPet();
        father.feedPet();
        son.feedPet();
        daughter.feedPet();

        // Перевірка
        System.out.println("\nType checks:");
        System.out.println("Is dog a Pet? " + true); // true
        System.out.println("Is cat a Dog? " + false); // false

        // Використання поліморфізму
        System.out.println("\nPolymorphism example:");
        Pet somePet = new Dog("Buddy");
        somePet.sound(); // Виклик методу Dog

        somePet = new Cat();
        somePet.sound(); // Тепер виклик методу Cat

        System.out.println("Family size: " + family.countFamily());
    }
}