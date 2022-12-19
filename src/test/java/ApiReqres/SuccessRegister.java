package ApiReqres;

public class SuccessRegister {
    private Integer id;
    private String token;

    public SuccessRegister(){};

    public SuccessRegister(int id, String token) {
        this.id = id;
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
