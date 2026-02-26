package com.team1.f1_api.model;


//Track class with id, name, countryID for now
public class Track {
    private String id;
    private String name;
    private String countryID;
    public Track(String id, String name, String countryID){
        this.id = id;
        this.name = name;
        this.countryID = countryID;
    }
    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getCountryID(){
        return countryID;
    }
}
