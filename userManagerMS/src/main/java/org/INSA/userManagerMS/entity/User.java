package org.INSA.userManagerMS.entity;

public class User {

    private String username;
    private int type;
    private String mdp;

    public User() {}

    public User(String username, int type, String mdp) {
        this.username = username;
        this.type = type;
        this.mdp = mdp;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }
}