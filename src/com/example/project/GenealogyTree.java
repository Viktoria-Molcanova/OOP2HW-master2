package com.example.project;

import java.io.Serializable;
import java.util.*;
//Исправила. Теперь коллекция GenealogyTree содержит список всех членов семьи, включая корневой элемент.
// Методы сортировки и печати теперь работают с этим списком, позволяет отображать и сортировать всех членов семьи.
// Добавлен метод getAllPersons(), который возвращает список всех членов семьи.

public class GenealogyTree<T> implements Iterable<T>, Serializable {
    private final T root;
    private final List<T> allPersons;

    public GenealogyTree(T root) {
        this.root = root;
        this.allPersons = new ArrayList<>();
        populateAllPersons(root);
    }

    private void populateAllPersons(T person) {
        allPersons.add(person);
        if (person instanceof Person) {
            List<Person> children = ((Person) person).getChildren();
            for (Person child : children) {
                populateAllPersons((T) child);
            }
        }
    }

    public List<T> getAllPersons() {
        return allPersons;
    }

    public List<Person> getChildrenOfPerson(String personName) throws PersonNotFoundException {
        Person person = findPersonByName(personName);
        if (person == null) {
            throw new PersonNotFoundException("Член семьи не найден: " + personName);
        }
        return person.getChildren();
    }

    public void removePerson(String personName) throws PersonNotFoundException {
        Person person = findPersonByName(personName);
        if (person == null) {
            throw new PersonNotFoundException("Член семьи не найден: " + personName);
        }
        Person father = person.getFather();
        Person mother = person.getMother();
        if (father != null) {
            father.getChildren().remove(person);
        }
        if (mother != null) {
            mother.getChildren().remove(person);
        }
    }

    public List<Person> getSortedByBirthDate() {
        List<Person> sortedList = new ArrayList<>();
        for (T person : allPersons) {
            if (person instanceof Person) {
                sortedList.add((Person) person);
            }
        }
        sortedList.sort(Comparator.comparing(Person::getBirthDate));
        return sortedList;
    }

    public List<Person> getSortedByName() {
        List<Person> sortedList = new ArrayList<>();
        for (T person : allPersons) {
            if (person instanceof Person) {
                sortedList.add((Person) person);
            }
        }
        sortedList.sort(Comparator.comparing(Person::getName));
        return sortedList;
    }

    public void printGenealogyTreeSortedByBirthDate() {
        List<Person> sortedList = getSortedByBirthDate();
        printSortedPerson(sortedList, 0);
    }

    public void printGenealogyTreeSortedByName() {
        List<Person> sortedList = getSortedByName();
        printSortedPerson(sortedList, 0);
    }

    private void printSortedPerson(List<Person> sortedList, int level) {
        sortedList.forEach(person -> {
            StringBuilder indent = new StringBuilder();
            for (int i = 0; i < level; i++) {
                indent.append("  ");
            }
            System.out.println(indent.toString() + person.getName());
            printSortedPerson(person.getChildren(), level + 1);
        });
    }

    private Person findPersonByName(String personName) {
        for (T person : allPersons) {
            if (person instanceof Person && ((Person) person).getName().equals(personName)) {
                return (Person) person;
            }
        }
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return allPersons.iterator();
    }
}
