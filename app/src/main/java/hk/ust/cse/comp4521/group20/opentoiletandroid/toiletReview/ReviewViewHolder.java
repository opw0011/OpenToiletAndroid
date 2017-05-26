/*
   #    COMP 4521
   #    CHAN HON SUM    20192524    hschanak@connect.ust.hk
   #    O PUI WAI       20198827    pwo@connect.ust.hk
   #    YU WANG LEUNG   20202032    wlyuaa@connect.ust.hk
 */
package hk.ust.cse.comp4521.group20.opentoiletandroid.toiletReview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import hk.ust.cse.comp4521.group20.opentoiletandroid.toiletDetail.ImageActivity;
import hk.ust.cse.comp4521.group20.opentoiletandroid.R;


/**
 * Created by samch on 2017/5/3.
 */
public class ReviewViewHolder extends RecyclerView.ViewHolder{
    private final TextView mTitleField;
    private final TextView mDescField;
    private final RatingBar mRatingBar;
    private Context context;
    private ImageView imageView;


    /**
     * Instantiates a new Review view holder.
     *
     * @param itemView the item view
     */
    public ReviewViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        mTitleField = (TextView) itemView.findViewById(R.id.reviewTitle);
        mDescField = (TextView) itemView.findViewById(R.id.reviewDescription);
        mRatingBar = (RatingBar) itemView.findViewById(R.id.rating);
        imageView = (ImageView) itemView.findViewById(R.id.ivToiletImage);
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        mTitleField.setText(title);
    }

    /**
     * Sets desc.
     *
     * @param desc the desc
     */
    public void setDesc(String desc) {
        mDescField.setText(desc);
    }

    /**
     * Sets rating.
     *
     * @param rating the rating
     */
    public void setRating(float rating) {mRatingBar.setRating(rating);}

    /**
     * Sets image view.
     *
     * @param ImageURL the image url
     */
    public void setImageView (String ImageURL) {
        Picasso.with(context)
            .load(ImageURL).resize(48, 48).into(imageView);
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ImageActivity.class);
            intent.putExtra("ImageURL", ImageURL);
            context.startActivity(intent);
        });
    }
}
