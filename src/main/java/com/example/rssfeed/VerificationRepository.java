package com.example.rssfeed;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface VerificationRepository extends JpaRepository<Verification, UUID> {
    Verification findByUserId(String userId);
}
