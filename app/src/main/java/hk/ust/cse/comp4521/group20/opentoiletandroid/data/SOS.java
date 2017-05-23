package hk.ust.cse.comp4521.group20.opentoiletandroid.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by opw on 12/5/2017.
 */

public class SOS {
    private String toilet_id;
    private String author;
    private String message;
    private String created_at;
    private String title;
    private boolean is_active;

    public SOS() {}

    public SOS(String toilet_id, String author, String message, String created_at, String title) {
        this.toilet_id = toilet_id;
        this.message = message;
        this.created_at = created_at;
        this.title = title;
        this.is_active = true;
    }

    public SOS(String toilet_id, String author, String message, String created_at, String title, boolean active) {
        this.toilet_id = toilet_id;
        this.message = message;
        this.created_at = created_at;
        this.title = title;
        this.is_active = active;
    }

    public String getToilet_id() {
        return toilet_id;
    }

    public void setToilet_id(String toilet_id) {
        this.toilet_id = toilet_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public Date obtainCreatedAtDate()throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        return dateFormat.parse(created_at);
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

    public boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }
}
