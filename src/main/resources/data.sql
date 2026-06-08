INSERT INTO usuarios (id, nombre, email, telefono, dni, fechaNacimiento, direccion, distrito, rol, imagen, activo, password) VALUES
  (1, 'Juan Pérez', 'juan@gmail.com', '555-0001', '40123456', '1990-05-10', 'Jr. Los Olivos 120', 'Lima', 'cliente', 'juan.jpg', TRUE, 'juan123'),
  (2, 'María García', 'maria@gmail.com', '555-0002', '40234567', '1988-03-22', 'Av. Arequipa 450', 'Miraflores', 'proveedor', 'maria.jpg', TRUE, 'maria123'),
  (3, 'Carlos López', 'carlos@gmail.com', '555-0003', '40345678', '1992-11-01', 'Calle Las Flores 88', 'Surco', 'proveedor', 'carlos.jpg', TRUE, 'carlos123'),
  (4, 'Ana Martínez', 'ana@gmail.com', '555-0004', '40456789', '1995-07-18', 'Psje. Primavera 5', 'San Borja', 'cliente', 'ana.jpg', TRUE, 'ana123');

INSERT INTO servicios (id, nombre, descripcion, categoria, precio, imagen, disponible, calificacion) VALUES
  (1, 'Reparación de Plomería', 'Reparación y mantenimiento de tuberías', 'Plomería', 50.0, 'plomeria.jpg', TRUE, 4),
  (2, 'Limpieza del Hogar', 'Servicio de limpieza profunda', 'Limpieza', 35.0, 'limpiezadelHogar.JPG', TRUE, 5),
  (3, 'Electricista', 'Reparación de instalaciones eléctricas', 'Electricidad', 60.0, 'Electricista.JPG', TRUE, 4),
  (4, 'Jardinería', 'Mantenimiento de jardines y plantas', 'Jardinería', 40.0, 'Jardineria.jpg', TRUE, 3),
  (5, 'Reparación de Electrodomésticos', 'Arreglo de neveras, lavadoras, etc', 'Electrodomésticos', 70.0, 'ReparaciondeElectrodomesticos.jpg', TRUE, 4),
  (6, 'Pintura', 'Pintura de interiores y exteriores', 'Pintura', 45.0, 'Pintor.jpg', TRUE, 5);

INSERT INTO notificaciones (id, titulo, mensaje, tipo, fecha, leida, usuarioId) VALUES
  (1, 'Nueva Oferta', 'Descuento 20% en servicios de limpieza', 'oferta', '2026-06-08 08:00:00', FALSE, 1),
  (2, 'Servicio Completado', 'Tu servicio de plomería ha sido completado', 'alerta', '2026-06-08 05:00:00', TRUE, 1),
  (3, 'Nuevo Mensaje', 'María García te envió un mensaje', 'mensaje', '2026-06-08 09:30:00', FALSE, 1),
  (4, 'Recordatorio', 'No olvides calificar el servicio', 'alerta', '2026-06-08 07:00:00', FALSE, 1);

INSERT INTO mensajes (id, conversacionId, delProveedor, texto, creadoEn) VALUES
  (1, 1, TRUE, 'Hola, vi tu solicitud para reparación eléctrica. ¿Cuándo te vendría bien que vaya?', '2026-06-08 10:30:00'),
  (2, 1, FALSE, '¡Hola Carlos! Me vendría bien mañana por la tarde, alrededor de las 3 PM.', '2026-06-08 10:35:00'),
  (3, 1, TRUE, 'Perfecto, mañana a las 3 PM. ¿Cuál es la dirección exacta?', '2026-06-08 10:36:00'),
  (4, 1, FALSE, 'Av. Larco 123, Miraflores. ¿Necesitas algún material específico?', '2026-06-08 10:40:00'),
  (5, 1, TRUE, 'No, llevaré todo lo necesario. ¿Cuándo podemos agendar la reparación?', '2026-06-08 11:15:00'),
  (6, 2, TRUE, 'El trabajo quedó perfecto, gracias!', '2026-06-07 18:20:00'),
  (7, 3, TRUE, '¿Tienes disponibilidad esta semana?', '2026-06-05 09:00:00');
