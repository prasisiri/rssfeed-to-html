package com.example.rssfeed;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.UUID;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import java.util.concurrent.TimeUnit;

@RestController
public class InstitutionController {

    @Autowired
    private RedisTemplate<String, Institution> redisTemplate;

    @Autowired
    private VerificationRepository verificationRepository;

    private static final String INSTITUTIONS_CACHE_PREFIX = "institutions:";

    private Institution getCachedInstitutionByGuid(String guid) {
        Set<String> keys = redisTemplate.keys(INSTITUTIONS_CACHE_PREFIX + "*" + guid);
        if (keys == null || keys.isEmpty()) {
            return null;
        }
        String key = keys.iterator().next();
        Institution institution = redisTemplate.opsForValue().get(key);
        return institution;
    }

    @GetMapping("/institutions/by-guid")
    public ResponseEntity<Institution> getInstitutionByGuid(@RequestParam String guid) {
    Institution institution = getCachedInstitutionByGuid(guid);
    if (institution == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(institution);
}

    

    @GetMapping("/institutions")
    public List<Institution> getInstitutions() {
        List<Institution> institutions = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            Institution institution = new Institution();
            institution.setCode("bank-" + i);
            institution.setCreatedAt("2016-09-21T22:03:55+00:00");
            institution.setGuid(UUID.randomUUID().toString());
            institution.setMediumLogoUrl("https://content.abccompany.com/storage/MD_Assets/Ipad%20Logos/100x100/INS-1572a04c-912b-59bf-5841-332c7dfafaef_100x100.png");
            institution.setName("Bank-" + i);
            institution.setPopularity(72664);
            institution.setSmallLogoUrl("https://content.abccompany.com/storage/MD_Assets/Ipad%20Logos/50x50/INS-1572a04c-912b-59bf-5841-332c7dfafaef_50x50.png");
            institution.setSupportsAccountIdentification(true);
            institution.setSupportsAccountVerification(true);
            institution.setSupportsOauth(false);
            institution.setSupportsTransactionHistory(true);
            institution.setUpdatedAt("2022-04-11T17:38:27+00:00");
            //institution.setUrl("https://www.bank" + i + ".com");
            institution.setUrl("http://localhost:3005");
            institutions.add(institution);
        }
        // Cache institutions in Redis
        cacheInstitutions(institutions);

        return institutions;
    }

    @GetMapping("/institutions/typeahead")
    public List<Institution> getInstitutionsBySearchQuery(@RequestParam String prefix) {
        List<Institution> matchingInstitutions = getCachedInstitutionsByPrefix(prefix);
        return matchingInstitutions;
    }
    
    

    private void cacheInstitutions(List<Institution> institutions) {
        ValueOperations<String, Institution> valueOperations = redisTemplate.opsForValue();
        for (Institution institution : institutions) {
            String key = INSTITUTIONS_CACHE_PREFIX + institution.getName().toLowerCase() + ":" + institution.getGuid();
            valueOperations.set(key, institution, 24, TimeUnit.HOURS); // Cache for 24 hours
        }
    }

    private List<Institution> getCachedInstitutionsByPrefix(String prefix) {
        Set<String> keys = redisTemplate.keys(INSTITUTIONS_CACHE_PREFIX + prefix.toLowerCase() + "*");
        if (keys == null || keys.isEmpty()) {
            return new ArrayList<>();
        }
        List<Institution> institutions = redisTemplate.opsForValue().multiGet(keys);
        return institutions;
    }

    private List<Institution> getCachedInstitutionsByName(String name) {
        Set<String> keys = redisTemplate.keys(INSTITUTIONS_CACHE_PREFIX + name.toLowerCase() + "*");
        if (keys == null || keys.isEmpty()) {
            return new ArrayList<>();
        }
        List<Institution> institutions = redisTemplate.opsForValue().multiGet(keys);
        return institutions;
    }


    
    @PostMapping("/create-user-oauth-member-pending")
    public ResponseEntity<String> createUserAndOauthMemberPending(@RequestBody Map<String, String> requestBody) {
        String userId = requestBody.get("userId");
        String fiGuid = requestBody.get("fiGuid");
    
        // Create a new verification object and set the initial state
        Verification verification = new Verification();
        verification.setUserId(userId);
        verification.setVerificationState("USER_CREATED");
    
        // Save the new verification record with the USER_CREATED state
        Verification savedVerification = verificationRepository.save(verification);
    
        // Update the verification record with the provided values
        if (savedVerification != null) {
            savedVerification.setFiGuid(fiGuid);
            savedVerification.setVerificationMethod("OAUTH");
            savedVerification.setMemberId("mbr-123");
            savedVerification.setVerificationState("MEMBER_CREATION_PENDING");
            savedVerification.setVerificationStatus("PENDING");
            verificationRepository.save(savedVerification);
            return ResponseEntity.ok("User created and verification updated successfully");
        } else {
            // Handle case where verification record for the user is not saved
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user and updating verification");
        }
    }
    
    

    
    
    
}

