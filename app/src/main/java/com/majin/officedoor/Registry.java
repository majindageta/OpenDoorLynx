package com.majin.officedoor;

/**
 * Created by Majin on 26/02/2017.
 */

public class Registry {

    private static Registry instance;

    public static String URL = "http://10.0.2.76?on?json";

    private boolean buttonEnabled = true;

    public static void initInstance()
    {
        if (instance == null)
        {
            // Create the instance
            instance = new Registry();
        }
    }

    public static Registry getInstance()
    {
        // Return the instance
        initInstance();
        return instance;
    }

    private Registry()
    {
        // Constructor hidden because this is a singleton
    }

    public boolean isButtonEnabled() {
        return buttonEnabled;
    }

    public void setButtonEnabled(boolean buttonEnabled) {
        this.buttonEnabled = buttonEnabled;
    }
}
