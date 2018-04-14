package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Name of the album
     */
    public String albumName;

    /**
     * List of photos in an album
     */
    public ArrayList<Photo> photos;

    /**
     * Current photo
     */
    public Photo currentPhoto;

    /**
     * Number of photos in an album
     */
    public int numeberOfPhotos;

    public Album(String albumName){
        this.albumName = albumName;
        photos = new ArrayList<Photo>();
    }

    public void addPhoto(){

    }

    public void deletePhoto(){

    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public Photo getCurrentPhoto() {
        return currentPhoto;
    }

    public void setCurrentPhoto(Photo currentPhoto) {
        this.currentPhoto = currentPhoto;
    }

    public int getNumeberOfPhotos() {
        return numeberOfPhotos;
    }

}
