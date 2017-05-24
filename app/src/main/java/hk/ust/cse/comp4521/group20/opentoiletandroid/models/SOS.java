/*
   #    COMP 4521
   #    CHAN HON SUM    20192524    hschanak@connect.ust.hk
   #    O PUI WAI       20198827    pwo@connect.ust.hk
   #    YU WANG LEUNG   20202032    wlyuaa@connect.ust.hk
 */
package hk.ust.cse.comp4521.group20.opentoiletandroid.models;

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

    /**
     * Instantiates a new Sos.
     */
    public SOS() {}

    /**
     * Instantiates a new Sos.
     *
     * @param toilet_id  the toilet id
     * @param author     the author
     * @param message    the message
     * @param created_at the created at
     * @param title      the title
     */
    public SOS(String toilet_id, String author, String message, String created_at, String title) {
        this.toilet_id = toilet_id;
        this.author = author;
        this.message = message;
        this.created_at = created_at;
        this.title = title;
        this.is_active = true;
    }

    /**
     * Instantiates a new Sos.
     *
     * @param toilet_id  the toilet id
     * @param author     the author
     * @param message    the message
     * @param created_at the created at
     * @param title      the title
     * @param active     the active
     */
    public SOS(String toilet_id, String author, String message, String created_at, String title, boolean active) {
        this.toilet_id = toilet_id;
        this.author = author;
        this.message = message;
        this.created_at = created_at;
        this.title = title;
        this.is_active = active;
    }

    /**
     * Gets toilet id.
     *
     * @return the toilet id
     */
    public String getToilet_id() {
        return toilet_id;
    }

    /**
     * Sets toilet id.
     *
     * @param toilet_id the toilet id
     */
    public void setToilet_id(String toilet_id) {
        this.toilet_id = toilet_id;
    }

    /**
     * Gets author.
     *
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets author.
     *
     * @param author the author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets created at.
     *
     * @return the created at
     */
    public String getCreated_at() {
        return created_at;
    }

    /**
     * Obtain created at date date.
     *
     * @return the date
     * @throws ParseException the parse exception
     */
    public Date obtainCreatedAtDate()throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        return dateFormat.parse(created_at);
    }

    /**
     * Sets created at.
     *
     * @param created_at the created at
     */
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets is active.
     *
     * @return the is active
     */
    public boolean getIs_active() {
        return is_active;
    }

    /**
     * Sets is active.
     *
     * @param is_active the is active
     */
    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }
}
