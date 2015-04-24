package com.my.sumit.models;

import com.google.api.services.plusDomains.model.Person;

import java.util.List;

/**
 * Created by Ken on 3/16/2015.
 */
public class CurrentFriends {
    private static List<Person> persons;

    public CurrentFriends(List<Person> persons) {
        this.persons = persons;
    }

    public static List<Person> getPersons() {
        return persons;
    }

    public void clear(){
        persons.clear();
    }
}
