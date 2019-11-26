package model;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Data
public class LoginRequest {
    private String username;
    private String password;
}
