package com.example.rannaghor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {
    ArrayList<Integer> ImageList=new ArrayList<>();
    public OnboardingAdapter(ArrayList<Integer> ImageList){
        this.ImageList=ImageList;
    }
    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public OnboardingViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.onboarding_view_holder,parent,false);
        return new OnboardingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull OnboardingAdapter.OnboardingViewHolder holder, int position) {
        holder.OnboardingImageView.setImageResource(ImageList.get(position));
    }

    @Override
    public int getItemCount() {
        return ImageList.size();
    }

    public class OnboardingViewHolder extends RecyclerView.ViewHolder {
        ImageView OnboardingImageView;
        public OnboardingViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            OnboardingImageView=(ImageView)itemView.findViewById(R.id.onboarding_viewholder_imgview);
        }
    }
}
