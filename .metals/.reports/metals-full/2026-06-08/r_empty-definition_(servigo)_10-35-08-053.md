error id: file:///C:/Users/mauri/Downloads/Proyecto-Web-ServiGo-main/src/main/java/com/ServiGo/servigo/repository/MensajeChatRepository.java:org/springframework/data/jpa/repository/JpaRepository#
file:///C:/Users/mauri/Downloads/Proyecto-Web-ServiGo-main/src/main/java/com/ServiGo/servigo/repository/MensajeChatRepository.java
empty definition using pc, found symbol in pc: org/springframework/data/jpa/repository/JpaRepository#
semanticdb not found
empty definition using fallback
non-local guesses:

offset: 137
uri: file:///C:/Users/mauri/Downloads/Proyecto-Web-ServiGo-main/src/main/java/com/ServiGo/servigo/repository/MensajeChatRepository.java
text:
```scala
package com.ServiGo.servigo.repository;

import com.ServiGo.servigo.model.MensajeChat;
import org.springframework.data.jpa.repository.@@JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MensajeChatRepository extends JpaRepository<MensajeChat, Long> {
    List<MensajeChat> findByConversacionIdOrderByCreadoEnAsc(Long conversacionId);
    Optional<MensajeChat> findTopByConversacionIdOrderByCreadoEnDesc(Long conversacionId);
}

```


#### Short summary: 

empty definition using pc, found symbol in pc: org/springframework/data/jpa/repository/JpaRepository#