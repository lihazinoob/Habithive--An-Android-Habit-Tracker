package com.example.habithive.activities.model;

public class UserManagerSingleton {
    private static UserManagerSingleton instance;
    private User currentUser;
    // Private constructor to prevent instantiation
    private UserManagerSingleton() {
        // Prevent instantiation from outside
    }
    public static synchronized UserManagerSingleton getInstance()
    {
        if(instance == null)
        {
            instance = new UserManagerSingleton();

        }
        return instance;
    }
    // Method to set the current user (e.g., after login or fetching from Room)
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    // Method to get the current user
    public User getCurrentUser() {
        return this.currentUser;
    }

    // Method to clear the current user (e.g., on logout)
    public void clearCurrentUser() {
        this.currentUser = null;
    }

}
