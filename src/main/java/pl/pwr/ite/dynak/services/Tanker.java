package pl.pwr.ite.dynak.services;

import interfaces.IHouse;
import interfaces.IOffice;
import interfaces.ISewagePlant;
import interfaces.ITanker;
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

@Getter
@Setter
public class Tanker implements ITanker {
    private final int maxCapacity;
    private int id;
    private int contents;
    private final ISewagePlant iSewagePlant;
    private final IOffice iOffice;
    private final String name;
    public Tanker(int maxCapacity, ISewagePlant iSewagePlant, IOffice iOffice, String name) {
        this.maxCapacity = maxCapacity;
        this.iSewagePlant = iSewagePlant;
        this.iOffice = iOffice;
        this.name = name;
    }
    public void registerAtOffice(ITanker iTanker, String name) {
        try {
            iOffice.register(iTanker, name);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Registered with id: " + id);
    }
    @Override
    public void setJob(IHouse house) throws RemoteException {
        //get house's sewage
        house.getPumpOut(maxCapacity);
        System.out.println("Amount of sewage pumped from house: " + contents);
        //go to the sewage plant and offload
        iSewagePlant.setPumpIn(id, offloadCargo());
        System.out.println("Sewage pumped out");
        //tell the office you're done
        iOffice.setReadyToServe(id);
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
    public static void main(String[] args) {
        int registryPort = 2000;
        int tankerPort = 8884;
        String universalHost = "localhost";
        String name = "Tanker";
        try {
            Registry registry = LocateRegistry.getRegistry(2000);
            IOffice iOffice = (IOffice) registry.lookup("Office");
            ISewagePlant iSewagePlant = (ISewagePlant) registry.lookup("SewagePlant");
            Tanker tanker = new Tanker(30, iSewagePlant, iOffice, "Tanker");
            ITanker iTanker = (ITanker) UnicastRemoteObject.exportObject(tanker, registryPort);
            registry = LocateRegistry.getRegistry(universalHost, tankerPort);
            registry.rebind("Tanker", iTanker);
            tanker.registerAtOffice(iTanker, name);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}
