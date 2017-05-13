package hk.ust.cse.comp4521.group20.opentoiletandroid.data;

import java.util.Date;

/**
 * Created by opw on 12/5/2017.
 */

public class SOS {
    private String toilet_id;
    private String message;
    private String created_at;
    private String title;

    public SOS() {}

    public SOS(String toilet_id, String message, String created_at, String title) {
        this.toilet_id = toilet_id;
        this.message = message;
        this.created_at = created_at;
        this.title = title;
    }

    public String getToilet_id() {
        return toilet_id;
    }

    public void setToilet_id(String toilet_id) {
        this.toilet_id = toilet_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
