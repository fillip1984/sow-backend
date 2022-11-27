package org.home.knowledge.sow.security;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * RsaKeyProperties record adds rsa. properties hints in application.properties
 * and also allows for us to inject said properties as record
 */
@ConfigurationProperties(prefix = "rsa")
public record RsaKeyProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
}