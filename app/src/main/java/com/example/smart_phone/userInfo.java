package com.example.smart_phone;

import java.util.Date;

public class userInfo {
    private static String UID;
    private static String name;
    private static String email;
    private static String provider;
    private static Date registeredTime;

    //GETTER
    public static String getUID()
    {
        return UID;
    }

    public static String getName()
    {
        return name;
    }

    public static String getEmail()
    {
        return email;
    }

    public static String getProvider()
    {
        return provider;
    }
    //SETTER

    public static void setUID(String news)
    {
        UID = news;
    }

    public static void setName(String news)
    {
        name = news;
    }

    public static void setEmail(String news)
    {
        email = news;
    }

    public static void setProvider(String news)
    {
        provider = news;
    }

}
