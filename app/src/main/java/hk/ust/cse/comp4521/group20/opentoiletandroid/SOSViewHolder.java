package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by opw on 12/5/2017.
 */

public class SOSViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    private TextView textViewTitle;
    private TextView textViewMessage;
    private TextView textViewToiletName;

    public SOSViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        textViewTitle = (TextView) itemView.findViewById(R.id.tv_sos_title);
        textViewMessage = (TextView) itemView.findViewById(R.id.tv_sos_message);
        textViewToiletName = (TextView) itemView.findViewById(R.id.tv_sos_toilet_name);
    }

    public void setTextViewTitle(String title) {
        textViewTitle.setText(title);
    }

    public void setTextViewMessage(String message) {
        textViewMessage.setText(message);
    }

    public void setTextViewToiletName(String name) {
        textViewToiletName.setText(name);
    }
}
