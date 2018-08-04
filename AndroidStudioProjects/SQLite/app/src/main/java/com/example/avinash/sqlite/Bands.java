package com.example.avinash.sqlite;



public class Bands {

    private int _id;
    private String _bandName;

    public Bands(){
    }

    public Bands(String bandName) {
        this._bandName = bandName;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_bandName(String _bandName) {
        this._bandName = _bandName;
    }

    public int get_id() {
        return _id;
    }

    public String get_bandName() {
        return _bandName;
    }
}
