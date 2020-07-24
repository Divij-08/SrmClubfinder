package com.webarch.Clubfinder;

public class cards {
    private String userId;
    private String name;
    private String profileImageUrl;
    private String linkedin;
    private String description;
    private String skills;
    public cards(String userId,String name,String profileImageUrl,String linkedin,String description,String skills)
    {

      this.userId=userId;
      this.name=name;
      this.profileImageUrl=profileImageUrl;
      this.linkedin=linkedin;
      this.description=description;
      this.skills=skills;
    }


    public String getUserId(){
        return userId;
    }
    public void setUserId(String userId){
        this.userId=userId;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
    public String getLinkedin(){
        return linkedin;
    }
    public void setLinkedin(String linkedin){
        this.linkedin=linkedin;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description=description;
    }
    public String getSkills(){
        return skills;
    }
    public void setSkills(String skills){
        this.skills=skills;
    }
}
