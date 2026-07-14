package com.ServiGo.servigo.repository;

import com.ServiGo.servigo.model.MensajeChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MensajeChatRepository extends JpaRepository<MensajeChat, Long> {
    List<MensajeChat> findByConversacionIdOrderByCreadoEnAsc(Long conversacionId);
    Optional<MensajeChat> findTopByConversacionIdOrderByCreadoEnDesc(Long conversacionId);
    List<MensajeChat> findByConversacionIdAndIdGreaterThanOrderByCreadoEnAsc(Long conversacionId, Long lastId);
    long countByConversacionIdAndSenderUserIdNotAndLeidoFalse(Long conversacionId, Long senderUserId);
    List<MensajeChat> findByConversacionIdAndSenderUserIdNotAndLeidoFalse(Long conversacionId, Long senderUserId);

    @Query("SELECT DISTINCT m.conversacionId FROM MensajeChat m")
    List<Long> findAllDistinctConversacionIds();

    @Query("SELECT m FROM MensajeChat m WHERE m.conversacionId IN :conversacionIds AND m.senderUserId != :userId AND m.leido = false")
    List<MensajeChat> countUnreadByConversacionIds(@Param("conversacionIds") List<Long> conversacionIds, @Param("userId") Long userId);

    @Query("SELECT m FROM MensajeChat m WHERE m.conversacionId IN :conversacionIds")
    List<MensajeChat> findByConversacionIds(@Param("conversacionIds") List<Long> conversacionIds);

    @Query("SELECT m.conversacionId, MAX(m.id) as maxId FROM MensajeChat m WHERE m.conversacionId IN :conversacionIds GROUP BY m.conversacionId")
    List<Object[]> findLastMessageByConversacionIds(@Param("conversacionIds") List<Long> conversacionIds);

    @Query("SELECT m FROM MensajeChat m WHERE m.id IN (SELECT MAX(m2.id) FROM MensajeChat m2 WHERE m2.conversacionId IN :conversacionIds GROUP BY m2.conversacionId)")
    List<MensajeChat> findLastMessagesByConversacionIds(@Param("conversacionIds") List<Long> conversacionIds);
}
