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
    private boolean has_accessible_toilet;
    private boolean has_changing_room;
    private boolean has_shower;
    private int total_score;
    private int total_waiting_minute;
    private int count;

    public Toilet(boolean has_changing_room, List<Integer> lift, String name, int floor, Gender gender, String image_url, boolean has_accessible_toilet, boolean has_shower, int total_score, int total_waiting_minute, int count) {
        this.has_changing_room = has_changing_room;
        this.lift = lift;
        this.name = name;
        this.floor = floor;
        this.gender = gender;
        this.image_url = image_url;
        this.has_accessible_toilet = has_accessible_toilet;
        this.has_shower = has_shower;
        this.total_score = total_score;
        this.total_waiting_minute = total_waiting_minute;
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

    public boolean isHas_accessible_toilet() {
        return has_accessible_toilet;
    }

    public boolean isHas_changing_room() {
        return has_changing_room;
    }

    public boolean isHas_shower() {
        return has_shower;
    }

    public int getTotal_score() {
        return total_score;
    }

    public int getTotal_waiting_minute() {
        return total_waiting_minute;
    }

    public int getCount() {
        return count;
    }
}