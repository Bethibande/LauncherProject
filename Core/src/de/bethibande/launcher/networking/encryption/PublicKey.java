package de.bethibande.launcher.networking.encryption;

import lombok.Getter;

import java.math.BigInteger;

public class PublicKey {

    @Getter
    private final BigInteger e;
    @Getter
    private final BigInteger N;

    public PublicKey(BigInteger e, BigInteger N) {
        this.e = e;
        this.N = N;
    }

}
