package grpc;

import com.example.generated.GreetingServiceGrpc;
import com.example.generated.HelloRequest;
import com.example.generated.HelloResponse;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

/**
 * Created by kai-tait on 22/02/2017.
 */
public class MyGrpcServer {
    public static void main(String[] args) throws InterruptedException, IOException {
        noSsl();
    }

    public static void noSsl() throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8888)
                .addService((BindableService) new GreetingServerImpl())
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
}
