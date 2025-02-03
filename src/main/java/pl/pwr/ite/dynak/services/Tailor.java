package pl.pwr.ite.dynak.services;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Tailor {
    public static void main(String[] args) {
        try {
            int port = 2000;
            Registry registry = LocateRegistry.createRegistry(port);
            System.out.println("Registry created at: " + port);
            while(true){}
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }
}
