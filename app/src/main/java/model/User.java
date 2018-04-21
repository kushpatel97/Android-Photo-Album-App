package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * List of albums for each user
     */
    public ArrayList<Album> albums;

    /**
     * Current user
     */
    public Album currentAlbum;

    /**
     * Number of albums a user has
     */
    public int numberOfAlbums;


    public User(){
        albums = new ArrayList<Album>();
        this.currentAlbum = null;
        numberOfAlbums = 0;
    }


    public void addAlbum(Album name){
        albums.add(name);
        numberOfAlbums++;
    }

    public void deleteAlbum(int index){
        albums.remove(index);
        numberOfAlbums--;
    }

    public boolean albumExists(String albumname){
        for(Album album : albums){
            if(album.getAlbumName().equals(albumname)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Photo> searchTags(ArrayList<Tag> taggedlist){
        ArrayList<Photo> photolist = new ArrayList<Photo>();
        //Used to make sure no duplicates
        HashSet<Photo> check = new HashSet<Photo>();
        for(Tag tag : taggedlist) {
            for(Album album : albums) {
                for(Photo photo : album.getPhotos()) {
                    if(photo.tagExists(tag.key, tag.value)) {
                        if(photo.containsTagValue(tag.value.toLowerCase())) {
                            check.add(photo);
                        }
                    }
                }

            }
        }
        photolist.addAll(check);
        return photolist;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }


    public Album getCurrentAlbum() {
        return currentAlbum;
    }

    public void setCurrentAlbum(Album currentAlbum) {
        this.currentAlbum = currentAlbum;
    }

    public int getNumberOfAlbums() {
        return numberOfAlbums;
    }

    public static void save(User pdApp) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("/data/data/rutgers.cs213androidproject/files/data.dat"));
        oos.writeObject(pdApp);
        oos.close();
    }

    public static User load() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("/data/data/rutgers.cs213androidproject/files/data.dat"));
        User userList = (User) ois.readObject();
        ois.close();
        return userList;

    }

}
