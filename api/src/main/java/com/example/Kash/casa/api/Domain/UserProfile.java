package com.example.Kash.casa.api.Domain;

import com.example.Kash.casa.api.Helpers.Deref;
import com.example.Kash.casa.api.Helpers.SaltedHash;
import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Subclass;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The object model for the data we are sending through endpoints
 */

@Subclass(name = "User")
public class UserProfile extends CasaEntity
{
    public static class Homie {}
    public static class Dev {}

    @Index
    private String Username;
    @Index
    private String Email;
    private String PasswordSalt;
    private String PasswordHash;
    private Date CreatedDate;
    private Date LastLogInDate;

    @Index
    @Load(Homie.class)
    private List<Ref<UserProfile>> Homies;
    @Index
    @Load(Dev.class)
    private List<Ref<Device>> Devices;

    public UserProfile()
    {
        Homies = new ArrayList<>();
        Devices = new ArrayList<>();
    }

    public UserProfile(String username, String email, String clearTextPassword)
    {
        Username = username;
        Email = email;
        CreatedDate = new Date();
        Devices = new ArrayList<>();
        Homies = new ArrayList<>();
        setPassword(clearTextPassword);
    }

    public void setPassword(String clearTextPassword)
    {
        SaltedHash saltedHash = new SaltedHash(clearTextPassword);
        PasswordHash = saltedHash.Hash;
        PasswordSalt = saltedHash.Salt;
    }

    public boolean verifyPassword(String clearTextPassword)
    {
        return new SaltedHash(this.PasswordSalt, this.PasswordHash).verify(clearTextPassword);
    }

    public String getUsername()
    {
        return Username;
    }
    public void setUsername(String username)
    {
        Username = username;
    }

    public String getEmail()
    {
        return Email;
    }
    public void setEmail(String email)
    {
        Email = email;
    }

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public List<Device> getDevices()
    {
        return Deref.deRef(Devices);
    }

    public void addDevice(Device device){
        Devices.add(Ref.create(device));
    }

    public String getPasswordSalt()
    {
        return PasswordSalt;
    }

    public String getPasswordHash()
    {
        return PasswordHash;
    }

    public Date getCreatedDate()
    {
        return CreatedDate;
    }
    public void setCreatedDate(Date createdDate)
    {
        CreatedDate = createdDate;
    }

    public Date getLastLogInDate()
    {
        return LastLogInDate;
    }
    public void setLastLogInDate(Date lastLogInDate)
    {
        LastLogInDate = lastLogInDate;
    }

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public List<UserProfile> getHomies()
    {
        return Deref.deRef(Homies);
    }
}