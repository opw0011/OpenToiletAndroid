package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hk.ust.cse.comp4521.group20.opentoiletandroid.models.Toilet;


/**
 * Created by samch on 2017/5/2.
 */
public class ToiletViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView mNameField;
    private final TextView mTextField;
    private Context context;
    private Toilet toilet;
    private String toiletId;

    /**
     * Instantiates a new Toilet view holder.
     *
     * @param itemView the item view
     */
    public ToiletViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        context = itemView.getContext();
        mNameField = (TextView) itemView.findViewById(R.id.tvName);
        mTextField = (TextView) itemView.findViewById(R.id.tvDescription);
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        mNameField.setText(name);
    }

    /**
     * Sets text.
     *
     * @param text the text
     */
    public void setText(String text) {
        mTextField.setText(text);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, ToiletDetailActivity.class);
        // TODO: put the selected toilet models in the intent
//        intent.putExtra("ToiletName", mNameField.getText().toString());
//        intent.putExtra("ToiletTotalScore", toilet.getTotal_score());
//        intent.putExtra("ToiletReviewCount", toilet.getCount());
//        intent.putExtra("ToiletFloor", toilet.getFloor());
//        intent.putExtra("ToiletLift", toilet.getLiftList().toString());
        intent.putExtra("ToiletId", toiletId);
        context.startActivity(intent);
    }

    /**
     * Sets toilet.
     *
     * @param toilet the toilet
     */
    public void setToilet(Toilet toilet) {
        this.toilet = toilet;
    }

    /**
     * Sets toilet id.
     *
     * @param toiletId the toilet id
     */
    public void setToiletId(String toiletId) {
        this.toiletId = toiletId;
    }

    /**
     * Hide.
     */
    public void hide() {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)itemView.getLayoutParams();
        params.height = 0;
        itemView.setLayoutParams(params);
    }

    /**
     * Show.
     */
    public void show() {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)itemView.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        itemView.setLayoutParams(params);
    }

    /**
     * Gets toilet.
     *
     * @return the toilet
     */
    public Toilet getToilet() {
        return toilet;
    }
}
