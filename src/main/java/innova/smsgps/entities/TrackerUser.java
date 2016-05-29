package innova.smsgps.entities;

import java.util.ArrayList;

/**
 * Created by USUARIO on 20/05/2016.
 */
public class TrackerUser
{
    private int status  = 0;
    private String description = "";
    private Tracker tracker = new Tracker();

    ArrayList<Friend> alFriend      = new ArrayList<Friend>();
    ArrayList<Tracker> alTracker    = new ArrayList<Tracker>();

    /**
     * @param status int
     * @param description String
     * @param tracker Tracker
     */
    public TrackerUser(int status, String description, Tracker tracker) {
        this.status = status;
        this.description = description;
        this.tracker = tracker;
    }

    /**
     * @param status int
     * @param description String
     */
    public TrackerUser(int status, String description)
    {
        this.status = status;
        this.description = description;
    }

    public boolean isCorrecto()
    {
        if (this.status == 1 || this.status == 2)
            return true;

        return false;
    }
    public String getDescription() {
        return description;
    }
    public Tracker getTracker() {
        return tracker;
    }




    public ArrayList<Friend> getAlFriend() {
        return alFriend;
    }

    public void setAlFriend(ArrayList<Friend> alFriend) {
        this.alFriend = alFriend;
    }

    public ArrayList<Tracker> getAlTracker() {
        return alTracker;
    }

    public void setAlTracker(ArrayList<Tracker> alTracker) {
        this.alTracker = alTracker;
    }

}
