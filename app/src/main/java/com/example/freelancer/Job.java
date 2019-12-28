package com.example.freelancer;

import java.io.Serializable;
import java.util.ArrayList;

public class Job implements Serializable {

    public Job(String _name, String _description, String _type, String _cv, ArrayList<Integer> _price, ArrayList<String> _price_description, int _id, String _username, String _fullname) {
        this._name = _name;
        this._description = _description;
        this._type = _type;
        this._cv = _cv;
        this._price = _price;
        this._price_description = _price_description;
        this._id = _id;
        this._username = _username;
        this._fullname = _fullname;
    }

    private String _name;
    private String _description;
    private String _type;
    private String _cv;
    private ArrayList<Integer> _price;
    private ArrayList<String> _price_description;
    private int _id;
    private String _username;
    private String _fullname;

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String get_description() {
        return _description;
    }

    public void set_description(String _description) {
        this._description = _description;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public String get_cv() { return _cv; }

    public void set_cv(String _cv) { this._cv = _cv; }

    public ArrayList<Integer> get_price() { return _price; }

    public void set_price(ArrayList<Integer> _price) { this._price = _price; }

    public ArrayList<String> get_price_description() { return _price_description; }

    public void set_price_description(ArrayList<String> _price_description) { this._price_description = _price_description; }

    public int get_id() { return _id; }

    public void set_id(int _id) { this._id = _id; }

    public String get_username() { return _username; }

    public void set_username(String _username) { this._username = _username; }

    public String get_fullname() { return _fullname; }

    public void set_fullname(String _fullname) { this._fullname = _fullname; }
}
