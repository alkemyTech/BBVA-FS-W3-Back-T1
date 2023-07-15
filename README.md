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
      
15. **userwithTransactions**
    - Email: userwithtransactions@example.com
    - Rol: USER
    - Cuentas:
      - Moneda: USD (Dólares)
      - Moneda: ARS (Pesos)
      - Saldo: Ninguno
      

## Transacciones
   
1. **Deposito en Cuenta USD**
   - Monto: $25,000.00
   - Tipo : DEPOSIT
   - Cuenta : accountUSDUserwithTransactions

2. **Deposito en Cuenta ARS**
   - Monto: $500,000.00
   - Tipo : DEPOSIT
   - Cuenta : accountARSUserwithTransactions

3. **Pago con Cuenta en USD**
   - Monto: $300.00
   - Tipo : PAYMENT
   - Cuenta : accountUSDUserwithTransactions

4. **Pago con Cuenta en ARS**
   - Monto: $16,000.00
   - Tipo : PAYMENT
   - Cuenta : accountARSUserwithTransactions

5. **Envio de dinero al userCuentaEnDolares desde Cuenta en USD**

   - Monto: $900.00
   - Tipo : PAYMENT
   - Cuenta : accountUSDUserwithTransactions
   
   - Monto: $900.00
   - Tipo : INCOME
   - Cuenta : accountUserCuentaEnDolares

6. **Envio de dinero al userCuentaPesos desde Cuenta en ARS**

   - Monto: $60,000.00
   - Tipo : PAYMENT
   - Cuenta : accountARSUserwithTransactions
   
   - Monto: $60,000.00
   - Tipo : INCOME
   - Cuenta : accountUserCuentaEnPesos
      
7. **Deposito en Cuenta ARS**
   - Monto: $50,000.00
   - Tipo : DEPOSIT
   - Cuenta : accountARSUserwithTransactions
      
8. **Pago con Cuenta en ARS**
   - Monto: $44,800.00
   - Tipo : PAYMENT
   - Cuenta : accountARSUserwithTransactions

9. **Ingreso de dinero a la Cuenta en USD que envia el userCuentaEnDolares**

   - Monto: $100.00
   - Tipo : INCOME
   - Cuenta : accountUSDUserwithTransactions
   
   - Monto: $100.00
   - Tipo : PAYMENT
   - Cuenta : accountUserCuentaEnDolares
    
10. **Pago con Cuenta en USD**
    - Monto: $360.00
    - Tipo : PAYMENT
    - Cuenta : accountUSDUserwithTransactions

11. **Ingreso de dinero a la Cuenta en ARS que envia el userCuentaEnPesos**

    - Monto: $2,000.00
    - Tipo : INCOME
    - Cuenta : accountARSUserwithTransactions
   
    - Monto: $2,000.00
    - Tipo : PAYMENT
    - Cuenta : accountUserCuentaEnPesos

12. **Deposito en Cuenta USD**
    - Monto: $2,200.00
    - Tipo : DEPOSIT
    - Cuenta : accountUSDUserwithTransactions

13. **Deposito en Cuenta ARS**
    - Monto: $230,000.00
    - Tipo : DEPOSIT
    - Cuenta : accountARSUserwithTransactions
    
14. **Pago con Cuenta en ARS**
    - Monto: $37,000.00
    - Tipo : PAYMENT
    - Cuenta : accountARSUserwithTransactions
    
15. **Pago con Cuenta en USD**
    - Monto: $550.00
    - Tipo : PAYMENT
    - Cuenta : accountUSDUserwithTransactions

16. **Deposito en Cuenta USD**
    - Monto: $3,400.00
    - Tipo : DEPOSIT
    - Cuenta : accountUSDUserwithTransactions

17. **Envio de dinero al userCuentaEnDolares desde Cuenta en USD**

    - Monto: $420.00
    - Tipo : PAYMENT
    - Cuenta : accountUSDUserwithTransactions
     
    - Monto: $420.00
    - Tipo : INCOME
    - Cuenta : accountUserConCuentaEnDolares
   
18. **Ingreso de dinero a la Cuenta en ARS que envia el userCuentaEnPesos**

    - Monto: $5,000.00
    - Tipo : INCOME
    - Cuenta : accountARSUserwithTransactions
   
    - Monto: $5,000.00
    - Tipo : PAYMENT
    - Cuenta : accountUserCuentaEnPesos
    
19. **Deposito en Cuenta ARS**
    - Monto: $350,000.00
    - Tipo : DEPOSIT
    - Cuenta : accountARSUserwithTransactions
   
20. **Deposito en Cuenta USD**
    - Monto: $3,800.00
    - Tipo : DEPOSIT
    - Cuenta : accountUSDUserwithTransactions
   

## Información Adicional

- Cada usuario está asociado con su respectivo rol (ADMIN o USER).
- El seeder crea cuentas para los usuarios según la moneda y el saldo especificados, si corresponde.
- Los últimos 5 usuarios creados (user0, user1, user2, user3, user4) tienen ambas cuentas (USD y ARS) sin saldo.
- Los últimos 5 administradores creados (admin0, admin1, admin2, admin3, admin4) tienen ambas cuentas (USD y ARS) sin saldo.
- Se crea el usuario userWithTransactions con una cuenta en USD y una cuenta en ARS. El mismo realiza 20 transacciones con distintos montos, de las cuales 8 son deposit, 6 son payment y 6 son sendMoney. Las transacciones sendMoney con la cuenta en ARS se hacen con el usuario userCuentaEnPesos y las que son con la cuenta en USD se hacen con el usuario userCuentaEnDolares. 


Por favor, asegúrate de actualizar las configuraciones necesarias, como el archivo `application.properties`, antes de ejecutar el seeder de la base de datos. Además, ten en cuenta que al ejecutar el seeder, se eliminarán físicamente las tablas de usuarios,cuentas y transacciones.
