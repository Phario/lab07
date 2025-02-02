package pl.pwr.ite.dynak.services;

import interfaces.ISewagePlant;
import lombok.Getter;
import lombok.Setter;
import pl.pwr.ite.dynak.utils.InvalidMethodException;
import pl.pwr.ite.dynak.utils.Method;
import pl.pwr.ite.dynak.utils.TankerData;

import java.io.IOException;
import java.util.ArrayList;

@Getter
@Setter
public class SewagePlant implements ISewagePlant {
    ArrayList<TankerData> tankerData = new ArrayList<>();
    @Override
    public void setPumpIn(int number, int volume) {
        updateTankerData(number, volume);
        System.out.println("Tanker " + number + " pumped in " + volume);
    }
    private synchronized void updateTankerData(int number, int volume) {
        for (TankerData tankersData : tankerData) {
            if (tankersData.getId() == number) {
                tankersData.setTotalSewageDroppedOff(tankersData.getTotalSewageDroppedOff() + volume);
                return;
            }
        }
        tankerData.add(new TankerData(number, volume));
    }
    @Override
    public int getStatus(int number) {
        for (TankerData tankersData : tankerData) {
            if (tankersData.getId() == number) {
                System.out.println("Getting status for tanker " + number);
                try {
                    return tankersData.getTotalSewageDroppedOff();
                }
                finally {
                    tankersData.setTotalSewageDroppedOff(0);
                }
            }
        }
        return 0;
    }

    @Override
    public void setPayoff(int number) {
        System.out.println("Setting payoff for tanker " + number);
        updateTankerData(number, 0);
    }
    public static void main(String[] args) throws IOException {
        int sewagePlantPort = 8766;
        SewagePlant sewagePlant = new SewagePlant();
    }
}
