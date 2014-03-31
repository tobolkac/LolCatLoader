package com.claytobolka.lolloader.app;

/**
 * Created by tobolkac on 3/31/14.
 *
 * Object to hold the imformation about the photos returned in the xml from
 * the flickr api
 */
public class FlickrPhoto {
    private String id;
    private String owner;
    private String secret;
    private String server;
    private String farm;
    private String title;

    public FlickrPhoto()
    {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getFarm() {
        return farm;
    }

    public void setFarm(String farm) {
        this.farm = farm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
