package com.app.awaaz.Models;

public class EventsModel {
    String id;
    String event_title;
    String event_description;
    String event_image;
    String eventer_name;
    String eventer_photo;
    String date;

    public EventsModel(String id, String event_title, String event_description, String event_image, String eventer_name, String eventer_photo, String date) {
        this.id = id;
        this.event_title = event_title;
        this.event_description = event_description;
        this.event_image = event_image;
        this.eventer_name = eventer_name;
        this.eventer_photo = eventer_photo;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getEvent_title() {
        return event_title;
    }

    public String getEvent_description() {
        return event_description;
    }

    public String getEvent_image() {
        return event_image;
    }

    public String getEventer_name() {
        return eventer_name;
    }

    public String getEventer_photo() {
        return eventer_photo;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "EventsModel{" +
                "id='" + id + '\'' +
                ", event_title='" + event_title + '\'' +
                ", event_description='" + event_description + '\'' +
                ", event_image='" + event_image + '\'' +
                ", eventer_name='" + eventer_name + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
