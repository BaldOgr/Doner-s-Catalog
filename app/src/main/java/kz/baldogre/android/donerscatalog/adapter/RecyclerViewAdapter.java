package kz.baldogre.android.donerscatalog.adapter;

import android.animation.Animator;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.List;

import kz.baldogre.android.donerscatalog.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Uri> images;
    private int lastPosition = 0;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public RecyclerViewAdapter(List<Uri> mImages) {
        images = mImages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_photo_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (position < images.size()) {
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            holder.imageView.setImageURI(images.get(position));
            holder.imageView.setClickable(false);
            holder.deleteImage.setVisibility(View.VISIBLE);
            holder.deleteImage.setClickable(true);
            holder.deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.cv.setAnimation(AnimationUtils.loadAnimation(holder.cv.getContext(), R.anim.delete_photo_anim));
                    holder.cv.animate().setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            images.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    }).start();
                }
            });
        } else {
            if (lastPosition < position) {
                lastPosition = holder.getAdapterPosition() + 1;
                holder.cv.setAnimation(AnimationUtils.loadAnimation(holder.cv.getContext(), R.anim.add_photo_anim));
                holder.cv.animate().start();
            }
            holder.imageView.setImageResource(R.mipmap.ic_launcher);
            holder.imageView.setClickable(true);
            holder.deleteImage.setVisibility(View.INVISIBLE);
            holder.deleteImage.setClickable(false);
        }
    }

    @Override
    public int getItemCount() {
        return images.size() + 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView imageView;
        ImageView deleteImage;

        ViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.recycle_view_item_card_view);
            imageView = itemView.findViewById(R.id.recycle_view_item_image_view);
            deleteImage = itemView.findViewById(R.id.recycle_view_delete_image);
        }
    }
}
