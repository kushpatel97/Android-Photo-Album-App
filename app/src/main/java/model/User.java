package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final String storeDir = "dat";
    public static final String storeFile = "data.dat";


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


    public void addAlbum(String name){
        albums.add(new Album(name));
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
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storeDir + File.separator + storeFile));
        oos.writeObject(pdApp);
        oos.close();
    }

    public static User load() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storeDir + File.separator + storeFile));
        User userList = (User) ois.readObject();
        ois.close();
        return userList;

    }

}
