/*
   #    COMP 4521
   #    CHAN HON SUM    20192524    hschanak@connect.ust.hk
   #    O PUI WAI       20198827    pwo@connect.ust.hk
   #    YU WANG LEUNG   20202032    wlyuaa@connect.ust.hk
 */
package hk.ust.cse.comp4521.group20.opentoiletandroid.models;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by opw on 30/4/2017.
 */
public class Toilet {
    /**
     * The Gender enumeration.
     */
    public enum Gender {
        /**
         * Male.
         */
        M, /**
         * Female.
         */
        F, /**
         * Unisex.
         */
        Both
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

    /**
     * Instantiates a new Toilet.
     *
     * @param has_changing_room     the has changing room
     * @param lift                  the lift
     * @param name                  the name
     * @param floor                 the floor
     * @param gender                the gender
     * @param image_url             the image url
     * @param has_accessible_toilet the has accessible toilet
     * @param has_shower            the has shower
     * @param total_score           the total score
     * @param total_waiting_minute  the total waiting minute
     * @param count                 the count
     * @param pa_pos_x              the pa pos x
     * @param pa_pos_y              the pa pos y
     */
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

    /**
     * Instantiates a new Toilet.
     */
    public Toilet() {
    }

    /**
     * Gets lift list.
     *
     * @return the lift list
     */
    @Exclude
    public List<String> getLiftList() {
        List<String> stringList = new ArrayList<>();
        if (lift != null) stringList.addAll(lift.keySet());
        return stringList;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets floor.
     *
     * @return the floor
     */
    public String getFloor() {
        return floor;
    }

    /**
     * Gets gender.
     *
     * @return the gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Gets image url.
     *
     * @return the image url
     */
    public String getImage_url() {
        return image_url;
    }

    /**
     * Is has accessible toilet boolean.
     *
     * @return the boolean
     */
    public boolean isHas_accessible_toilet() {
        return has_accessible_toilet;
    }

    /**
     * Is has changing room boolean.
     *
     * @return the boolean
     */
    public boolean isHas_changing_room() {
        return has_changing_room;
    }

    /**
     * Is has shower boolean.
     *
     * @return the boolean
     */
    public boolean isHas_shower() {
        return has_shower;
    }

    /**
     * Gets total score.
     *
     * @return the total score
     */
    public float getTotal_score() {
        return total_score;
    }

    /**
     * Gets total waiting minute.
     *
     * @return the total waiting minute
     */
    public int getTotal_waiting_minute() {
        return total_waiting_minute;
    }

    /**
     * Gets count.
     *
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * Gets pa pos x.
     *
     * @return the pa pos x
     */
    public int getPa_pos_x() {
        return pa_pos_x;
    }

    /**
     * Gets pa pos y.
     *
     * @return the pa pos y
     */
    public int getPa_pos_y() {
        return pa_pos_y;
    }


    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets floor.
     *
     * @param floor the floor
     */
    public void setFloor(String floor) {
        this.floor = floor;
    }

    /**
     * Sets gender.
     *
     * @param gender the gender
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * Sets image url.
     *
     * @param image_url the image url
     */
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    /**
     * Sets has accessible toilet.
     *
     * @param has_accessible_toilet the has accessible toilet
     */
    public void setHas_accessible_toilet(boolean has_accessible_toilet) {
        this.has_accessible_toilet = has_accessible_toilet;
    }

    /**
     * Sets has changing room.
     *
     * @param has_changing_room the has changing room
     */
    public void setHas_changing_room(boolean has_changing_room) {
        this.has_changing_room = has_changing_room;
    }

    /**
     * Sets has shower.
     *
     * @param has_shower the has shower
     */
    public void setHas_shower(boolean has_shower) {
        this.has_shower = has_shower;
    }

    /**
     * Sets total score.
     *
     * @param total_score the total score
     */
    public void setTotal_score(float total_score) {
        this.total_score = total_score;
    }

    /**
     * Sets total waiting minute.
     *
     * @param total_waiting_minute the total waiting minute
     */
    public void setTotal_waiting_minute(int total_waiting_minute) {
        this.total_waiting_minute = total_waiting_minute;
    }

    /**
     * Sets count.
     *
     * @param count the count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Sets pa pos x.
     *
     * @param pa_pos_x the pa pos x
     */
    public void setPa_pos_x(int pa_pos_x) {
        this.pa_pos_x = pa_pos_x;
    }

    /**
     * Sets pa pos y.
     *
     * @param pa_pos_y the pa pos y
     */
    public void setPa_pos_y(int pa_pos_y) {
        this.pa_pos_y = pa_pos_y;
    }

    /**
     * Sets lift.
     *
     * @param lift the lift
     */
    public void setLift(HashMap<String, Boolean> lift) {
        this.lift = lift;
    }

    /**
     * Gets lift.
     *
     * @return the lift
     */
    public HashMap<String, Boolean> getLift() {
        return lift;
    }

    /**
     * Obtain average score double.
     *
     * @return the double
     */
    public double obtainAverageScore() {
        return count != 0? ((double) total_score) / count : 0;
    }

    /**
     * Obtain average waiting time double.
     *
     * @return the double
     */
    public double obtainAverageWaitingTime() {
        return count != 0? ((double) total_waiting_minute) / count : 0;
    }
}