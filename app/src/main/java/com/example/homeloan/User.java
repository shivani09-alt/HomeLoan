package com.example.homeloan;

public class User {
    private  String fName,lName,contact,projectName,flatDetails,propertyCost,loadRequirement,state,city,id;

    public User( String id,String fName, String lName, String contact, String projectName, String flatDetails, String propertyCost, String state, String city, String loadRequirement) {
        this.fName = fName;
        this.lName = lName;
        this.contact = contact;
        this.projectName = projectName;
        this.flatDetails = flatDetails;
        this.propertyCost = propertyCost;
        this.loadRequirement = loadRequirement;
        this.state = state;
        this.city = city;
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getContact() {
        return contact;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getFlatDetails() {
        return flatDetails;
    }

    public String getPropertyCost() {
        return propertyCost;
    }

    public String getLoadRequirement() {
        return loadRequirement;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getId() {
        return id;
    }
}
