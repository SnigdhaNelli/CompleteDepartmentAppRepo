package com.example.mycseapp;

/**
 * Created by sri on 10/8/17.
 */
public class Item {
    private String name ;
    private String message ;
    public Item(String n, String m)
    {
        super() ;
        this.name = n ;
        this.message = m ;
    }
    public String getName()
    {
        return this.name ;
    }
    public String getMessage()
    {
        return this.message ;
    }
}
