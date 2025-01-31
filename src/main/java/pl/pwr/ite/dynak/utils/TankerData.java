package pl.pwr.ite.dynak.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TankerData {
    private final String tankerPort;
    private final String tankerHost;
    private final int id;
    private boolean isReady;
    private int totalSewageDroppedOff = 0;
    public TankerData(int id, String tankerPort, String tankerHost, boolean isReady) {
        this.tankerPort = tankerPort;
        this.tankerHost = tankerHost;
        this.id = id;
        this.isReady = isReady;
    }
    public TankerData(int id, int volume) {
        this.tankerPort = null;
        this.tankerHost = null;
        this.id = id;
        this.totalSewageDroppedOff += volume;
    }
}
