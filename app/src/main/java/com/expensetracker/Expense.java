package com.expensetracker;

public class Expense {
    private int id;
    private String category;
    private String description;
    private float amount;

    public Expense(int id, String category, String description, double amount) {
        this.id = id;
        this.category = category;
        this.description = description;
        this.amount = (float) amount; // Cast double to float
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = (float) amount; // Cast double to float
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                '}';
    }
}
