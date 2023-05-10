package com.example.rssfeed;

import lombok.Data;

@Data
public class Institution {
    private String code;
    private String createdAt;
    private String guid;
    private String mediumLogoUrl;
    private String name;
    private int popularity;
    private String smallLogoUrl;
    private boolean supportsAccountIdentification;
    private boolean supportsAccountVerification;
    private boolean supportsOauth;
    private boolean supportsTransactionHistory;
    private String updatedAt;
    private String url;
}
