package grpc.connection;

import io.grpc.ManagedChannel;

public interface ClientConnectionManager {
    public ManagedChannel getChannel();
}
