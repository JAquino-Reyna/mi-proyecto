# Revisión de la entrega

| Criterio | Implementación | Evidencia o pendiente |
| --- | --- | --- |
| Entidades | Ocho entidades, relaciones y tablas intermedias | `model/`, índices, campos obligatorios y control de versión |
| DTOs | Solicitudes y respuestas específicas | `dto/` y `mapper/StudyMapper` |
| Arquitectura | Controller, service, repository | Inyección por constructor y transacciones en servicios |
| Errores | Jerarquía de nueve excepciones de dominio | Respuestas uniformes y validaciones |
| Seguridad | BCrypt, JWT, refresh rotativo, roles y propiedad | Pruebas de credenciales, firma, expiración y permisos |
| REST | API versionada con CRUD | Swagger y 43 solicitudes Postman |
| Asincronía | Registro, colaboración y progreso | Listeners posteriores al commit y pool acotado |
| Correo | SMTP y plantilla HTML | Prueba MIME y escape HTML; configurar proveedor para envío externo |
| Despliegue | Docker, PostgreSQL y guía EC2/RDS | Pendiente crear recursos y verificar URL pública |
| Documentación | Informe README, diagramas e instrucciones | Colección Postman con variables y ejemplos |
| GitHub | Workflow CI y plantillas | Pendiente ejecución remota, issues, tablero y revisiones reales |

Las pruebas se ejecutan con `./mvnw verify`. No se ha medido cobertura porcentual ni se afirma que alcance el bonus del 80 %. Los contenedores requieren una instalación Docker para su validación. La entrega corresponde al backend; el frontend, OAuth y exportación de reportes quedan fuera de esta versión.
