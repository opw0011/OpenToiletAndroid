package hk.ust.cse.comp4521.group20.opentoiletandroid.data;

import java.util.List;

/**
 * Created by opw on 30/4/2017.
 */

public class Toilet {
    public enum Gender {
        M, F, Both
    }
    private List<Integer> lift;
    private String name;
    private int floor;
    private Gender gender;
    private String image_url;
    private boolean hasAccessibleToilet;
    private boolean hasChangingRoom;
    private boolean hasShower;
    private int totalScore;
    private int totalWaitingMinute;
    private int count;

    public Toilet(boolean has_changing_room, List<Integer> lift, String name, int floor, Gender gender, String image_url, boolean has_accessible_toilet, boolean has_shower, int total_score, int total_waiting_minute, int count) {
        this.hasChangingRoom = has_changing_room;
        this.lift = lift;
        this.name = name;
        this.floor = floor;
        this.gender = gender;
        this.image_url = image_url;
        this.hasAccessibleToilet = has_accessible_toilet;
        this.hasShower = has_shower;
        this.totalScore = total_score;
        this.totalWaitingMinute = total_waiting_minute;
        this.count = count;
    }

    public Toilet(){}


    public List<Integer> getLift() {
        return lift;
    }

    public String getName() {
        return name;
    }

    public int getFloor() {
        return floor;
    }

    public Gender getGender() {
        return gender;
    }

    public String getImage_url() {
        return image_url;
    }

    public boolean isHasAccessibleToilet() {
        return hasAccessibleToilet;
    }

    public boolean isHasChangingRoom() {
        return hasChangingRoom;
    }

    public boolean isHasShower() {
        return hasShower;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getTotalWaitingMinute() {
        return totalWaitingMinute;
    }

    public int getCount() {
        return count;
    }
}