# PcPedia API

API de arrendamiento tecnologico para ECAT Leasing. Gestiona inventario y catalogo de equipos, solicitudes/cotizaciones/contratos, facturacion/pagos, tickets de soporte y tableros, con autenticacion JWT y control de roles.

## Stack y arquitectura
- Java 21, Spring Boot 3.4 (Web, Data JPA, Security, Validation, Actuator)
- MySQL (Hibernate ddl-auto=update), Maven Wrapper
- JWT (jjwt), Lombok, MapStruct, i18n via properties
- OpenAPI con springdoc (`/swagger-ui.html`, `/api-docs`)
- Patron CQRS ligero (commands/queries + handlers), DTOs de request/response

## Modulos principales
- IAM: login JWT, cambio de contrasena, gestion de usuarios cliente (rol ADMIN).
- Inventario/Catalogo: alta/edicion/baja de equipos con precio (admin) y catalogo sin precio para clientes.
- Ventas: solicitudes de arriendo (cliente), cotizaciones (admin crea/envia; cliente acepta/rechaza), contratos (admin crea/renueva/cancela) y equipos del cliente.
- Billing: facturacion y registro de pagos (admin).
- Soporte: tickets con comentarios y cambios de estado; cliente crea, admin resuelve/cierra.
- Dashboard: metricas separadas para admin y cliente.
- Shared: seguridad JWT, CORS, excepciones, mensajes i18n y creacion de admin inicial.

## Seguridad y roles
- Publico: `POST /api/auth/login`, `/swagger-ui/**`, `/api-docs/**`, `/actuator/health`.
- Solo ADMIN: `/api/users/**`, `/api/inventory/**`, `/api/payments/**`, `/api/dashboard/admin`, creacion/edicion de cotizaciones y contratos, cambio de estado de tickets, etc.
- Solo CLIENT: `/api/catalog/**`, `/api/my-equipment/**`, `/api/dashboard/client`, creacion de solicitudes y tickets, aceptacion/rechazo de cotizaciones.
- Resto: requiere JWT valido en header `Authorization: Bearer <token>`.

## Configuracion (.env ejemplo)
```
DB_URL=jdbc:mysql://localhost:3306/pcpedia?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
DB_USERNAME=root
DB_PASSWORD=changeme
JWT_SECRET=supersecreto
JWT_EXPIRATION=86400000
ADMIN_EMAIL=admin@pcpedia.com
ADMIN_PASSWORD=Admin123!
PORT=8080
CORS_ORIGINS=http://localhost:3000,http://localhost:4200
```
El `DataInitializer` crea el admin inicial si `ADMIN_EMAIL` y `ADMIN_PASSWORD` estan definidos.

## Ejecucion local
- Requisitos: JDK 21, MySQL en marcha.
- Instalar dependencias: `./mvnw clean install`
- Ejecutar: `./mvnw spring-boot:run` (o `mvnw.cmd` en Windows)
- Tests: `./mvnw test`
- Swagger UI: `http://localhost:${PORT:-8080}/swagger-ui.html`
- Health: `/actuator/health`

## Guia para el frontend (endpoints y flujo)
Base URL por defecto: `http://localhost:8080`.
- Autenticacion: `POST /api/auth/login` con `{email, password}` -> guarda `token` JWT; usa `GET /api/auth/me` para hidratar el usuario; `POST /api/auth/change-password` para cambio.
- Catalogo (CLIENT): listar `GET /api/catalog?search=&category=&page=0&size=10`, detalle `GET /api/catalog/{id}`, categorias `GET /api/catalog/categories`.
- Inventario (ADMIN): crear `POST /api/inventory`, listar paginado `GET /api/inventory?page=0&size=10&search=&category=`, detalle `GET /api/inventory/{id}`, actualizar `PUT /api/inventory/{id}`, eliminar `DELETE /api/inventory/{id}`, cambiar estado `PATCH /api/inventory/{id}/status` (body `{status}`), categorias `GET /api/inventory/categories`.
- Solicitudes (CLIENT): crear `POST /api/requests`, listar propias `GET /api/requests?page=0&size=10`, detalle `GET /api/requests/{id}`; rechazo admin `PATCH /api/requests/{id}/reject` (ADMIN).
- Cotizaciones: admin crea `POST /api/quotes`, actualiza `PUT /api/quotes/{id}`, envia `PATCH /api/quotes/{id}/send`; cliente acepta/rechaza `PATCH /api/quotes/{id}/accept` o `/reject`; listar `GET /api/quotes` y detalle `GET /api/quotes/{id}` (retorna solo las que corresponden segun rol).
- Contratos: admin crea `POST /api/contracts`, cancela `PATCH /api/contracts/{id}/cancel`, renueva `POST /api/contracts/{id}/renew?months=12`; listar `GET /api/contracts?page=0&size=10` y detalle `GET /api/contracts/{id}` (cliente ve solo los suyos). Equipos del cliente: `GET /api/my-equipment`.
- Facturas y pagos (ADMIN): facturas `POST /api/invoices`, `GET /api/invoices`, `GET /api/invoices/{id}`, `PATCH /api/invoices/{id}/cancel`; pagos `POST /api/payments`, `GET /api/payments`.
- Tickets de soporte: cliente crea `POST /api/tickets`, lista `GET /api/tickets?page=0&size=10`, detalle `GET /api/tickets/{id}`; comentarios `POST /api/tickets/{id}/comments`; admin cambia estado `PATCH /api/tickets/{id}/status`.
- Dashboard: admin `GET /api/dashboard/admin`; cliente `GET /api/dashboard/client`.
- Cabeceras comunes: `Authorization: Bearer <token>`, `Content-Type: application/json`; respetar cors con `Origin` de frontend configurada en `CORS_ORIGINS`.

## Endpoints clave (resumen)
- Auth: `POST /api/auth/login`, `GET /api/auth/me`, `POST /api/auth/change-password`, `POST /api/auth/logout`
- Usuarios (ADMIN): `POST/GET/PUT/PATCH /api/users`
- Inventario (ADMIN): `POST/GET/PUT/DELETE /api/inventory`, `PATCH /api/inventory/{id}/status`, `GET /api/inventory/categories`
- Catalogo (CLIENT): `GET /api/catalog`, `GET /api/catalog/{id}`, `GET /api/catalog/categories`
- Solicitudes/Cotizaciones/Contratos: `POST/GET/PATCH /api/requests`, `POST/PUT/PATCH /api/quotes`, `POST/PATCH /api/contracts`
- Equipos del cliente: `GET /api/my-equipment`
- Facturas/Pagos (ADMIN): `POST/GET /api/invoices`, `POST/GET /api/payments`
- Soporte: `POST/GET/PATCH /api/tickets`, `POST /api/tickets/{id}/comments`
- Dashboards: `GET /api/dashboard/admin`, `GET /api/dashboard/client`

## Internacionalizacion
Mensajes en `src/main/resources/i18n/messages_{es,en}.properties`; `LocaleContextHolder` selecciona idioma.

## Estructura de paquetes
`iam/`, `inventory/`, `sales/`, `billing/`, `support/`, `dashboard/`, `shared/` (config, seguridad, excepciones, CQRS, utilidades).

## Notas operativas
- `spring.jpa.hibernate.ddl-auto=update` crea/actualiza el esquema; usar credenciales MySQL con permiso de creacion.
- Ajusta `cors.allowed-origins` via `CORS_ORIGINS` para habilitar frontends.
