package model;

import java.io.Serializable;

public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    public String key;

    public String value;

    public Tag(String key, String value){
        this.key = key;
        this.value = value;
    }

}
