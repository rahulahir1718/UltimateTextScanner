package com.rudraambition.ultimatetextscanner;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AboutUsAdapter extends RecyclerView.Adapter<AboutUsAdapter.AboutUsHolder> {


    Context context;
    private List<Names> list;

    public AboutUsAdapter(){}
    public AboutUsAdapter(Context context,List<Names> list)
    {
        this.context=context;
        this.list=list;
    }
    @NonNull
    @Override
    public AboutUsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View v=inflater.inflate(R.layout.about_us_card,null,false);
        return new AboutUsHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull AboutUsHolder holder, int position) {
        Names names=list.get(position);
        holder.Name.setText(names.getName());
        holder.Skill1.setText(names.getSkill1());
        holder.Skill2.setText(names.getSkill2());
        holder.imageView.setImageResource(names.getImage());
        switch(names.getRole())
        {
            case "1":
                switch(position)
                {
                    case 0:holder.cardLinearLayout.setBackground(context.getResources().getDrawable(R.drawable.g1,null));
                        break;
                    case 1:holder.cardLinearLayout.setBackground(context.getResources().getDrawable(R.drawable.g2,null));
                        break;
                    case 2:holder.cardLinearLayout.setBackground(context.getResources().getDrawable(R.drawable.g3,null));
                        break;
                }
                break;
            case "2":
                switch(position)
                {
                    case 0:holder.cardLinearLayout.setBackground(context.getResources().getDrawable(R.drawable.g4,null));
                        break;
                    case 1:holder.cardLinearLayout.setBackground(context.getResources().getDrawable(R.drawable.g5,null));
                        break;
                    case 2:holder.cardLinearLayout.setBackground(context.getResources().getDrawable(R.drawable.g6,null));
                        break;
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AboutUsHolder extends RecyclerView.ViewHolder{
        TextView Name;
        TextView Skill1;
        TextView Skill2;
        LinearLayout cardLinearLayout;
        ImageView imageView;
        public AboutUsHolder(@NonNull View itemView) {
            super(itemView);
            Name=(TextView)itemView.findViewById(R.id.teamMateName);
            Skill1=(TextView)itemView.findViewById(R.id.skill1);
            Skill2=(TextView)itemView.findViewById(R.id.skill2);
            cardLinearLayout=(LinearLayout)itemView.findViewById(R.id.cardLinearLayout);
            imageView=(ImageView)itemView.findViewById(R.id.desireImage);
        }
    }
}
