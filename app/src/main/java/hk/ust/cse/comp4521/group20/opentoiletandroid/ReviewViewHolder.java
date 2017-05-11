package hk.ust.cse.comp4521.group20.opentoiletandroid;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by samch on 2017/5/3.
 */
public class ReviewViewHolder extends RecyclerView.ViewHolder{
    private final TextView mTitleField;
    private final TextView mDescField;
    private final RatingBar mRatingBar;
    private Context context;
    private ImageView imageView;

    public ReviewViewHolder(View itemView) {
        super(itemView);
        context = itemView.getContext();
        mTitleField = (TextView) itemView.findViewById(R.id.reviewTitle);
        mDescField = (TextView) itemView.findViewById(R.id.reviewDescription);
        mRatingBar = (RatingBar) itemView.findViewById(R.id.rating);
        imageView = (ImageView) itemView.findViewById(R.id.ivToiletImage);
    }

    public void setTitle(String title) {
        mTitleField.setText(title);
    }

    public void setDesc(String desc) {
        mDescField.setText(desc);
    }

    public void setRating(float rating) {mRatingBar.setRating(rating);}

    public void setImageView (String ImageURL) {
        Picasso.with(context)
            .load(ImageURL)
            .resize(200, 0).into(imageView);
    }
}
