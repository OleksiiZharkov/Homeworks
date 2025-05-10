package hw04;

import java.util.ArrayList;

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

        Dog(String name) {
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
    }

    public static class Family {
        private final Human mother;
        private final Human father;
        private final ArrayList<Human> children;
        private final ArrayList<Pet> pets;

        public Family(Human mother, Human father) {
            this.mother = mother;
            this.father = father;
            this.children = new ArrayList<>();
            this.pets = new ArrayList<>();
        }

        public void addChild(Human child) {
            children.add(child);
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
            return null;
        }
    }

    public static void main(String[] args) {
        // Створення членів сім'ї
        Human mother = new Human("Anna");
        Human father = new Human("John");

        // Створення дітей
        Human son = new Human("Mike");
        Human daughter = new Human("Sarah");

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
        System.out.println("Is dog a Pet? " + (dog instanceof Pet)); // true
        System.out.println("Is cat a Dog? " + (cat instanceof Dog)); // false

        // Використання поліморфізму
        System.out.println("\nPolymorphism example:");
        Pet somePet = new Dog("Buddy");
        somePet.sound(); // Виклик методу Dog

        somePet = new Cat();
        somePet.sound(); // Тепер виклик методу Cat
    }
}

