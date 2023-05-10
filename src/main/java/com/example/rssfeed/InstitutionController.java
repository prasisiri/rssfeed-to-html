package com.example.rssfeed;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.UUID;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class InstitutionController {

    @GetMapping("/institutions")
    public List<Institution> getInstitutions() {
        List<Institution> institutions = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            Institution institution = new Institution();
            institution.setCode("bank-" + i);
            institution.setCreatedAt("2016-09-21T22:03:55+00:00");
            institution.setGuid(UUID.randomUUID().toString());
            institution.setMediumLogoUrl("https://content.moneydesktop.com/storage/MD_Assets/Ipad%20Logos/100x100/INS-1572a04c-912b-59bf-5841-332c7dfafaef_100x100.png");
            institution.setName("Bank-" + i);
            institution.setPopularity(72664);
            institution.setSmallLogoUrl("https://content.moneydesktop.com/storage/MD_Assets/Ipad%20Logos/50x50/INS-1572a04c-912b-59bf-5841-332c7dfafaef_50x50.png");
            institution.setSupportsAccountIdentification(true);
            institution.setSupportsAccountVerification(true);
            institution.setSupportsOauth(false);
            institution.setSupportsTransactionHistory(true);
            institution.setUpdatedAt("2022-04-11T17:38:27+00:00");
            //institution.setUrl("https://www.bank" + i + ".com");
            institution.setUrl("http://localhost:3005");
            institutions.add(institution);
        }
        return institutions;
    }

    @GetMapping("/institutions/typeahead")
    public List<Institution> getInstitutionsBySearchQuery(@RequestParam String query) {
        List<Institution> matchingInstitutions = new ArrayList<>();
        List<Institution> institutions = getInstitutions();
        for (Institution inst : institutions) {
            if (inst.getName().toLowerCase().contains(query.toLowerCase())) {
                matchingInstitutions.add(inst);
            }
        }
        return matchingInstitutions;
    }
    
}

