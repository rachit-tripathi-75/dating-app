package com.deificdigital.chaska.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.deificdigital.chaska.R;
import com.deificdigital.chaska.app.App;
import com.deificdigital.chaska.model.Profile;

import java.util.List;


public class ProfilesSpotlightListAdapter extends RecyclerView.Adapter<ProfilesSpotlightListAdapter.MyViewHolder> {

    private List<Profile> items;
    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {

        void onItemClick(View view, Profile obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {

        this.mOnItemClickListener = mItemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView mFullname;
        public ImageView thumbnail;
        public ProgressBar mProgressBar;
        public MaterialRippleLayout mParent;

        public MyViewHolder(View view) {

            super(view);

            mParent = view.findViewById(R.id.parent);
            thumbnail = view.findViewById(R.id.thumbnail);
            mFullname = view.findViewById(R.id.fullname);
            mProgressBar = view.findViewById(R.id.progressBar);
        }
    }


    public ProfilesSpotlightListAdapter(Context context, List<Profile> items) {

        mContext = context;
        this.items = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_spotlight_thumbnail, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final Profile item = items.get(position);

        holder.mFullname.setVisibility(View.GONE);
        holder.thumbnail.setVisibility(View.VISIBLE);
        holder.mProgressBar.setVisibility(View.VISIBLE);

        holder.mFullname.setText(item.getFullname());

        if (item.getLowPhotoUrl() != null && item.getLowPhotoUrl().length() > 0 &&
                (App.getInstance().getSettings().isAllowShowNotModeratedProfilePhotos() || App.getInstance().getId() == item.getId() || item.getPhotoModerateAt() != 0)) {

            final ProgressBar progressBar = holder.mProgressBar;
            final ImageView imageView = holder.thumbnail;
            final TextView fullname = holder.mFullname;

            Glide.with(mContext)
                    .load(item.getLowPhotoUrl())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                            fullname.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            imageView.setImageResource(R.drawable.profile_default_photo);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                            progressBar.setVisibility(View.GONE);
                            fullname.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(holder.thumbnail);

        } else {

            holder.mProgressBar.setVisibility(View.GONE);
            holder.thumbnail.setImageResource(R.drawable.profile_default_photo);
            holder.mFullname.setVisibility(View.VISIBLE);
        }

        holder.mParent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (mOnItemClickListener != null) {

                    mOnItemClickListener.onItemClick(view, item, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {

        return items.size();
    }
}