package pl.pwr.ite.dynak.services;

import interfaces.IHouse;
import interfaces.ITanker;
import lombok.Getter;
import lombok.Setter;
import pl.pwr.ite.dynak.utils.InvalidMethodException;
import pl.pwr.ite.dynak.utils.Method;

import java.io.IOException;
import java.rmi.RemoteException;

@Getter
@Setter
public class Tanker implements ITanker {
    private final int maxCapacity;
    private int id;
    private int contents;
    public Tanker(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
    public void registerAtOffice() {
        System.out.println("Registered with id: " + id);
    }
    @Override
    public void setJob(IHouse house) throws RemoteException {
        //get house's sewage
        System.out.println("Amount of sewage pumped from house: " + contents);
        //go to the sewage plant and offload
        System.out.println("Sewage pumped out");
        //tell the office you're done
        System.out.println("Tanker" + id + "is ready");
    }
    public int offloadCargo() {
        try {
            Thread.sleep(100);
            return contents;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {

            contents = 0;
        }
    }
    public static void main(String[] args) throws IOException {
        int officePort = 8765;
        int sewagePlantPort = 8766;
        int tankerPort = 8768;
        String universalHost = "localhost";
        Tanker tanker = new Tanker(30);
        tanker.registerAtOffice();
    }
}
