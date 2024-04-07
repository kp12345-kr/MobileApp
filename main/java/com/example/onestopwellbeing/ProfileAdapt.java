package com.example.onestopwellbeing;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProfileAdapt extends RecyclerView.Adapter<ProfileAdapt.ProfileViewHolder> {

    private Context context;
    private List<Profile> profileList;

    public ProfileAdapt(Context context, List<Profile> profileList) {
        this.context = context;
        this.profileList = profileList;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.profile_item, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Profile profile = profileList.get(position);

        holder.textName.setText(profile.getName());
        holder.textType.setText(profile.getType());
        holder.textLocation.setText(profile.getLocation());
        holder.textContact.setText(profile.getContact());
        holder.imageProfile.setImageResource(profile.getImageResId());

        if (profile.isShowExtraInfo()) {
            holder.viewMoreButton.setVisibility(View.VISIBLE);
            holder.qrCodeText.setVisibility(View.VISIBLE);
            holder.textExtraInfo.setVisibility(View.VISIBLE);

            holder.textExtraInfo.setText(profile.getExtraInfo());

            holder.viewMoreButton.setOnClickListener(view -> {
                Intent intent = new Intent(context, HotYogaDetails.class);
                intent.putExtra("profileName", profile.getName());
                intent.putExtra("profileType", profile.getType());
                intent.putExtra("profileLocation", profile.getLocation());
                intent.putExtra("profileContact", profile.getContact());
                intent.putExtra("imageResId", profile.getImageResId());
                context.startActivity(intent);
            });
        } else {
            holder.viewMoreButton.setVisibility(View.GONE);
            holder.qrCodeText.setVisibility(View.GONE);
            holder.textExtraInfo.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textType, textLocation, textContact, qrCodeText, textExtraInfo;
        ImageView imageProfile;
        Button viewMoreButton;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name);
            textType = itemView.findViewById(R.id.text_type);
            textLocation = itemView.findViewById(R.id.text_location);
            textContact = itemView.findViewById(R.id.text_contact);
            imageProfile = itemView.findViewById(R.id.image_profile);
            viewMoreButton = itemView.findViewById(R.id.viewMoreButton);
            qrCodeText = itemView.findViewById(R.id.qrCodeText);
            textExtraInfo = itemView.findViewById(R.id.text_extra_info);
        }
    }
}