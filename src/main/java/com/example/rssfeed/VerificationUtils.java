package com.example.rssfeed;

public class VerificationUtils {

    public static Verification createUserWithUserCreatedState(Verification verification) {
        verification.setVerificationState("USER_CREATED");
        return verification;
    }
}

