package application;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import model.DomainRegisteredUser;
import model.DomainUser;
import net.badata.protobuf.converter.Converter;
import net.badata.protobuf.example.proto.LoginServiceGrpc;
import net.badata.protobuf.example.proto.RegisteredUser;
import net.badata.protobuf.example.proto.User;

import java.util.concurrent.TimeUnit;

/**
 * Created by kai-tait on 24/02/2017.
 */
public class ProtobufClient {

    private static DomainUser currentUser;
    private String userToken;
    private final ManagedChannel channel;
    private final LoginServiceGrpc.LoginServiceBlockingStub blockingStub;

    public ProtobufClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext(true)
                .build();
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
//        DomainRegisteredUser domainRegisteredUser = Converter.create().toDomain(DomainRegisteredUser.class, registeredGrpcUser);
        DomainRegisteredUser domainRegisteredUser = new DomainRegisteredUser(registeredGrpcUser, userToken);
        System.out.println(domainRegisteredUser.toString());
        if(userToken != null && !userToken.isEmpty()) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        ProtobufClient protobufClient = new ProtobufClient("localhost", 8888);
        DomainUser dummyUser = new DomainUser("test", "asdfghjkl");
        boolean signedIn = protobufClient.signIn(dummyUser);
        System.out.println("Signed in: " + signedIn);
    }
}
