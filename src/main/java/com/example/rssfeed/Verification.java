package com.example.rssfeed;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "verification")
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String memberId;
    private String userId;
    private String fiGuid;
    private String verificationMethod;
    private String verificationState;
    private String verificationStatus;
    
    // Add more fields as needed
}
