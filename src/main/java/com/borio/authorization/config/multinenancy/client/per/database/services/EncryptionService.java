package com.borio.authorization.config.multinenancy.client.per.database.services;

public interface EncryptionService {

    String encrypt(String strToEncrypt, String secret, String salt);
    String decrypt(String strToDecrypt, String secret, String salt);

}
