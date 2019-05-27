package com.aier.ardemo.bean;

public class GloData {

    public static Person person;

    public static Person getPerson() {
        return person;
    }

    public static void setPersons(Person person) {
        GloData.person = person;
    }
}
