package pl.pwr.ite.dynak.services;

import lombok.Getter;
import lombok.Setter;
import pl.pwr.ite.dynak.services.interfaces.ITanker;
import pl.pwr.ite.dynak.utils.InvalidMethodException;
import pl.pwr.ite.dynak.utils.Method;

import java.io.IOException;

@Getter
@Setter
public class Tanker extends SocketUser implements ITanker{
    private final int maxCapacity;
    private int id;
    private int contents;
    private final int officePort;
    private final String officeHost;
    private final int sewagePlantPort;
    private final String sewagePlantHost;
    public Tanker(int maxCapacity, int port, int officePort, String officeHost, int sewagePlantPort, String sewagePlantHost) {
        super(port);
        this.maxCapacity = maxCapacity;
        this.officePort = officePort;
        this.officeHost = officeHost;
        this.sewagePlantPort = sewagePlantPort;
        this.sewagePlantHost = sewagePlantHost;
    }
    public void registerAtOffice() {
        this.id = Integer.parseInt(sendRequest("r:"+ "localhost" + "," + port, officeHost, officePort));
        System.out.println("Registered with id: " + id);
    }
    @Override
    public void setJob(String host, String port) {
        //get house's sewage
        contents += Integer.parseInt(sendRequest("gp:"+maxCapacity,host, Integer.parseInt(port)));
        System.out.println("Amount of sewage pumped from house: " + contents);
        //go to the sewage plant and offload
        sendRequest("spi:"+ id + "," + offloadCargo(), sewagePlantHost, sewagePlantPort);
        System.out.println("Sewage pumped out");
        //tell the office you're done
        sendRequest("sr:"+id, officeHost, officePort);
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
    @Override
    public int handleRequest(Method method) throws InvalidMethodException {
        if (method.methodName().equals("sj")) {
            setJob(method.host(), method.parameter());
            return 0;
        }
        else throw new InvalidMethodException();
    }
    public static void main(String[] args) throws IOException {
        int officePort = 8765;
        int sewagePlantPort = 8766;
        int tankerPort = 8768;
        String universalHost = "localhost";
        Tanker tanker = new Tanker(30, tankerPort, officePort, universalHost, sewagePlantPort, universalHost);
        tanker.registerAtOffice();
        tanker.startListening();
    }
}
