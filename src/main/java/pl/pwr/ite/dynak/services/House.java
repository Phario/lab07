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
public class House extends SocketUser implements IHouse {
    private final int maxCapacity;
    private int sewageLevel;
    private final int officePort;
    private final String officeHost;
    private final int tickSpeed;
    public House(int port, int maxCapacity, int officePort, String officeHost, int tickSpeed) {
        super(port);
        this.maxCapacity = maxCapacity;
        this.officePort = officePort;
        this.officeHost = officeHost;
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
    @Override
    public int handleRequest(Method method) throws InvalidMethodException {
        if (method.methodName().equals("gp")) {
            return getPumpOut(Integer.parseInt(method.parameter()));
        }
        else throw new InvalidMethodException();
    }
    public void sendSewageAlert() {
        sendRequest("o:" + "localhost" + "," + port,officeHost,officePort);
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
        int officePort = 8765;
        int housePort = 8767;
        String universalHost = "localhost";
        House house = new House(housePort, 30, officePort, universalHost, 100);
        house.startSimulation();
        house.startListening();
    }
}
