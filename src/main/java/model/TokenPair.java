package model;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
@Data
public class TokenPair implements Serializable {

    private String accessToken;
    private String refreshToken;

}
