


# Е-систем за банкарство со автентикација со X.509 сертификати и контрола на пристап


# Tech Stack
Проектот е изработен во **Spring Boot**, со **Spring Security 6** и **Spring Data JPA** со **PostgreSQL** датабаза. Корисничкиот интерфејс е изработен со **Thymeleaf** со додаден **Bootstrap 5**.

# За апликацијата
Изработена е со стандардна MVC слоевита архитектура. 
 
### Репозиториуми и Сервиси
Секој од моделите има соодветен JpaRepository интерфејс, како и сервисна логика потребна за CRUD операции. 

Од особено значење е **UserServiceImpl** кој не само што имплементира **UserService**, туку и **UserDetailsService**, кој е потребен за Security да може да презема информации за корисник од база.
# Взаемна автентикација меѓу сервер и клиент
## SSL конфигурација на сервер 
Користев **OpenSSL** за генерирање на сертификатите.
следуваат чекорите за генерирање на сертификати
1. генерирање на приватен клуч: ```openssl genrsa -des3 -out domain.key 2048```
2. креирање на CSR (Certificate Signing Request): ```openssl req -key domain.key -new -out domain.csr```
3. генерирање на само-потпишан(self-signed) сертификат: ```openssl x509 -signkey domain.key -in domain.csr -req -days 365 -out domain.crt```
4. креирање на само-потпишан Root CA>: ```openssl req -x509 -sha256 -days 1825 -newkey rsa:2048 -keyout rootCA.key -out rootCA.crt```
5. конфигурациска датотека за потпишување ```domain.ext``` со содржина:  

```authorityKeyIdentifier=keyid,issuer
basicConstraints=CA:FALSE
subjectAltName = @alt_names
[alt_names]
DNS.1 = domain
```
6. потпишување на CSR со ново-креираниот root CA: ```openssl x509 -req -CA rootCA.crt -CAkey rootCA.key -in domain.csr -out domain.crt -days 365 -CAcreateserial -extfile domain.ext```
7. конвертирање во PKCS12 формат за импортирање на серверот: ```openssl pkcs12 -inkey domain.key -in domain.crt -export -out keystore.p12```
8. импортирање на ```keystore.p12``` во Spring и соодветна конфигурација во ```application.properties``` : 
```

> бидејќи користиме само-потпишан root CA, **потребно е истиот да се импортира во browser** за да биде препознаен како валиден сертификат. Ова не е потребно како чекор доколку користевме сертификат потпишан од познат CA

## Конфигурација на клиентска автентикација со X.509 сертификат
за генерирање на клиентските сертификати, во проектот имам креирано bash скрипта која ги извршува конандите за генерирање и потпишување на CSR, како и негово експортирање во p12 формат. Скриптата како аргумент прима име на корисникот, кое ќе го користи како име на датотеките кои се генерираат. Содржината е следна:
```
#!/bin/bash

if [[ $# -eq 0 ]]; then
  echo "Usage: $0 <name>"
  exit 1
fi

name=$1

# Generate CSR
openssl req -new -newkey rsa:4096 -nodes -keyout "${name}.key" -out "${name}.csr"

# Sign CSR with root CA and create certificate
openssl x509 -req -CA rootCA.crt -CAkey rootCA.key -in "${name}.csr" -out "${name}.crt" -days 365 -CAcreateserial

# Create PKCS#12 file containing private key and certificate
openssl pkcs12 -export -out "${name}.p12" -name "${name}" -inkey "${name}.key" -in "${name}.crt"
```

Како CN (Common Name) во prompt на командите се пишува ЕМБГ на корисникот и **МОРА** да се совпаѓа со вредноста зачувана во база.

### Keystore
1. пакување на приватниот клуч на серверот со сертификатот во p12 фајл :
```openssl pkcs12 -export -in domain.crt -inkey privateKey.key -out user1.p12```
2. конвертирање во ЈКS датотека: ```keytool -importkeystore -srckeystore user1.p12 -srcstoretype PKCS12 -destkeystore keystore.jks -deststoretype JKS```
3. импортирање на keystore во ```resources``` и додавање во ```application.properties```:
4. 
```
### Spring Security конфигурација

```package com.kambovski.ibproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Spring Security Configuration Beans.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests((auth) -> {
                    auth
                            // Само ADMIN има пристап до account management рутите
                            .requestMatchers("/account/**").hasRole("ADMIN")
                            // Само CUSTOMER има пристап до transaction рутите
                            .requestMatchers("/transaction/**").hasRole("CUSTOMER")
                            // Овозможуваме пристап до овие руте без автентикација
                            .requestMatchers("/", "/login", "/register", "/user/create","/account/details", "/account/withdraw", "/account/deposit").permitAll()
                            // Сите други бараат автентикација
                            .anyRequest().authenticated();
                })
                // X.509 автентикација со сертификати
                .x509()
                .subjectPrincipalRegex("CN=(.*?)(?:,|$)")  // Извлекување на корисничкото име (EMBG) од сертификатот
                .userDetailsService(userDetailsService)  // Користење на UserDetailsService за вчитување на корисникот од базата
                .and()
                .logout()
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/login");  // Пренасочување кон најава по одјавување

        return http.build();
    }
}

```
.
овде Spring Security е конфигурирано да презема x509 сертификат и да го извлече CN преку regex (имплементирано со филтер во позадина), како и конфигурирано да ограничува пристап според патеки


Дополнително, сите корисници мора да се автентицирани за пристап, т.е. да имаат сертификат.

# Дополнителни безбедносни ограничувања
Бидејќи некои од погледите се споделени од различни контролери, со тоа и различни видови на корисници, некои елементи во самите Thymeleaf templates се ограничени да рендерират само при присуство на одреден role.

