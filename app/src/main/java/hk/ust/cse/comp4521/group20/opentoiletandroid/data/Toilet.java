package hk.ust.cse.comp4521.group20.opentoiletandroid.data;

/**
 * Created by opw on 30/4/2017.
 */

public class Toilet {
    public enum Gender {
        Male, Female
    }

    private String name;
    private int lift;
    private Gender gender;
    private double averageRating;
    private int like;
    private int dislike;
    private boolean hasChangingRoom;
    private boolean hasShower;
    private boolean hasAccessibility;

    public Toilet(String name, int lift, Gender gender, double averageRating, int like, int dislike, boolean hasChangingRoom, boolean hasShower, boolean hasAccessibility) {
        this.name = name;
        this.lift = lift;
        this.gender = gender;
        this.averageRating = averageRating;
        this.like = like;
        this.dislike = dislike;
        this.hasChangingRoom = hasChangingRoom;
        this.hasShower = hasShower;
        this.hasAccessibility = hasAccessibility;
    }

    public String getName() {
        return name;
    }

    public int getLift() {
        return lift;
    }

    public Gender getGender() {
        return gender;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public int getLike() {
        return like;
    }

    public int getDislike() {
        return dislike;
    }

    public boolean isHasChangingRoom() {
        return hasChangingRoom;
    }

    public boolean isHasShower() {
        return hasShower;
    }

    public boolean isHasAccessibility() {
        return hasAccessibility;
    }
}