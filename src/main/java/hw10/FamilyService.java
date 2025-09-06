package hw10;

import java.io.IOException;
import java.util.*;

public class FamilyService {
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

    public void bornChild(Family family, String masculineName, String feminineName) throws IOException {
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
            // Create a copy to avoid ConcurrentModificationException
            List<Human> childrenToRemove = new ArrayList<>();
            for (Human child : family.getChildren()) {
                if (child.getAge() > age) {
                    childrenToRemove.add(child);
                }
            }
            family.getChildren().removeAll(childrenToRemove);
            familyDao.saveFamily(family);
        });
    }

    public void addFamily(Family family) {
        familyDao.saveFamily(family);
    }

    public List<Family> getFamiliesBiggerThan(int size) {
        List<Family> result = new ArrayList<>();
        for (Family family : familyDao.getAllFamilies()) {
            if (family.count() > size) {
                result.add(family);
            }
        }
        return result;
    }

    public int countFamiliesWithMemberNumber(int size) {
        int count = 0;
        for (Family family : familyDao.getAllFamilies()) {
            if (family.count() == size) {
                count++;
            }
        }
        return count;
    }

    public List<String> getAllChildrenNames() {
        List<String> names = new ArrayList<>();
        for (Family family : familyDao.getAllFamilies()) {
            for (Human child : family.getChildren()) {
                names.add(child.getName());
            }
        }
        Collections.sort(names);
        return names;
    }
}
