package com.example.meet.view.addingcontacts;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.backend.bean.User;
import com.example.framework.backend.marker.Info;
import com.example.framework.glide.GlideUtil;
import com.example.meet.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddingContactsRecyclerViewHolder extends RecyclerView.ViewHolder{

    public AddingContactsRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    void set(Info info, String contactName){

        // portrait
        CircleImageView portrait = itemView.findViewById(R.id.iv_photo);
        String url = ((User) info).getPhoto();
        GlideUtil.load(itemView.getContext(), url, portrait);

        // nickname
        TextView nickName = itemView.findViewById(R.id.tv_nickname);
        nickName.setText(((User) info).getNickName());

        // gender
        ImageView gender = itemView.findViewById(R.id.iv_sex);
        gender.setImageResource(((User) info).isSex() ? R.drawable.img_boy_icon : R.drawable.img_girl_icon);

        // age
        TextView age = itemView.findViewById(R.id.tv_age);
        age.setText(((User) info).getAge() + " " + itemView.getContext().getResources().getString(R.string.text_search_age));

        //description
        TextView description = itemView.findViewById(R.id.tv_desc);
        description.setText(((User) info).getDesc());

        // contact info
        LinearLayout contactLayout = itemView.findViewById(R.id.ll_contact_info);
        contactLayout.setVisibility(View.VISIBLE);
        TextView contactNameView = itemView.findViewById(R.id.tv_contact_name);
        contactNameView.setText(contactName);
        TextView contactPhoneNumberView = itemView.findViewById(R.id.tv_contact_phone);
        contactPhoneNumberView.setText(((User) info).getMobilePhoneNumber());

    }

}
