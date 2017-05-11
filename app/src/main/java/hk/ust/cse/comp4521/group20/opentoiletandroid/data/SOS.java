package hk.ust.cse.comp4521.group20.opentoiletandroid.data;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by opw on 11/5/2017.
 */

public class SOS {
    private String userName;
    private String toiletID;
    private Date createdAt;

    public SOS(String userName, String toiletID, Date createdAt) {
        this.userName = userName;
        this.toiletID = toiletID;
        this.createdAt = createdAt;
    }

    public String getUserName() {
        return userName;
    }

    public String getToiletID() {
        return toiletID;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public static ArrayList<SOS> getTestingList() {
        ArrayList<SOS> sosList = new ArrayList<>();
        sosList.add(new SOS("User 1", "1", new Date()));
        sosList.add(new SOS("User 2", "2", new Date()));
        return sosList;
    }
}
