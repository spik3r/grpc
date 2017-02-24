package model;

import net.badata.protobuf.converter.annotation.ProtoClass;
import net.badata.protobuf.converter.annotation.ProtoField;
import net.badata.protobuf.example.proto.RegisteredUser;
import net.badata.protobuf.example.proto.User;

/**
 * Created by kai-tait on 24/02/2017.
 */
@ProtoClass(RegisteredUser.class)
public class DomainRegisteredUser {

    public DomainRegisteredUser() {

    }

    public DomainRegisteredUser(RegisteredUser user, String token) {
        this.name = user.getUser().getName();
        this.password = user.getUser().getPassword();
        this.token = token;
    }

    public DomainRegisteredUser(String name, String password, String token) {
        this.name = name;
        this.password = password;
        this.token = token;
    }

    @ProtoField
    private String name;
    @ProtoField
    private String password;
    @ProtoField
    private String token;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "name: " + name + " \n" +
               "password: " + password + " \n" +
               "token: " + token;
    }
}
