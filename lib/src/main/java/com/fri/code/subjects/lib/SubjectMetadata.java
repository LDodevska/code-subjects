package com.fri.code.subjects.lib;

import java.util.ArrayList;
import java.util.List;

public class SubjectMetadata {

    public Integer ID;
    public String name;

    //User IDs
    public List<Integer> users = new ArrayList<Integer>();

    public List<Integer> getUsers() {
        return users;
    }

    public void setUsers(List<Integer> users) {
        this.users = users;
    }

    public void addUserId(Integer userID){
        this.users.add(userID);
    }

    public void removeUserId(Integer userID){
        this.users.remove(userID);
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String programmingLanguage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProgrammingLanguage() {
        return programmingLanguage;
    }

    public void setProgrammingLanguage(String programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }
}
