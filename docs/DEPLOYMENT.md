# Despliegue en AWS: EC2 y RDS

Estado: configuración preparada; no se ha publicado ni verificado una URL remota. Esta guía requiere una cuenta AWS, permisos para crear recursos y un presupuesto aprobado por el equipo.

## Recursos

1. Crear una VPC o utilizar una existente. RDS debe tener un subnet group con subredes en dos zonas de disponibilidad.
2. Crear RDS PostgreSQL con base `studyprogress`, almacenamiento cifrado y acceso público deshabilitado. Guardar credenciales fuera de Git.
3. Crear EC2 con Linux y JDK 17. El grupo de seguridad de RDS permite 5432 únicamente desde el grupo de EC2. El de EC2 permite 443 para el servicio y 22 solo desde la IP del administrador, o utilizar Session Manager.
4. Asignar un dominio y terminar TLS en un proxy Nginx o en un ALB con certificado. El backend escucha en 8080; no publicar credenciales por HTTP. Configurar el proxy para reenviar las solicitudes a la aplicación.

## Aplicación

Desde una máquina de desarrollo, ejecutar `./mvnw verify` y transferir `target/studyprogress-0.0.1-SNAPSHOT.jar` a `/opt/studyprogress/app.jar`. Crear el usuario de sistema `studyprogress`, darle lectura del JAR y preparar `/etc/studyprogress.env` con permisos 600 y propietario root. No imprimir su contenido en registros públicos.

```text
SPRING_PROFILES_ACTIVE=prod
PORT=8080
DATABASE_URL=jdbc:postgresql://ENDPOINT_RDS:5432/studyprogress?sslmode=verify-full&sslrootcert=/opt/studyprogress/global-bundle.pem
DATABASE_USERNAME=USUARIO_BD
DATABASE_PASSWORD=CONTRASEÑA_BD
JWT_SECRET=CLAVE_ALEATORIA_DE_AL_MENOS_32_BYTES
CORS_ORIGINS=https://DOMINIO_FRONTEND
MAIL_HOST=HOST_SMTP
MAIL_PORT=587
MAIL_USERNAME=USUARIO_SMTP
MAIL_PASSWORD=CONTRASEÑA_SMTP
MAIL_AUTH=true
MAIL_STARTTLS=true
MAIL_FROM=REMITENTE_VERIFICADO
```

Descargar el certificado raíz vigente desde la documentación oficial de RDS y guardarlo en la ruta configurada. Reemplazar todos los valores del ejemplo antes de iniciar. `ADMIN_EMAIL` y `ADMIN_PASSWORD` pueden definirse en el primer arranque para crear la cuenta administrativa; retirar esas variables después. La creación automática no cambia contraseñas de cuentas existentes.

Copiar `deploy/studyprogress.service` a `/etc/systemd/system/`, ejecutar `sudo systemctl daemon-reload` y `sudo systemctl enable --now studyprogress`. Consultar `journalctl -u studyprogress` si el inicio falla. Para actualizar, validar primero el JAR, respaldar la base, reemplazar el archivo y reiniciar el servicio. Hibernate actualiza el esquema en esta primera versión; antes de cambios destructivos introducir migraciones y un procedimiento de reversión.

## Verificación y evidencia

- Consultar `https://DOMINIO_API/actuator/health`; debe devolver `UP`.
- Importar Postman, cambiar `baseUrl` y ejecutar registro, login, creación de curso, tema, tarea y avance.
- Confirmar que el curso persiste después de reiniciar el servicio y que otro usuario recibe 403.
- Configurar SMTP real y comprobar recepción del correo y recordatorio. No basta con que el endpoint responda 201.
- Verificar renovación y revocación de enlaces públicos; no habilitar Swagger públicamente salvo que se decida expresamente mediante `API_DOCS_ENABLED=true`.
- Incorporar al README la URL real y conservar evidencias de EC2, RDS, reglas de red y ejecución de Actions sin exponer secretos.

Los archivos locales no acreditan los puntos de despliegue: el servicio debe estar accesible y funcionando. Tampoco acreditan por sí mismos uso de Issues/Projects o revisiones de pull requests.

Referencias: [conexión EC2-RDS](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/ec2-rds-connect.html), [TLS en RDS PostgreSQL](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/PostgreSQL.Concepts.General.SSL.html).
