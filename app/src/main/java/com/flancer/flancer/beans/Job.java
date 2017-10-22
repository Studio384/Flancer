package com.flancer.flancer.beans;

/**
 * Created by Yannick on 17/10/2017.
 */

public class Job {
    private int id;
    private String title;
    private int company_id;
    private float minimum_bid;
    private String description;
    private String street;
    private String number;
    private String zip;
    private String city;
    private String country;

    /*
    public Job __construct( int id, String title, int company_id, float minimum_bid, String description, String street, String number, String city, String zip, String country ) {
        this.setId( id );
        this.setTitle( title );
        this.setCompanyId( company_id );
        this.setMinimumBid( minimum_bid );
        this.setDescription( description );
        this.setStreet( street );
        this.setNumber( number );
        this.setCity( city );
        this.setZip( zip );
        this.setCountry( country );
    }
    */

    // Setters
    public void setId( int id ) {
        this.id = id;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public void setCompanyId( int company_id ) {
        this.company_id = company_id;
    }

    public void setMinimumBid( float minimum_bid ) {
        this.minimum_bid = minimum_bid;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public void setStreet( String street ) {
        this.street = street;
    }

    public void setNumber( String number ) {
        this.number = number;
    }

    public void setCity( String city ) {
        this.city = city;
    }

    public void setZip( String zip ) {
        this.zip = zip;
    }

    public void setCountry( String country ) {
        this.country = country;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getCompanyId() {
        return company_id;
    }

    public float getMinimumBid() {
        return minimum_bid;
    }

    public String getDescription() {
        return description;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getCity() {
        return city;
    }

    public String getZip() {
        return zip;
    }

    public String getCountry() {
        return country;
    }
}
