package com.example.meet.view.addingfriend;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.framework.backend.bean.User;
import com.example.framework.backend.marker.Info;
import com.example.framework.glide.GlideUtil;
import com.example.meet.R;
import com.example.meet.activity.NewFriendActivity;
import com.example.meet.activity.UserInfoActivity;

import de.hdodenhof.circleimageview.CircleImageView;

class AddingFriendRecyclerViewHolder extends RecyclerView.ViewHolder {

    AddingFriendRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    void set(final Info info){
        if(info instanceof AddingFriendTitleInfo){
            TextView textView = itemView.findViewById(R.id.tv_title);
            textView.setText(((AddingFriendTitleInfo) info).getTitle());
        }
        else if(info instanceof User){

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

            // click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), UserInfoActivity.class);
                    intent.putExtra(UserInfoActivity.INTENT_EXTRA_FRIEND_USER_ID, ((User) info).getUid());
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }


}
