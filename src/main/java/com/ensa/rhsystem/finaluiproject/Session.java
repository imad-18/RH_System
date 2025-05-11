package com.ensa.rhsystem.finaluiproject;

public class Session {
    public static int loggedInUserId;
    public static String loggedInUserRole;

    public static void clear() {
        loggedInUserId = 0;
        loggedInUserRole = null;
    }
}
