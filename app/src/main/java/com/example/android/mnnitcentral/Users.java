package com.example.android.mnnitcentral;

/**
 * Created by LENOVO on 3/24/2018.
 */

public class Users {
    private String name;
    private String image;
    private String desc;
    private String category;
    public Users(){}
    public Users(String name,String image,String desc,String category)
    {
        this.name = name;
        this.image = image;
        this.desc = desc;
        this.category = category;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name) { this.name = name; }
    public String getImage()
    {
        return image;
    }
    public void setImage(String image)
    {
        this.image=image;
    }
    public String getDesc()
    {
        return desc;
    }
    public void setDesc(String desc)
    {
        this.desc= desc;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}
