package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by opw on 12/5/2017.
 */

public class SOSViewHolder extends RecyclerView.ViewHolder {
    private Context context;

    public SOSViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
    }
}
