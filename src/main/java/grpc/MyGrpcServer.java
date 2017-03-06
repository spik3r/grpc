package grpc;

import com.example.generated.GreetingServiceGrpc;
import com.example.generated.HelloRequest;
import com.example.generated.HelloResponse;
import com.test.generated.LoginServiceGrpc;
import com.test.generated.RegisteredUser;
import com.test.generated.User;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.UUID;

public class MyGrpcServer {
    public static void main(String[] args) throws InterruptedException, IOException {
        noSsl();
    }

    public static void noSsl() throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8888)
                .addService((BindableService) new GreetingServerImpl())
                .addService((BindableService) new ProtobufServer())
                .build();

        server.start();
        System.out.println("Server started");
        server.awaitTermination();
    }

    public static class GreetingServerImpl extends GreetingServiceGrpc.GreetingServiceImplBase {
        @Override
        public void greet(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
            String greeting = "Hello, " + request.getName();
            System.out.println(request);

            HelloResponse response = HelloResponse.newBuilder().setGreeting(greeting).build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }


    }

    public static class ProtobufServer extends LoginServiceGrpc.LoginServiceImplBase{

        @Override
        public void signIn(final User request, final StreamObserver<RegisteredUser> responseObserver) {
            String username = "signIn, " + request.getName();
            String password = "signIn, " + request.getPassword();
            System.out.println(username);
            System.out.println(password);

            String token = createToken();
            System.out.println("token: " + token);
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
