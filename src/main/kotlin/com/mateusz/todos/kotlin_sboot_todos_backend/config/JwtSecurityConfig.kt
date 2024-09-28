package com.mateusz.todos.kotlin_sboot_todos_backend.config

import com.mateusz.todos.kotlin_sboot_todos_backend.services.JpaUserDetailsService
import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.jwk.JWKSelector
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.provisioning.JdbcUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.handler.HandlerMappingIntrospector
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*
import javax.sql.DataSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class JwtSecurityConfig(private val jpaUserDetailsService: JpaUserDetailsService) {

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(
        httpSecurity: HttpSecurity,
        introspector: HandlerMappingIntrospector?
    ): SecurityFilterChain {
        return httpSecurity
            .authorizeHttpRequests { auth: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry ->
                auth
                    .requestMatchers(
                        "/swagger-ui/**", "/v3/api-docs/**",
                        "/v1/login", "/v1/signup", "/explorer/**"
                    )
                    .permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }
            .userDetailsService(jpaUserDetailsService)
            .csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
            .sessionManagement { session: SessionManagementConfigurer<HttpSecurity?> ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            }
            .oauth2ResourceServer { oauth2: OAuth2ResourceServerConfigurer<HttpSecurity?> ->
                oauth2
                    .jwt(Customizer.withDefaults())
            }
            .httpBasic(
                Customizer.withDefaults()
            )
            .build()
    }

    @Bean
    fun authenticationManager(
        userDetailsService: UserDetailsService?
    ): AuthenticationManager {
        val authenticationProvider = DaoAuthenticationProvider()
        authenticationProvider.setUserDetailsService(userDetailsService)
        return ProviderManager(authenticationProvider)
    }

    @Bean
    @ConfigurationProperties("spring.datasource")
    fun dataSource(properties: DataSourceProperties): HikariDataSource {
        return properties.initializeDataSourceBuilder().type(HikariDataSource::class.java).build()
    }

    @Bean
    @Qualifier("jdbcUserService")  // <-- this references the bean id
    fun userDetailsService(dataSource: DataSource?): UserDetailsService {
        return JdbcUserDetailsManager(dataSource)
    }

    @Bean
    fun jwkSource(rsaKey: RSAKey): JWKSource<SecurityContext> {
        val jwkSet = JWKSet(rsaKey)
        return JWKSource { jwkSelector: JWKSelector, _: SecurityContext? -> jwkSelector.select(jwkSet) }
    }

    @Bean
    fun jwtEncoder(jwkSource: JWKSource<SecurityContext?>): JwtEncoder {
        return NimbusJwtEncoder(jwkSource)
    }

    @Bean
    @Throws(JOSEException::class)
    fun jwtDecoder(rsaKey: RSAKey): JwtDecoder {
        return NimbusJwtDecoder
            .withPublicKey(rsaKey.toRSAPublicKey())
            .build()
    }

    @Bean
    fun rsaKey(keyPair: KeyPair): RSAKey {
        return RSAKey.Builder(keyPair.public as RSAPublicKey) // typecasting
            .privateKey(keyPair.private as RSAPrivateKey) // typecasting
            .keyID(UUID.randomUUID().toString())
            .build()
    }

    @Bean
    fun keyPair(): KeyPair {
        try {
            val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
            keyPairGenerator.initialize(2048)
            return keyPairGenerator.generateKeyPair()
        } catch (ex: Exception) {
            throw RuntimeException(ex) // convert to unchecked ex
        }
    }
}