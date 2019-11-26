package model;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Data
public class RestErrorResponse {

    private int code;
    private String message;

    public RestErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
