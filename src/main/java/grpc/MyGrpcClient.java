package grpc;

//import com.example.grpc.GreetingServiceGrpc;
//import com.example.grpc.HelloRequest;
//import com.example.grpc.HelloResponse;
//import io.grpc.Deadline;
//import io.grpc.ManagedChannel;
//import io.grpc.ManagedChannelBuilder;

import com.example.generated.GreetingServiceGrpc;
import com.example.generated.HelloRequest;
import com.example.generated.HelloResponse;
import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by kai-tait on 22/02/2017.
 */
public class MyGrpcClient {
    public static void main(String[] args) throws InterruptedException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException, IOException {
        System.out.println();

        // 1. Channel, ManagedChannel, usePlainText?
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 8888)
                .usePlaintext(true)
                .build();

        // 2. Load Balancing, Name Resolver
        GreetingServiceGrpc.GreetingServiceBlockingStub stub = GreetingServiceGrpc.newBlockingStub(channel);
        HelloResponse response = stub.greet(HelloRequest.newBuilder().setName("bob").setAge(2).setSentimentValue(1).build());
        HelloResponse response2 = stub.greet(HelloRequest.newBuilder().setName("toby").setAge(86).setSentimentValue(2).build());
        stub.withDeadline(Deadline.after(500, TimeUnit.MILLISECONDS));
        System.out.println(response);
        System.out.println(response2);

        channel.shutdown().awaitTermination(5, SECONDS);
        // 3. Blocking vs Non-blocking Stub
        // 4. Builders
        // 5. Deadline
    }
}
