package hk.ust.cse.comp4521.group20.opentoiletandroid.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by opw on 30/4/2017.
 */

public class Toilet {
    public enum Gender {
        M, F, Both
    }

    private HashMap<String, Boolean> lift;
    private String name;
    private String floor;
    private Gender gender;
    private String image_url;
    private boolean has_accessible_toilet;
    private boolean has_changing_room;
    private boolean has_shower;
    private float total_score;
    private int total_waiting_minute;
    private int count;
    private int pa_pos_x;
    private int pa_pos_y;

    public Toilet(boolean has_changing_room, HashMap<String, Boolean> lift, String name, String floor, Gender gender, String image_url, boolean has_accessible_toilet, boolean has_shower, float total_score, int total_waiting_minute, int count, int pa_pos_x, int pa_pos_y) {
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
        this.pa_pos_x = pa_pos_x;
        this.pa_pos_y = pa_pos_y;
    }

    public Toilet() {
    }

    public List<String> getLiftList() {
        List<String> stringList = new ArrayList<>();
        if (lift != null) stringList.addAll(lift.keySet());
        return stringList;
    }

    public String getName() {
        return name;
    }

    public String getFloor() {
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

    public float getTotal_score() {
        return total_score;
    }

    public int getTotal_waiting_minute() {
        return total_waiting_minute;
    }

    public int getCount() {
        return count;
    }

    public int getPa_pos_x() {
        return pa_pos_x;
    }

    public int getPa_pos_y() {
        return pa_pos_y;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setHas_accessible_toilet(boolean has_accessible_toilet) {
        this.has_accessible_toilet = has_accessible_toilet;
    }

    public void setHas_changing_room(boolean has_changing_room) {
        this.has_changing_room = has_changing_room;
    }

    public void setHas_shower(boolean has_shower) {
        this.has_shower = has_shower;
    }

    public void setTotal_score(float total_score) {
        this.total_score = total_score;
    }

    public void setTotal_waiting_minute(int total_waiting_minute) {
        this.total_waiting_minute = total_waiting_minute;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPa_pos_x(int pa_pos_x) {
        this.pa_pos_x = pa_pos_x;
    }

    public void setPa_pos_y(int pa_pos_y) {
        this.pa_pos_y = pa_pos_y;
    }

    public void setLift(HashMap<String, Boolean> lift) {
        this.lift = lift;
    }

    public HashMap<String, Boolean> getLift() {
        return lift;
    }
}