package pl.pwr.ite.dynak.services;

import interfaces.IHouse;
import interfaces.IOffice;
import lombok.Getter;
import lombok.Setter;
import pl.pwr.ite.dynak.utils.InvalidMethodException;
import pl.pwr.ite.dynak.utils.Method;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class House implements IHouse {
    private final int maxCapacity;
    private int sewageLevel;
    private final int tickSpeed;
    private final IOffice iOffice;
    private IHouse iHouse;
    private final String name;
    public House(IOffice iOffice, int maxCapacity, int tickSpeed, String name) {
        this.iOffice = iOffice;
        this.maxCapacity = maxCapacity;
        this.tickSpeed = tickSpeed;
        this.name = name;
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
    public void sendSewageAlert() throws RemoteException {
        iOffice.order(iHouse, name);
        System.out.println("Sending sewage alert");
    }
    public void startSimulation()
    {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            raiseSewageLevel();
            System.out.println("Sewage raised to: " + sewageLevel);

            if (sewageLevel >= maxCapacity/10) {
                try {
                    sendSewageAlert();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

        }, 0, tickSpeed, TimeUnit.MILLISECONDS);
    }
    public static void main(String[] args) throws IOException {
        int registryPort = 2000;
        int housePort = 8880;
        String universalHost = "localhost";
        try {
            Registry registry = LocateRegistry.getRegistry(universalHost, registryPort);
            IOffice iOffice = (IOffice) registry.lookup("Office");
            House house = new House(iOffice,30, 800, "House");
            IHouse ih = (IHouse) UnicastRemoteObject.exportObject(house, housePort);
            house.setIHouse(ih);
            registry.rebind("House", ih);
            house.startSimulation();
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}
