# Validación de StudyProgress

Fecha: 24 de septiembre de 2026.

- Compilación y empaquetado del JAR: correctos, con JDK 21 y destino Java 17.
- Pruebas automatizadas: 13, sin fallos ni errores.
- Aplicación empaquetada: arranque comprobado en un puerto local temporal.
- Solicitudes de la colección Postman ejecutadas por HTTP: 43, todas con el estado esperado.
- Correo: 3 mensajes recibidos por un servidor SMTP local de prueba; contenido HTML comprobado.
- OpenAPI: documento disponible con 25 rutas.
- README: 1897 palabras, dentro del rango solicitado.
- Revisión de whitespace con `git diff --check`: sin errores.

Las pruebas HTTP usan una base H2 temporal y un buzón local, sin enviar mensajes a destinatarios externos. La prueba automatizada de recordatorios verifica fallo SMTP, reintento y entrega. El JAR se generó después de pasar las pruebas; la descarga de dependencias requirió una ejecución adicional por una limitación de resolución DNS del entorno.

No se han validado Docker, PostgreSQL remoto ni AWS en este equipo. La publicación en AWS, el proveedor SMTP real y las evidencias de Issues/Projects y revisiones siguen pendientes. Los cambios permanecen locales, sin commit ni push automático.
