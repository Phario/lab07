package pl.pwr.ite.dynak.services;

import interfaces.IHouse;
import interfaces.IOffice;
import interfaces.ITanker;
import lombok.Getter;
import lombok.Setter;
import pl.pwr.ite.dynak.utils.InvalidMethodException;
import pl.pwr.ite.dynak.utils.Method;
import pl.pwr.ite.dynak.utils.TankerData;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

@Getter
@Setter
public class Office implements IOffice {
    private static int staticTankerId = 1;
    private ArrayList<TankerData> tankers;
    public Office() {
        tankers = new ArrayList<>();
    }
    @Override
    public int register(ITanker r, String name) throws RemoteException {
        tankers.add(new TankerData(staticTankerId,true, r, name));
        System.out.println("Tanker registered with id: " + staticTankerId);
        return staticTankerId++;
    }

    @Override
    public int order(IHouse house, String name) throws RemoteException {
        //find available tanker
        for (TankerData tanker : tankers) {
            //send tanker on a job if it's ready
            if (tanker.isReady()) {
                //set its state to not ready
                tanker.setReady(false);
                tanker.getITanker().setJob(house);
                //return 1 if successful
                System.out.println("Tanker " + tanker.getId() + " dispatched to sewage collection");
                return 1;
            }
        }
        return 0;
    }
    @Override
    public void setReadyToServe(int number) throws RemoteException {
        for (TankerData tanker : tankers) {
            if (tanker.getId() == number) {
                tanker.setReady(true);
                System.out.println("Tanker " + number + " is ready");
                break;
            }
        }
    }
    public void sendGetStatusRequest(int number) {//TODO finish this method
        System.out.println("Total sewage dropped off by tanker " + number + ": ");
    }
    public static void main(String[] args) throws IOException {
        int registryPort = 2000;
        int officePort = 8882;
        String universalHost = "localhost";
        Office office = new Office();
        IOffice io = (IOffice) UnicastRemoteObject.exportObject(office, registryPort);
        Registry registry = LocateRegistry.getRegistry(universalHost, officePort);
        registry.rebind("Office", io);
    }
}
