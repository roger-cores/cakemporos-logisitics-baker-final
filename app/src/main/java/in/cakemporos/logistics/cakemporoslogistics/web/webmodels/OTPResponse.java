package in.cakemporos.logistics.cakemporoslogistics.web.webmodels;

/**
 * Created by roger on 9/22/2016.
 */
public class OTPResponse {
    private Integer code;

    private String sessionId;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
