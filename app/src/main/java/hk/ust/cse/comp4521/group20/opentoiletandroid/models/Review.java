package hk.ust.cse.comp4521.group20.opentoiletandroid.models;

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

    /**
     * Instantiates a new Review.
     *
     * @param user_id        the user id
     * @param title          the title
     * @param content        the content
     * @param created_at     the created at
     * @param score          the score
     * @param waiting_minute the waiting minute
     * @param image_url      the image url
     */
    public Review(String user_id, String title, String content, String created_at, float score, int waiting_minute, String image_url) {
        this.user_id = user_id;
        this.title = title;
        this.content = content;
        this.created_at = created_at;
        this.score = score;
        this.waiting_minute = waiting_minute;
        this.image_url = image_url;
    }

    /**
     * Instantiates a new Review.
     */
    public Review(){}

    /**
     * Gets score.
     *
     * @return the score
     */
    public float getScore() {
        return score;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public String getUser_id() {
        return user_id;
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
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
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
     * Gets waiting minute.
     *
     * @return the waiting minute
     */
    public int getWaiting_minute() {
        return waiting_minute;
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
     * Sets image url.
     *
     * @param image_url the image url
     */
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
