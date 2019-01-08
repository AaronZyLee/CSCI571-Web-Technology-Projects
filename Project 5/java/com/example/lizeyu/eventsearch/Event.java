package com.example.lizeyu.eventsearch;

public class Event {
    String id;
    String name;
    String time;
    String venueName;
    String category;
    boolean favorite;
    int icon;
    //int favorite_icon;
    Event(String id,String name,String time,String venueName,String category){
        this.id = id;this.name=name;this.time=time;this.venueName=venueName;this.category=category;
        favorite=false;
        switch (category){
            case "Music":
                this.icon=R.drawable.music_icon;
                break;
            case "Sports":
                this.icon=R.drawable.sport_icon;
                break;
            case "Arts & Theatre":
                this.icon=R.drawable.art_icon;
                break;
            case "Film":
                this.icon=R.drawable.film_icon;
                break;
            case "Miscellaneous":
                this.icon=R.drawable.miscellaneous_icon;
                break;
            default:
                this.icon=R.drawable.sport_icon;
        }
    }
}
