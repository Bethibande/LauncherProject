package de.bethibande.launcher.networking.encryption;

import lombok.Getter;

import java.math.BigInteger;

public class PrivateKey {

    @Getter
    private final BigInteger d;
    @Getter
    private final BigInteger N;

    public PrivateKey(BigInteger d, BigInteger N) {
        this.d = d;
        this.N = N;
    }

}
