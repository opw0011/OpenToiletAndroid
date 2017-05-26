/*
   #    COMP 4521
   #    CHAN HON SUM    20192524    hschanak@connect.ust.hk
   #    O PUI WAI       20198827    pwo@connect.ust.hk
   #    YU WANG LEUNG   20202032    wlyuaa@connect.ust.hk
 */
package hk.ust.cse.comp4521.group20.opentoiletandroid.sos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hk.ust.cse.comp4521.group20.opentoiletandroid.R;

/**
 * Created by opw on 12/5/2017.
 */
public class SOSViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    private TextView textViewTitle;
    private TextView textViewMessage;
    private TextView textViewToiletName;
    private TextView textViewTimestamp;
    private Button buttonSOSResolve;
    private String sosId;

    /**
     * Instantiates a new Sos view holder.
     *
     * @param itemView the item view
     */
    public SOSViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        textViewTitle = (TextView) itemView.findViewById(R.id.tv_sos_title);
        textViewMessage = (TextView) itemView.findViewById(R.id.tv_sos_message);
        textViewToiletName = (TextView) itemView.findViewById(R.id.tv_sos_toilet_name);
        textViewTimestamp = (TextView) itemView.findViewById(R.id.tv_sos_timestamp);
        buttonSOSResolve = (Button) itemView.findViewById(R.id.btn_sos_resolve);
    }

    /**
     * Set sos id.
     *
     * @param sosId the sos id
     */
    public void setSosId(String sosId){
        this.sosId = sosId;
        Log.d("", sosId);
    }

    /**
     * Sets text view title.
     *
     * @param title the title
     */
    public void setTextViewTitle(String title) {
        textViewTitle.setText(title);
    }

    /**
     * Sets text view message.
     *
     * @param message the message
     */
    public void setTextViewMessage(String message) {
            textViewMessage.setText(message);
    }

    /**
     * Sets text view toilet name.
     *
     * @param name the name
     */
    public void setTextViewToiletName(String name) {
        textViewToiletName.setText(name);
    }

    /**
     * Sets text view timestamp.
     *
     * @param timestamp the timestamp
     */
    public void setTextViewTimestamp(String timestamp) {
        DateFormat dfOriginal = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        //DateFormat dfNew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String iconPrefix = "{gmd_access_time}";
        try {
            Date date = dfOriginal.parse(timestamp);
            PrettyTime p = new PrettyTime();
            textViewTimestamp.setText(iconPrefix + " " + p.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            textViewTimestamp.setText("");
        }

    }

    /**
     * Sets button sos resolve.
     *
     * @param userId the user id
     */
    public void setButtonSOSResolve(String userId) {
        FirebaseAuth.getInstance().addAuthStateListener(auth -> {
            if(auth.getCurrentUser() == null || !auth.getCurrentUser().getUid().equals(userId)) {
                buttonSOSResolve.setVisibility(View.GONE);
            } else {
                buttonSOSResolve.setVisibility(View.VISIBLE);
                buttonSOSResolve.setOnClickListener(v -> {
                    // Set to inactive
                    Log.d("SOSViewHolder", sosId);
                    FirebaseDatabase.getInstance().getReference("sos_items").child(sosId).child("is_active").setValue(false);
                });
            }
        });
    }
}
