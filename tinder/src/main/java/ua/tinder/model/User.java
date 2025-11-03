package ua.tinder.model;

public class User {
    private int id;
    private String email;
    private String name;
    private String photoUrl;

    public User(int id, String email, String name, String photoUrl) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getPhotoUrl() { return photoUrl; }
}