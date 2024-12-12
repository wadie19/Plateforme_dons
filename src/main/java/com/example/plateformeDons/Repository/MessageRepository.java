package com.example.plateformeDons.Repository;

import com.example.plateformeDons.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
