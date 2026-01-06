package com.aistory.repository;

import com.aistory.model.StorySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StorySessionRepository extends JpaRepository<StorySession, UUID> {
    List<StorySession> findByUserId(UUID userId);
    List<StorySession> findByUserIdAndStatusOrderByStartedAtDesc(UUID userId, StorySession.SessionStatus status);
}