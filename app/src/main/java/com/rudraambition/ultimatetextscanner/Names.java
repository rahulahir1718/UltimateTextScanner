package com.rudraambition.ultimatetextscanner;

public class Names {
    private String Name;
    private String Skill1;
    private String Skill2;
    private String role;
    private int image;

    public Names(){}
    public Names(String Name,String Skill1,String Skill2,String role,int image)
    {
        this.Name=Name;
        this.Skill1=Skill1;
        this.Skill2=Skill2;
        this.role=role;
        this.image=image;
    }
    public String getName()
    {
        return Name;
    }
    public String getSkill1()
    {
        return Skill1;
    }
    public String getSkill2()
    {
        return Skill2;
    }
    public String getRole()
    {
        return role;
    }
    public int getImage()
    {
        return image;
    }
}
