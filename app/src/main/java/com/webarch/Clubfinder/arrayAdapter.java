package com.webarch.Clubfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class arrayAdapter extends ArrayAdapter<cards> {
    Context context;

    public arrayAdapter(Context context, int resourceId, List<cards> items){
        super(context,resourceId,items);
    }
    public View getView(int position, View convertView , ViewGroup parent) {
        cards card_item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        TextView name = (TextView)convertView.findViewById(R.id.name);
        TextView linkedin = (TextView)convertView.findViewById(R.id.Linkedin);
        TextView description = (TextView)convertView.findViewById(R.id.Description);
        TextView skills = (TextView)convertView.findViewById(R.id.Skills);
        TextView names_label = (TextView)convertView.findViewById(R.id.Name_Title);

        TextView linkedin_link= (TextView)convertView.findViewById(R.id.linkedin_link);
        TextView describe_label= (TextView)convertView.findViewById(R.id.describe);
        TextView skills_label = (TextView)convertView.findViewById(R.id.skills_label);

        ImageView image = (ImageView)convertView.findViewById(R.id.image);
        assert card_item != null;
        name.setText(card_item.getName());

        description.setText(card_item.getDescription());
        linkedin.setText(card_item.getLinkedin());
        skills.setText(card_item.getSkills());
        names_label.setText("Name:");
        describe_label.setText("Description:");
        linkedin_link.setText("Linkedin: Profile");
        skills_label.setText("Skills:");
        image.setImageResource(R.mipmap.ic_launcher_round);
         switch (card_item.getProfileImageUrl()) {
             case "default" :
               Glide.with(convertView.getContext()).load(R.mipmap.ic_launcher).into(image);
                break;

            default:
                Glide.clear(image);
                Glide.with(convertView.getContext()).load(card_item.getProfileImageUrl()).into(image);
                break;

        }


        return convertView;

    }
}