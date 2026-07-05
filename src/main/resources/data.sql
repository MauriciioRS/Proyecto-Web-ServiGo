INSERT INTO usuarios (id, nombre, email, telefono, dni, fechaNacimiento, direccion, distrito, rol, imagen, activo, password) VALUES
  (1, 'Juan Pérez', 'juan@gmail.com', '555-0001', '40123456', '1990-05-10', 'Jr. Los Olivos 120', 'Lima', 'cliente', 'juan.jpg', TRUE, '$2a$10$Q9XvV0xKz9mL8H4vY5M5qe9CIq9L6u0M2jJO9zz6Lx0nZk3D4Yk3G'),
  (2, 'María García', 'maria@gmail.com', '555-0002', '40234567', '1988-03-22', 'Av. Arequipa 450', 'Miraflores', 'proveedor', 'maria.jpg', TRUE, '$2a$10$gI4Hk1j3C0Lq0t8o0N6/1e6Wc8R5rjB4WLx1aY4Cj6qL3j6V0eJq'),
  (3, 'Carlos López', 'carlos@gmail.com', '555-0003', '40345678', '1992-11-01', 'Calle Las Flores 88', 'Surco', 'proveedor', 'carlos.jpg', TRUE, '$2a$10$4QnG1CwVY7o6M7sO6M0PqO9XQ0tGz9rTzuB8J4B3C6w0eQocC7aK'),
  (4, 'Ana Martínez', 'ana@gmail.com', '555-0004', '40456789', '1995-07-18', 'Psje. Primavera 5', 'San Borja', 'cliente', 'ana.jpg', TRUE, '$2a$10$8mY6T2R3mK4wJ0iP4e4e2uL7dT3vYw4r0T1E9fIwvM2m2BsdS3P2');

