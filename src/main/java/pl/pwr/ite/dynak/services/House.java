package pl.pwr.ite.dynak.services;

import interfaces.IHouse;
import lombok.Getter;
import lombok.Setter;
import pl.pwr.ite.dynak.utils.InvalidMethodException;
import pl.pwr.ite.dynak.utils.Method;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class House implements IHouse {
    private final int maxCapacity;
    private int sewageLevel;
    private final int tickSpeed;
    public House(int maxCapacity, int tickSpeed) {
        this.maxCapacity = maxCapacity;
        this.tickSpeed = tickSpeed;
    }
    private void raiseSewageLevel() {
        sewageLevel++;
    }
    @Override
    public int getPumpOut(int max) {
        if (max > sewageLevel) {
            try {
                System.out.println("Sewage is getting pumped out: " + sewageLevel);
                return sewageLevel;
            }
            finally {
                sewageLevel = 0;
            }
        }
        else {
            System.out.println("Sewage is getting pumped out: " + max);
            sewageLevel -= max;
            return max;
        }
    }
    public void sendSewageAlert() {
        System.out.println("Sending sewage alert");
    }
    public void startSimulation()
    {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            raiseSewageLevel();
            System.out.println("Sewage raised to: " + sewageLevel);

            if (sewageLevel >= maxCapacity/10) sendSewageAlert();

        }, 0, tickSpeed, TimeUnit.MILLISECONDS);
    }
    public static void main(String[] args) throws IOException {
        int housePort = 8767;
        String universalHost = "localhost";
        House house = new House(housePort, 100);
        house.startSimulation();
    }
}
