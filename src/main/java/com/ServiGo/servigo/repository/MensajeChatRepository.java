package com.ServiGo.servigo.repository;

import com.ServiGo.servigo.model.MensajeChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MensajeChatRepository extends JpaRepository<MensajeChat, Long> {
    List<MensajeChat> findByConversacionIdOrderByCreadoEnAsc(Long conversacionId);
    Optional<MensajeChat> findTopByConversacionIdOrderByCreadoEnDesc(Long conversacionId);
    List<MensajeChat> findByConversacionIdAndIdGreaterThanOrderByCreadoEnAsc(Long conversacionId, Long lastId);
}