INSERT INTO servicios (id, nombre, descripcion, categoria, precio, imagen, disponible, calificacion) VALUES
  (1, 'Reparación de Plomería', 'Reparación y mantenimiento de tuberías', 'Plomería', 50.0, 'https://images.unsplash.com/photo-1585771724684-38269d6639fd?w=400&h=300&fit=crop', TRUE, 4),
  (2, 'Desagüe Desbloqueado', 'Limpieza y desobstrucción de desagües', 'Plomería', 45.0, 'https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=400&h=300&fit=crop', TRUE, 4),
  (3, 'Instalación de Lavabo', 'Montaje e instalación de lavabos y grifos', 'Plomería', 55.0, 'https://images.unsplash.com/photo-1552321554-5fefe8c9ef14?w=400&h=300&fit=crop', TRUE, 5),
  (4, 'Reparación de Fugas', 'Detección y arreglo de fugas de agua', 'Plomería', 65.0, 'https://images.unsplash.com/photo-1676210134188-4c05dd172f89?w=400&h=300&fit=crop', TRUE, 4),
  (5, 'Cambio de Válvula', 'Sustitución de válvulas y accesorios de plomería', 'Plomería', 40.0, 'https://images.unsplash.com/photo-1620653713380-7a34b773fef8?w=400&h=300&fit=crop', TRUE, 3),
  (6, 'Instalación de WC', 'Instalación y ajuste de inodoros', 'Plomería', 50.0, 'https://images.unsplash.com/photo-1552321554-5fefe8c9ef14?w=400&h=300&fit=crop', TRUE, 5),
  (7, 'Limpieza Residencial', 'Servicio de limpieza para viviendas', 'Limpieza', 35.0, 'https://images.unsplash.com/photo-1646980241033-cd7abda2ee88?w=400&h=300&fit=crop', TRUE, 5),
  (8, 'Limpieza de Cocinas', 'Desinfección y orden en áreas de cocina', 'Limpieza', 40.0, 'https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=400&h=300&fit=crop', TRUE, 4),
  (9, 'Limpieza de Alfombras', 'Aspirado y lavado de alfombras', 'Limpieza', 45.0, 'https://images.unsplash.com/photo-1581578731548-c64695cc6952?w=400&h=300&fit=crop', TRUE, 4),
  (10, 'Limpieza de Ventanas', 'Cristalizado y limpieza de ventanas', 'Limpieza', 30.0, 'https://images.unsplash.com/photo-1585421514284-efb74c2b69ba?w=400&h=300&fit=crop', TRUE, 5),
  (11, 'Limpieza Profunda', 'Limpieza profunda de baños y cocinas', 'Limpieza', 55.0, 'https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=400&h=300&fit=crop', TRUE, 5),
  (12, 'Limpieza de Muebles', 'Limpieza de tapicería y muebles', 'Limpieza', 38.0, 'https://images.unsplash.com/photo-1581578731548-c64695cc6952?w=400&h=300&fit=crop', TRUE, 4),
  (13, 'Electricista', 'Reparación de instalaciones eléctricas', 'Electricidad', 60.0, 'https://images.unsplash.com/photo-1682345262055-8f95f3c513ea?w=400&h=300&fit=crop', TRUE, 4),
  (14, 'Instalación de Iluminación', 'Colocación de lámparas y focos LED', 'Electricidad', 55.0, 'https://images.unsplash.com/photo-1565043666747-69f6646db940?w=400&h=300&fit=crop', TRUE, 5),
  (15, 'Revisión de Panel', 'Chequeo y reparación de tableros eléctricos', 'Electricidad', 70.0, 'https://images.unsplash.com/photo-1621905251189-08b45d6a269e?w=400&h=300&fit=crop', TRUE, 4),
  (16, 'Tomas y Enchufes', 'Instalación y cambio de enchufes', 'Electricidad', 42.0, 'https://images.unsplash.com/photo-1565043666747-69f6646db940?w=400&h=300&fit=crop', TRUE, 4),
  (17, 'Cortocircuitos', 'Diagnóstico de cortocircuitos y fallas', 'Electricidad', 65.0, 'https://images.unsplash.com/photo-1635335874521-7987db781153?w=400&h=300&fit=crop', TRUE, 5),
  (18, 'Mantenimiento Eléctrico', 'Mantenimiento preventivo eléctrico', 'Electricidad', 58.0, 'https://images.unsplash.com/photo-1565043666747-69f6646db940?w=400&h=300&fit=crop', TRUE, 4),
  (19, 'Jardinería', 'Mantenimiento de jardines y plantas', 'Jardinería', 40.0, 'https://images.unsplash.com/photo-1617576683096-00fc8eecb3af?w=400&h=300&fit=crop', TRUE, 3),
  (20, 'Corte de Césped', 'Corte y orden de césped y áreas verdes', 'Jardinería', 35.0, 'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400&h=300&fit=crop', TRUE, 4),
  (21, 'Plantación de Flores', 'Diseño y plantación de jardines florales', 'Jardinería', 50.0, 'https://images.unsplash.com/photo-1601001815894-4bb6c81416d7?w=400&h=300&fit=crop', TRUE, 5),
  (22, 'Poda de Árboles', 'Poda y limpieza de árboles pequeños', 'Jardinería', 55.0, 'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400&h=300&fit=crop', TRUE, 4),
  (23, 'Riego Automático', 'Instalación de sistemas de riego', 'Jardinería', 65.0, 'https://images.unsplash.com/photo-1718565524318-b58b8b86b813?w=400&h=300&fit=crop', TRUE, 4),
  (24, 'Control de Plagas', 'Tratamiento básico de plagas en jardín', 'Jardinería', 45.0, 'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400&h=300&fit=crop', TRUE, 3),
  (25, 'Reparación de Electrodomésticos', 'Arreglo de neveras, lavadoras, etc', 'Electrodomésticos', 70.0, 'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400&h=300&fit=crop', TRUE, 4),
  (26, 'Servicio de Lavadoras', 'Instalación y reparación de lavadoras', 'Electrodomésticos', 68.0, 'https://images.unsplash.com/photo-1626806819282-2c1dc01a5e0c?w=400&h=300&fit=crop', TRUE, 5),
  (27, 'Nevera y Congelador', 'Reparación de refrigeradores y congeladores', 'Electrodomésticos', 75.0, 'https://images.unsplash.com/photo-1536353284924-9220c464e262?w=400&h=300&fit=crop', TRUE, 4),
  (28, 'Microondas', 'Reparación y limpieza de microondas', 'Electrodomésticos', 40.0, 'https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=400&h=300&fit=crop', TRUE, 4),
  (29, 'Secadora', 'Instalación y mantenimiento de secadoras', 'Electrodomésticos', 55.0, 'https://images.unsplash.com/photo-1604335398980-ededcadcc37d?w=400&h=300&fit=crop', TRUE, 5),
  (30, 'Televisores', 'Arreglo de pantallas y audio', 'Electrodomésticos', 62.0, 'https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=400&h=300&fit=crop', TRUE, 4),
  (31, 'Pintura', 'Pintura de interiores y exteriores', 'Pintura', 45.0, 'https://images.unsplash.com/photo-1525909002-1b05e0c869d8?w=400&h=300&fit=crop', TRUE, 5),
  (32, 'Pintura de Fachadas', 'Aplicación de pintura en fachadas', 'Pintura', 60.0, 'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400&h=300&fit=crop', TRUE, 4),
  (33, 'Pintura Decorativa', 'Acabados y detalles decorativos', 'Pintura', 55.0, 'https://images.unsplash.com/photo-1571169173217-dd58b8c99253?w=400&h=300&fit=crop', TRUE, 5),
  (34, 'Pintura de Muebles', 'Pintura y restauración de muebles', 'Pintura', 38.0, 'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400&h=300&fit=crop', TRUE, 4),
  (35, 'Pintura de Habitaciones', 'Pintura de cuartos y oficinas', 'Pintura', 50.0, 'https://images.unsplash.com/photo-1482731215275-a1f151646268?w=400&h=300&fit=crop', TRUE, 4),
  (36, 'Pintura Rápida', 'Trabajo de pintura express en pequeñas áreas', 'Pintura', 32.0, 'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400&h=300&fit=crop', TRUE, 5);

