package org.tutorial.clique.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.tutorial.clique.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender.id = :userId AND m.receiver.id = :friendId) OR " +
            "(m.sender.id = :friendId AND m.receiver.id = :userId) " +
            "ORDER BY m.timestamp ASC")
    List<Message> findBySenderAndReceiver(@Param("userId") Long userId, @Param("friendId") Long friendId);

    List<Message> findByGroupIdOrderByTimestampAsc(Long groupId);
}
