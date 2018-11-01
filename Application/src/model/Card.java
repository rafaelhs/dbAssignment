package model;

public class Card {
    private int id;
    private String name;
    private String cost;
    private String description;
    private String type;
    private char rarity;


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

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public char getRarity() {
        return rarity;
    }

    public void setRarity(char rarity) {
        this.rarity = rarity;
    }
}
