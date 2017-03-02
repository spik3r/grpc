package grpc.connection;

import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class TestClientConnectionManagerImpl implements ClientConnectionManager {

    private final ManagedChannel channel;

    public TestClientConnectionManagerImpl(String uniqueServerName) throws IOException {

        final InProcessChannelBuilder channelBuilder =
                InProcessChannelBuilder.forName(uniqueServerName).directExecutor();
        channel = channelBuilder.build();
    }
    public ManagedChannel getChannel() {
        return channel;
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
}
