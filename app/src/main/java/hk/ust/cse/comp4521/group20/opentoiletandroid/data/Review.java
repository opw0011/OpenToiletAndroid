package hk.ust.cse.comp4521.group20.opentoiletandroid.data;

/**
 * Created by samch on 2017/5/3.
 */

public class Review {
    private String user_id;
    private String title;
    private String content;
    private String created_at;
    private float score;
    private int waiting_minute;
    private String image_url;

    public Review(String user_id, String title, String content, String created_at, float score, int waiting_minute, String image_url) {
        this.user_id = user_id;
        this.title = title;
        this.content = content;
        this.created_at = created_at;
        this.score = score;
        this.waiting_minute = waiting_minute;
        this.image_url = image_url;
    }

    public Review(){}

    public float getScore() {
        return score;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public int getWaiting_minute() {
        return waiting_minute;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
