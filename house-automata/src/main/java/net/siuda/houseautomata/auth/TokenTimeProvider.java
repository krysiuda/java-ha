package net.siuda.houseautomata.auth;

import org.springframework.stereotype.Service;

@Service
public class TokenTimeProvider {

    public static final long MILLIS_IN_SLOT = 1000L * 15L;

    public TokenTime getCurrentTimeSlot() {
        TokenTime tokenTime = new TokenTime();
        tokenTime.setTime(System.currentTimeMillis() / MILLIS_IN_SLOT);
        return tokenTime;
    }

    public TokenTime getPrevTimeSlot(TokenTime timeSlot) {
        TokenTime tokenTime = new TokenTime();
        tokenTime.setTime(timeSlot.getTime() - 1L);
        return tokenTime;
    }

    public static class TokenTime {
        private long time;

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public byte[] getBytes() {
            byte[] result = new byte[Long.BYTES];
            long value = time;
            for(int c = 0; value != 0L; value >>= Byte.SIZE) {
                result[c++] = (byte)(value & 0xFFL);
            }
            return result;
        }
    }

}