-- Solicitudes de ejemplo (Juan = cliente id 1)
INSERT INTO solicitudes (id, clienteId, servicioId, servicioNombre, servicioCategoria, servicioPrecio, estado, creadoEn, aceptadoEn, enProgresoEn, completadoEn, actualizadoEn) VALUES
  (1, 1, 7,  'Limpieza Residencial', 'Limpieza',     35.0, 'COMPLETADO',  '2026-06-09 10:00:00', '2026-06-09 10:30:00', '2026-06-09 12:00:00', '2026-06-09 14:00:00', '2026-06-09 14:00:00'),
  (2, 1, 13, 'Electricista',         'Electricidad', 60.0, 'EN_PROGRESO', '2026-06-11 08:00:00', '2026-06-11 09:00:00', '2026-06-11 09:30:00', NULL, '2026-06-11 09:30:00');
ALTER TABLE solicitudes ALTER COLUMN id RESTART WITH 3;

INSERT INTO notificaciones (id, titulo, mensaje, tipo, fecha, leida, usuarioId, solicitudId) VALUES
  (1,  'Nueva Oferta',          'Descuento 20% en servicios de limpieza',                                'oferta',  '2026-06-08 08:00:00', FALSE, 1, NULL),
  (2,  'Servicio Completado',   'Tu servicio de plomería ha sido completado',                            'alerta',  '2026-06-08 05:00:00', TRUE,  1, NULL),
  (3,  'Nuevo Mensaje',         'María García te envió un mensaje',                                      'mensaje', '2026-06-08 09:30:00', FALSE, 1, NULL),
  (4,  'Recordatorio',          'No olvides calificar el servicio',                                      'alerta',  '2026-06-08 07:00:00', FALSE, 1, NULL),
  (5,  'Solicitud enviada',     'Tu solicitud para "Limpieza Residencial" fue enviada.',                 'mensaje', '2026-06-09 10:00:00', TRUE,  1, 1),
  (6,  'Solicitud aceptada',    'El proveedor aceptó tu solicitud de "Limpieza Residencial".',           'mensaje', '2026-06-09 10:30:00', TRUE,  1, 1),
  (7,  'Profesional en camino', 'El profesional está en camino para "Limpieza Residencial".',           'alerta',  '2026-06-09 12:00:00', TRUE,  1, 1),
  (8,  'Servicio completado',   '"Limpieza Residencial" fue completado. No olvides calificarlo.',       'oferta',  '2026-06-09 14:00:00', FALSE, 1, 1),
  (9,  'Solicitud enviada',     'Tu solicitud para "Electricista" fue enviada.',                         'mensaje', '2026-06-11 08:00:00', TRUE,  1, 2),
  (10, 'Solicitud aceptada',    'El proveedor aceptó tu solicitud de "Electricista".',                   'mensaje', '2026-06-11 09:00:00', TRUE,  1, 2),
  (11, 'Profesional en camino', 'El profesional está en camino para realizar "Electricista".',          'alerta',  '2026-06-11 09:30:00', FALSE, 1, 2);

-- Reset identity sequences so auto-generated IDs don't clash with seed data
ALTER TABLE usuarios ALTER COLUMN id RESTART WITH 5;
ALTER TABLE servicios ALTER COLUMN id RESTART WITH 37;
ALTER TABLE notificaciones ALTER COLUMN id RESTART WITH 12;

-- conversacionId = min(userId,userId2)*10000 + max(userId,userId2)
-- Usuarios: Juan=1, María=2, Carlos=3, Ana=4
-- Juan(1)↔Carlos(3) = 10003 | Juan(1)↔María(2) = 10002 | Juan(1)↔Ana(4) = 10004
INSERT INTO mensajes (id, conversacionId, senderUserId, delProveedor, texto, creadoEn) VALUES
  (1, 10003, 3, TRUE,  'Hola Juan, vi tu solicitud de reparación eléctrica. ¿Cuándo te vendría bien que vaya?', '2026-06-08 10:30:00'),
  (2, 10003, 1, FALSE, '¡Hola Carlos! Me vendría bien mañana por la tarde, alrededor de las 3 PM.', '2026-06-08 10:35:00'),
  (3, 10003, 3, TRUE,  'Perfecto, mañana a las 3 PM. ¿Cuál es la dirección exacta?', '2026-06-08 10:36:00'),
  (4, 10003, 1, FALSE, 'Av. Larco 123, Miraflores. ¿Necesitas algún material específico?', '2026-06-08 10:40:00'),
  (5, 10003, 3, TRUE,  'No, llevaré todo lo necesario. ¡Hasta mañana!', '2026-06-08 11:15:00'),
  (6, 10002, 2, TRUE,  'Hola Juan, el trabajo de plomería quedó perfecto. ¡Gracias por contratarme!', '2026-06-07 18:20:00'),
  (7, 10004, 4, FALSE, 'Hola Juan, ¿tienes disponibilidad para un servicio esta semana?', '2026-06-05 09:00:00');
ALTER TABLE mensajes ALTER COLUMN id RESTART WITH 8;
