package com.pay.library.tool.store;

import android.text.TextUtils;

import java.security.GeneralSecurityException;


/**
 * Provides AES algorithm
 *
 * @author Orhan Obut
 */
final class AesEncryption implements Encryption {

    //never ever change this value since it will break backward compatibility in terms of keeping previous data
    private static final String KEY_STORAGE_SALT = "asdf3242klj";
    private static final String KEY_GENERATED_SECRET_KEYS = "adsfjlkj234234dasfgenasdfas";

    private final String password;
    private final Storage storage;
    private final Encoder encoder;

    private AesCbcWithIntegrity.SecretKeys key;
    private String saltKey;

    AesEncryption(Storage storage, Encoder encoder, String password) {
        this.storage = storage;
        this.encoder = encoder;
        this.password = password;
    }

    @Override
    public boolean init() {
        this.saltKey = storage.get(KEY_STORAGE_SALT);

        try {
            generateSecretKey(password);
            return true;
        } catch (GeneralSecurityException e) {
            Logger.w("Encryption is not supported in this device");
            return false;
        }
    }

    @Override
    public String encrypt(byte[] value) {
        if (value == null) {
            return null;
        }
        String result = null;
        try {
            AesCbcWithIntegrity.CipherTextIvMac civ = AesCbcWithIntegrity.encrypt(value, key);
            result = civ.toString();
        } catch (GeneralSecurityException e) {
            Logger.d(e.getMessage());
        }

        return result;
    }

    @Override
    public byte[] decrypt(String value) {
        if (value == null) {
            return null;
        }
        byte[] result = null;

        try {
            AesCbcWithIntegrity.CipherTextIvMac civ = getCipherTextIvMac(value);
            result = AesCbcWithIntegrity.decrypt(civ, key);
        } catch (GeneralSecurityException e) {
            Logger.d(e.getMessage());
        }

        return result;
    }

    @Override
    public boolean reset() {
        return storage.clear();
    }

    private AesCbcWithIntegrity.CipherTextIvMac getCipherTextIvMac(String cipherText) {
        return new AesCbcWithIntegrity.CipherTextIvMac(cipherText);
    }

    /**
     * Gets the secret key by using salt and password. Salt is stored in the storage
     * If the salt is not stored, that means it is first time and it creates the salt and
     * save it in the storage
     * <p/>
     * Some phones especially Samsung(I hate Samsung) do not support every algorithm. If it is not
     * supported, it will fall generate the key without password and store it.
     */
    private void generateSecretKey(String password) throws GeneralSecurityException {
        if (password == null || storage.contains(KEY_GENERATED_SECRET_KEYS)) {
            key = getSecretKeysWithoutPassword();
            Logger.w("key is generated without password");
            return;
        }

        key = generateSecretKeyFromPassword(password);
        if (key == null) {
            key = AesCbcWithIntegrity.generateKey();
            storage.put(KEY_GENERATED_SECRET_KEYS, encoder.encode(key));
        }
        Logger.w("key is generated from password");
    }

    private AesCbcWithIntegrity.SecretKeys getSecretKeysWithoutPassword() {
        try {
            AesCbcWithIntegrity.SecretKeys key = null;
            String keys = storage.get(KEY_GENERATED_SECRET_KEYS);
            if (keys != null) {
                key = encoder.decodeSerializable(keys);
            }
            if (key == null) {
                key = AesCbcWithIntegrity.generateKey();
                storage.put(KEY_GENERATED_SECRET_KEYS, encoder.encode(key));
            }
            return key;
        } catch (GeneralSecurityException e) {
            Logger.e(e.getMessage());
            return null;
        } catch (Exception e) {
            Logger.e(e.getMessage());
            return null;
        }
    }

    private AesCbcWithIntegrity.SecretKeys generateSecretKeyFromPassword(String password) {
        try {
            if (TextUtils.isEmpty(saltKey)) {
                saltKey = AesCbcWithIntegrity.saltString(AesCbcWithIntegrity.generateSalt());
                storage.put(KEY_STORAGE_SALT, saltKey);
            }
            return AesCbcWithIntegrity.generateKeyFromPassword(password, saltKey);
        } catch (GeneralSecurityException e) {
            Logger.e(e.getMessage());
            return null;
        }
    }

}