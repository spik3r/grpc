package model;

import net.badata.protobuf.converter.annotation.ProtoClass;
import net.badata.protobuf.converter.annotation.ProtoField;
import net.badata.protobuf.example.proto.User;

/**
 * Created by kai-tait on 24/02/2017.
 */

@ProtoClass(User.class)
public class DomainUser {

    public DomainUser(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @ProtoField
    private String name;
    @ProtoField
    private String password;

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
}
