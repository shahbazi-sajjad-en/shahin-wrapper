package biz.espc.shahin.util;

import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

@Component
public class ImportCA {

    public KeyStore loadPkcs12(String p12Path, String password) {
        if (p12Path == null || p12Path.isBlank()) {
            throw new IllegalArgumentException("p12Path must not be null or blank");
        }

        try (InputStream inputStream = new FileInputStream(p12Path)) {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            char[] pwd = password == null ? new char[0] : password.toCharArray();
            keyStore.load(inputStream, pwd);
            return keyStore;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load PKCS#12 file from path: " + p12Path, e);
        }
    }

    public String findFirstPrivateKeyAlias(KeyStore keyStore) {
        try {
            Enumeration<String> aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                if (keyStore.isKeyEntry(alias)) {
                    return alias;
                }
            }
            throw new IllegalStateException("No private key alias found in PKCS#12 keystore");
        } catch (Exception e) {
            throw new IllegalStateException("Failed to inspect PKCS#12 keystore aliases", e);
        }
    }

    public PrivateKey loadPrivateKey(String p12Path, String password) {
        try {
            KeyStore keyStore = loadPkcs12(p12Path, password);
            String alias = findFirstPrivateKeyAlias(keyStore);
            return loadPrivateKey(keyStore, alias, password);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load private key from PKCS#12: " + p12Path, e);
        }
    }

    public PrivateKey loadPrivateKey(KeyStore keyStore, String alias, String password) {
        if (alias == null || alias.isBlank()) {
            throw new IllegalArgumentException("alias must not be null or blank");
        }

        try {
            char[] pwd = password == null ? new char[0] : password.toCharArray();
            Key key = keyStore.getKey(alias, pwd);
            if (key == null) {
                throw new IllegalStateException("No key found for alias: " + alias);
            }
            if (!(key instanceof PrivateKey)) {
                throw new IllegalStateException("Key for alias is not a PrivateKey: " + alias);
            }
            return (PrivateKey) key;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load private key for alias: " + alias, e);
        }
    }

    public X509Certificate loadCertificate(String p12Path, String password) {
        try {
            KeyStore keyStore = loadPkcs12(p12Path, password);
            String alias = findFirstPrivateKeyAlias(keyStore);
            return loadCertificate(keyStore, alias);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load certificate from PKCS#12: " + p12Path, e);
        }
    }

    public X509Certificate loadCertificate(KeyStore keyStore, String alias) {
        try {
            Certificate certificate = keyStore.getCertificate(alias);
            if (certificate == null) {
                throw new IllegalStateException("No certificate found for alias: " + alias);
            }
            if (!(certificate instanceof X509Certificate)) {
                throw new IllegalStateException("Certificate is not X509Certificate for alias: " + alias);
            }
            return (X509Certificate) certificate;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load certificate for alias: " + alias, e);
        }
    }

    public Certificate[] loadCertificateChain(String p12Path, String password) {
        try {
            KeyStore keyStore = loadPkcs12(p12Path, password);
            String alias = findFirstPrivateKeyAlias(keyStore);
            Certificate[] chain = keyStore.getCertificateChain(alias);
            if (chain == null || chain.length == 0) {
                throw new IllegalStateException("No certificate chain found for alias: " + alias);
            }
            return chain;
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load certificate chain from PKCS#12: " + p12Path, e);
        }
    }
}
