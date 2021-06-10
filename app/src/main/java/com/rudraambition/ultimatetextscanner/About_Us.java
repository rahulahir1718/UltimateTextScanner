package com.rudraambition.ultimatetextscanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.rudraambition.ultimatetextscanner.R;

import java.util.ArrayList;
import java.util.List;

public class About_Us extends AppCompatActivity {


    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private List<Names> developers;
    private List<Names> others;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about__us);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("About Us");

        developers=new ArrayList<>();
        others=new ArrayList<>();

        recyclerView1=(RecyclerView)findViewById(R.id.firstRecyclerView);
        recyclerView2=(RecyclerView)findViewById(R.id.secondrecyclerView);

        developers.add(new Names("Rahul Kumbharvadiya","Android App Developer","","1",R.drawable.i1));
        developers.add(new Names("Akshay Fichadia","Flutter Developer","(iOS And Android)","1",R.drawable.i2));
        developers.add(new Names("Abhishek Kathiriya","Flutter Developer","(iOS And Android)","1",R.drawable.i3));
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        AboutUsAdapter aboutUsAdapter1=new AboutUsAdapter(this,developers);
        recyclerView1.setAdapter(aboutUsAdapter1);


        others.add(new Names("Ketan Goswami","Graphic Designer","Art Designer","2",R.drawable.i4));
        others.add(new Names("Kashyap Vekariya","Graphic Designer","3D Builder","2",R.drawable.i5));
        others.add(new Names("Sweta Dholariya","Graphic Designer","Art Designer","2",R.drawable.i6));
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        AboutUsAdapter aboutUsAdapter2=new AboutUsAdapter(this,others);
        recyclerView2.setAdapter(aboutUsAdapter2);
    }
}