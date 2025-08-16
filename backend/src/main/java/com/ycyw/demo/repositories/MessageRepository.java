package com.ycyw.demo.repositories;

import com.ycyw.demo.models.Message;
import com.ycyw.demo.models.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationOrderByCreatedAtAsc(Conversation conversation);
}
