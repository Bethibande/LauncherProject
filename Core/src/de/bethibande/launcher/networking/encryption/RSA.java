package de.bethibande.launcher.networking.encryption;

import lombok.Getter;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class RSA {

    /*
    A class implementing the basic rsa encryption algorithm
    https://en.wikipedia.org/wiki/RSA_(cryptosystem)
     */

    private final BigInteger N;
    private final BigInteger e;
    private final BigInteger d;
    @Getter
    private final int key_size;

    @Getter
    private final PublicKey publicKey;
    @Getter
    private final PrivateKey privateKey;

    public RSA(int key_size) {
        this.key_size = key_size;
        Random r = new Random();
        BigInteger p = BigInteger.probablePrime(key_size, r);
        BigInteger q = BigInteger.probablePrime(key_size, r);
        N = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        e = BigInteger.probablePrime(key_size / 2, r);
        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0)
        {
            e.add(BigInteger.ONE);
        }
        d = e.modInverse(phi);

        this.publicKey = new PublicKey(e, N);
        this.privateKey = new PrivateKey(d, N);
    }

    public byte[] encrypt(byte[] message) {
        return (new BigInteger(message)).modPow(e, N).toByteArray();
    }

    public byte[] encrypt(byte[] message, PublicKey key) {
        return (new BigInteger(message).modPow(key.getE(), key.getN())).toByteArray();
    }

    public byte[] decrypt(byte[] message) {
        return (new BigInteger(message)).modPow(d, N).toByteArray();
    }

}
