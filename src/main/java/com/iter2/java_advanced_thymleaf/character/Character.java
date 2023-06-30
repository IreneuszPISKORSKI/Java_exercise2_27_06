package com.iter2.java_advanced_thymleaf.character;

public class Character {

    private int id;
    private String name;
    private String type;
    private int hp;

    public Character(){

    }

    public Character(int id, String name, String type, int hp){
        this.id = id;
        this.name = name;
        this.type = type;
        this.hp = hp;
    }
    public Character(String name, String type, int hp){
        this.name = name;
        this.type = type;
        this.hp = hp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
