# Seeder de la Base de Datos

Este README proporciona información sobre los usuarios y administradores creados por el seeder de la base de datos. Ten en cuenta que para ejecutar el seeder, debes configurar la variable `develop.seeder` en el archivo `application.properties` a `true`.

## Usuarios

Todos los usuarios Y administradores tienen la contraseña establecida como `123`.

### Usuarios Administradores

1. **adminSinCuentas**
   - Email: adminSinCuenta@example.com
   - Rol: ADMIN
   - Cuentas: Ninguna

2. **adminCuentaEnPesos**
   - Email: adminCuentaEnPesos@example.com
   - Rol: ADMIN
   - Cuentas:
     - Moneda: ARS (Pesos)

3. **adminCuentaEnDolares**
   - Email: adminCuentaEnDolares@example.com
   - Rol: ADMIN
   - Cuentas:
     - Moneda: USD (Dólares)

4. **adminPesosBalance100mil**
   - Email: adminPesosBalance100mil@example.com
   - Rol: ADMIN
   - Cuentas:
     - Moneda: USD (Dólares)
     - Saldo: $100,000.00

5. **adminDolares10mil**
   - Email: adminDolares10mil@example.com
   - Rol: ADMIN
   - Cuentas:
     - Moneda: USD (Dólares)
     - Saldo: $10,000.00

6. **admin0 to admin4**
   - Email: admin0@example.com - admin4@example.com
   - Rol: ADMIN
   - Cuentas:
     - Moneda: USD (Dólares)
     - Moneda: ARS (Pesos)

### Usuarios Regulares

9. **userSinCuentas**
   - Email: userSinCuenta@example.com
   - Rol: USER
   - Cuentas: Ninguna

10. **userCuentaEnPesos**
    - Email: userCuentaEnPesos@example.com
    - Rol: USER
    - Cuentas:
      - Moneda: ARS (Pesos)

11. **userCuentaEnDolares**
    - Email: userCuentaEnDolares@example.com
    - Rol: USER
    - Cuentas:
      - Moneda: USD (Dólares)

12. **userPesosBalance100mil**
    - Email: userPesosBalance100mil@example.com
    - Rol: USER
    - Cuentas:
      - Moneda: USD (Dólares)
      - Saldo: $100,000.00

13. **userDolares10mil**
    - Email: userDolares10mil@example.com
    - Rol: USER
    - Cuentas:
      - Moneda: USD (Dólares)
      - Saldo: $10,000.00

14. **user0 to user4**
    - Email: user0@example.com - user4@example.com
    - Rol: USER
    - Cuentas:
      - Moneda: USD (Dólares)
      - Moneda: ARS (Pesos)
      - Saldo: Ninguno



## Información Adicional

- Cada usuario está asociado con su respectivo rol (ADMIN o USER).
- El seeder crea cuentas para los usuarios según la moneda y el saldo especificados, si corresponde.
- Los últimos 5 usuarios creados (user0, user1, user2, user3, user4) tienen ambas cuentas (USD y ARS) sin saldo.
- Los últimos 5 administradores creados (admin0, admin1, admin2, admin3, admin4) tienen ambas cuentas (USD y ARS) sin saldo.

Por favor, asegúrate de actualizar las configuraciones necesarias, como el archivo `application.properties`, antes de ejecutar el seeder de la base de datos. Además, ten en cuenta que al ejecutar el seeder, se eliminarán físicamente las tablas de usuarios y cuentas.