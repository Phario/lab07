package pl.pwr.ite.dynak.services;

import interfaces.IOffice;
import lombok.Getter;
import lombok.Setter;
import pl.pwr.ite.dynak.utils.InvalidMethodException;
import pl.pwr.ite.dynak.utils.Method;
import pl.pwr.ite.dynak.utils.TankerData;

import java.io.IOException;
import java.util.ArrayList;

@Getter
@Setter
public class Office extends SocketUser implements IOffice {
    private static int staticTankerId = 1;
    private ArrayList<TankerData> tankers;
    private final int sewagePlantPort;
    private final String sewagePlantHost;
    public Office(int port, int sewagePlantPort, String sewagePlantHost) {
        super(port);
        tankers = new ArrayList<>();
        this.sewagePlantPort = sewagePlantPort;
        this.sewagePlantHost = sewagePlantHost;
    }

    @Override
    public int handleRequest(Method method) {
        return switch (method.methodName()) {
            case "r" -> register(method.host(), method.parameter());
            case "o" -> order(method.host(), method.parameter());
            case "sr" -> {
                setReadyToServe(Integer.parseInt(method.parameter()));
                yield 0;
            }
            default -> throw new InvalidMethodException();
        };
    }
    @Override
    public int register(String host, String port) {
        tankers.add(new TankerData(staticTankerId, port, host, true));
        System.out.println("Tanker registered with id: " + staticTankerId);
        return staticTankerId++;
    }

    @Override
    public int order(String host, String port) {
        //find available tanker
        for (TankerData tanker : tankers) {
            //send tanker on a job if it's ready
            if (tanker.isReady()) {
                //set its state to not ready
                tanker.setReady(false);
                sendRequest("sj:" + host + "," + port,tanker.getTankerHost(), Integer.parseInt(tanker.getTankerPort()));
                //return 1 if successful
                System.out.println("Tanker " + tanker.getId() + " dispatched to sewage collection at " + port);
                return 1;
            }
        }
        return 0;
    }
    @Override
    public void setReadyToServe(int number) {
        for (TankerData tanker : tankers) {
            if (tanker.getId() == number) {
                tanker.setReady(true);
                System.out.println("Tanker " + number + " is ready");
                break;
            }
        }
    }
    public void sendGetStatusRequest(int number) {
        String response = sendRequest("gs:" + number,sewagePlantHost, sewagePlantPort);
        System.out.println("Total sewage dropped off by tanker " + number + ": " + response);
    }
    public static void main(String[] args) throws IOException {
        int officePort = 8765;
        int sewagePlantPort = 8766;
        String universalHost = "localhost";
        Office office = new Office(officePort, sewagePlantPort, universalHost);
        office.startListening();
    }
}
