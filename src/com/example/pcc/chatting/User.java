package com.example.pcc.chatting;
import java.io.Serializable;


public class User implements Serializable {
    private String userName;
    private String password;
    private String Email;
    private long  id;
    private String Mac_Address;
    private boolean signedIn;
    private static final long serialVersionUID = 1L;


    public User(String userName, String password, String email, long id, String mac_Address, boolean signedIn) {
        this.userName = userName;
        this.password = password;
        Email = email;
        this.id = id;
        Mac_Address = mac_Address;
        this.signedIn = signedIn;
    }

    public User() {

    }

    public User(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        Email = email;
    }

    public User(String username) {
        this.userName=username;
    }


    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public long getId() {
        return id;
    }

    public String getMac_Address() {
        return Mac_Address;
    }

    public boolean isSignedIn() {
        return signedIn;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setMac_Address(String mac_Address) {
        Mac_Address = mac_Address;
    }

    public void setSignedIn(boolean signedIn) {
        this.signedIn = signedIn;
    }

    public String getEmail() {
        return Email;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void printUser(){
        System.out.println("\n      id       : "+id+
                "\n      name     : "+ userName +
                "\n      Email    : "+Email+
                "\n      password : "+ password+
                "\n      signedIn : "+ signedIn+
                "\n      macAddress : "+ Mac_Address
        );
        System.out.println("\n _____________________________");
    }
}
