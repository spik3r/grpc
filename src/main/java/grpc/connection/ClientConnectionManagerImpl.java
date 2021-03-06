package grpc.connection;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public class ClientConnectionManagerImpl implements ClientConnectionManager {

    private ManagedChannel channel;

    public ClientConnectionManagerImpl() {
                channel = ManagedChannelBuilder
//                .forAddress("localhost", 10183)
                .forAddress("paul-vm.macolighting.com", 10183)
                .usePlaintext(true)
                .build();
    }
    public ManagedChannel getChannel() {
        return channel;
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
}
