package com.ServiGo.servigo;

import com.ServiGo.servigo.model.ContactoConversacion;
import com.ServiGo.servigo.model.MensajeChat;
import com.ServiGo.servigo.model.Usuario;
import com.ServiGo.servigo.service.MensajeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MensajesEndpointTest {

    @Autowired
    private MensajeService mensajeService;

    @Test
    void buildContactosAsCliente_shouldWork() {
        Usuario juan = new Usuario();
        juan.setId(1L);
        juan.setNombre("Juan P");
        juan.setRol("cliente");
        juan.setEmail("juan@gmail.com");

        List<ContactoConversacion> contactos = mensajeService.buildContactos(juan);
        assertThat(contactos).isNotEmpty();

        ContactoConversacion first = contactos.get(0);
        assertThat(first.getNombre()).isNotNull();
        assertThat(first.getAvatarUrl()).isNotNull();
        assertThat(first.getProfesion()).isNotNull();
    }

    @Test
    void buildContactosAsProveedor_shouldWork() {
        Usuario maria = new Usuario();
        maria.setId(2L);
        maria.setNombre("Maria G");
        maria.setRol("proveedor");
        maria.setEmail("maria@gmail.com");

        List<ContactoConversacion> contactos = mensajeService.buildContactos(maria);
        assertThat(contactos).isNotEmpty();

        ContactoConversacion first = contactos.get(0);
        assertThat(first.getNombre()).isNotNull();
        assertThat(first.getAvatarUrl()).isNotNull();
        assertThat(first.getProfesion()).isNotNull();
    }

    @Test
    void obtenerMensajes_shouldWork() {
        List<MensajeChat> mensajes = mensajeService.obtenerMensajes(1L, 3L);
        assertThat(mensajes).isNotEmpty();
        for (MensajeChat m : mensajes) {
            assertThat(m.getTexto()).isNotNull();
            assertThat(m.getHoraCorta()).isNotNull();
        }
    }

    @Test
    void marcarLeidos_shouldNotThrow() {
        mensajeService.marcarLeidos(1L, 3L);
    }
}
