package application;

import grpc.connection.TestClientConnectionManagerImpl;
import io.grpc.Server;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import model.DomainUser;
import net.badata.protobuf.example.proto.LoginServiceGrpc;
import net.badata.protobuf.example.proto.RegisteredUser;
import net.badata.protobuf.example.proto.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;

import java.io.IOException;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ProtobufClientTest {

    private TestClientConnectionManagerImpl connectionManager;
    private ProtobufClient protobufClient;
    private Server fakeServer;
    private final LoginServiceGrpc.LoginServiceImplBase serviceImpl = spy(new TestProtobufServer() {});

    @Before
    public void setUp() throws Exception {
        String uniqueServerName = "fake server for " + getClass();

        setUpFakeServer(uniqueServerName);
        connectionManager = new TestClientConnectionManagerImpl(uniqueServerName);
        protobufClient = new ProtobufClient(connectionManager);
    }

    public void setUpFakeServer(String uniqueServerName) throws IOException {
        fakeServer = InProcessServerBuilder
                .forName(uniqueServerName).directExecutor().addService(serviceImpl).build().start();
    }

    @After
    public void tearDown() throws Exception {
        connectionManager.shutdown();
        fakeServer.shutdown().awaitTermination(6, SECONDS);
    }

    @Test
    public void signIn() throws Exception {
        ArgumentCaptor<User> requestCaptor = ArgumentCaptor.forClass(User.class);
        DomainUser user = new DomainUser("Bob", "asdfghjkl");
        protobufClient.signIn(user);

        verify(serviceImpl).signIn(requestCaptor.capture(), Matchers.<StreamObserver<RegisteredUser>>any());
        assertThat(requestCaptor.getValue().getName(), is("Bob"));
        assertThat(requestCaptor.getValue().getPassword(), is("asdfghjkl"));
    }

    public static class TestProtobufServer extends LoginServiceGrpc.LoginServiceImplBase{

        @Override
        public void signIn(final User request, final StreamObserver<RegisteredUser> responseObserver) {
            String token = createToken();
            RegisteredUser response = RegisteredUser.newBuilder()
                    .setUser(request)
                    .setToken(token)
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        private String createToken() {
            return "secureToken" + UUID.randomUUID();
        }
    }
}