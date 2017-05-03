package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import hk.ust.cse.comp4521.group20.opentoiletandroid.data.Toilet;


/**
 * Created by samch on 2017/5/2.
 */
public class ToiletViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView mNameField;
    private final TextView mTextField;
    private Context context;
    private Toilet toilet;
    private String toiletId;

    public ToiletViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        context = itemView.getContext();
        mNameField = (TextView) itemView.findViewById(R.id.tvName);
        mTextField = (TextView) itemView.findViewById(R.id.tvDescription);
    }

    public void setName(String name) {
        mNameField.setText(name);
    }

    public void setText(String text) {
        mTextField.setText(text);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, ToiletDetailActivity.class);
        // TODO: put the selected toilet data in the intent
        intent.putExtra("ToiletName", mNameField.getText().toString());
        intent.putExtra("ToiletTotalScore", toilet.getTotal_score());
        intent.putExtra("ToiletReviewCount", toilet.getCount());
        intent.putExtra("ToiletFloor", toilet.getFloor());
        intent.putExtra("ToiletLift", toilet.getLift().toString());
        intent.putExtra("ToiletId", toiletId);
        context.startActivity(intent);
    }

    public void setToilet(Toilet toilet) {
        this.toilet = toilet;
    }

    public void setToiletId(String toiletId) {
        this.toiletId = toiletId;
    }
}
