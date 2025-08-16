package com.ycyw.demo.repositories;

import com.ycyw.demo.models.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByUserIdAndName(Long userId, String name);
}
