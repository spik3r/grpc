package application;

import com.test.generated.LoginServiceGrpc;
import com.test.generated.RegisteredUser;
import com.test.generated.User;
import grpc.connection.ClientConnectionManager;
import grpc.connection.ClientConnectionManagerImpl;
import io.grpc.ManagedChannel;
import model.DomainRegisteredUser;
import model.DomainUser;
import net.badata.protobuf.converter.Converter;

import java.util.concurrent.TimeUnit;

/**
 * Created by kai-tait on 24/02/2017.
 */
public class ProtobufClient {

    private static DomainUser currentUser;
    private String userToken;
    private final ManagedChannel channel;
    private final LoginServiceGrpc.LoginServiceBlockingStub blockingStub;

    public ProtobufClient(ClientConnectionManager manager) {
        this.channel = manager.getChannel();
        blockingStub = LoginServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() {
        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean signIn(final DomainUser user) {
        User userCredentials = Converter.create().toProtobuf(User.class, user);
        System.out.println("userCredentials: " + userCredentials);
        RegisteredUser registeredGrpcUser = blockingStub.signIn(userCredentials);

        userToken = registeredGrpcUser.getToken();
        DomainRegisteredUser domainRegisteredUser = Converter.create().toDomain(DomainRegisteredUser.class, registeredGrpcUser);
        System.out.println(domainRegisteredUser.toString());
        if(userToken != null && !userToken.isEmpty()) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws InterruptedException {
        ClientConnectionManagerImpl manager = new ClientConnectionManagerImpl();
        ProtobufClient protobufClient = new ProtobufClient(manager);

        DomainUser dummyUser = new DomainUser("test", "asdfghjkl");
        boolean signedIn = protobufClient.signIn(dummyUser);
        System.out.println("Signed in: " + signedIn);
        manager.shutdown();
    }
}
