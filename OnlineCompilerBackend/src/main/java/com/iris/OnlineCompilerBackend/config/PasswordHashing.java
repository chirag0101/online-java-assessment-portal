package com.iris.OnlineCompilerBackend.config;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * PBKDF2 salted password hashing.
 */
public class PasswordHashing {

    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
    public static final int SALT_BYTES = 64;
    public static final int HASH_BYTES = 512;
    public static final int PBKDF2_ITERATIONS = 1666;

    /**
     * generates a secure random salt value.
     */
    public static String getSalt() {
        SecureRandom random = new SecureRandom();
        byte salt[] = new byte[SALT_BYTES];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Returns a salted PBKDF2 hash of the password.
     *
     * @param password        the password to hash
     * @param isDoubleHashReq
     * @return a salted PBKDF2 hash of the password
     */
    public static String createHash(String password, String salt, Boolean isDoubleHashReq) throws Exception {
        if (password.isEmpty() || salt.isEmpty()) {
            throw new IllegalArgumentException("password or salt is null/empty");
        }
        try {
            String pwdHash = null;
            if (isDoubleHashReq) {
                pwdHash = getSHA256HashValue(password);
            } else {
                pwdHash = password;
            }
            return getPBEHashValue(pwdHash, salt);
        } catch (Exception e) {
            throw e;
        }
    }

    public static String getSHA256HashValue(String value) throws NoSuchAlgorithmException {
        return SHAHashingUtil.hashUsingSHA256(value);
    }

    public static String getPBEHashValue(String value, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(value.toCharArray(), Base64.getDecoder().decode(salt), PBKDF2_ITERATIONS, HASH_BYTES);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        return Base64.getEncoder().encodeToString(factory.generateSecret(spec).getEncoded());
    }

    /**
     * Validates a password using a hash
     *
     * @param salt         the value used when the password is hashed earlier.
     * @param hashPassword the hash of the valid password.
     * @return true if the password is correct, false if not
     * @throws Exception
     */
    public static boolean validatePassword(String salt, String hashPassword, String dbHashPwd) throws Exception {
        try {
            return dbHashPwd.equals(hashPassword);
        } catch (Exception e) {
            throw e;
        }
    }

}