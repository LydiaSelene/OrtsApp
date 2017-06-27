package com.example.lydia.mobappprojekt;

/**
 * Created by Lydia on 25.06.2016.
 * Diese Klasse entspricht einem Datensatz in der Tabelle der SQLite Datenbank.
 * Ein Datensatz repräsentiert ein Location-Objekt.
 */
public class LocationData {

    /**Database Id */
    private long id;
    private String name, street, postalCode, town;

    public LocationData(String name, String street, String postalCode, String town, long id) {
        this.name = name;
        this.street = street;
        this.postalCode = postalCode;
        this.town = town;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStreet() {
        return street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getTown() {
        return town;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setTown(String town) {
        this.town = town;
    }

    @Override
    public String toString() {
        return "LocationData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", street='" + street + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", town='" + town + '\'' +
                '}';
    }
}
