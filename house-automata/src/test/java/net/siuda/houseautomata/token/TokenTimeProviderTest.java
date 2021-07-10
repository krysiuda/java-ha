package net.siuda.houseautomata.token;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TokenTimeProviderTest {

    @Test
    public void testTokenTimeZero() {
        TokenTimeProvider.TokenTime tokenTime = new TokenTimeProvider.TokenTime();
        tokenTime.setTime(0L);
        Assertions.assertArrayEquals(new byte [] { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, tokenTime.getBytes());
    }

    @Test
    public void testTokenTimeOne() {
        TokenTimeProvider.TokenTime tokenTime = new TokenTimeProvider.TokenTime();
        tokenTime.setTime(1L);
        Assertions.assertArrayEquals(new byte [] { 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, tokenTime.getBytes());
    }

    @Test
    public void testTokenTimeOneK() {
        TokenTimeProvider.TokenTime tokenTime = new TokenTimeProvider.TokenTime();
        tokenTime.setTime(1024L);
        Assertions.assertArrayEquals(new byte [] { 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, tokenTime.getBytes());
    }

    @Test
    public void testTokenTimeOneKOne() {
        TokenTimeProvider.TokenTime tokenTime = new TokenTimeProvider.TokenTime();
        tokenTime.setTime(1025L);
        Assertions.assertArrayEquals(new byte [] { 0x01, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 }, tokenTime.getBytes());
    }
}