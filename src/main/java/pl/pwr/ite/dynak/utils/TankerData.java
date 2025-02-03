package pl.pwr.ite.dynak.utils;

import interfaces.ITanker;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TankerData {
    private final int id;
    private boolean isReady;
    private ITanker iTanker;
    private int totalSewageDroppedOff = 0;
    private String name;

    public TankerData(int id, boolean isReady, ITanker iTanker, String name) {
        this.id = id;
        this.isReady = isReady;
        this.iTanker = iTanker;
        this.name = name;
    }
    public TankerData(int id, int volume) {
        this.id = id;
        this.totalSewageDroppedOff += volume;
    }
}
