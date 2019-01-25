package simplewebapp.dao;

public class Status {
    public boolean status;
    public String error;
    public Status(boolean status){
        this.status = status;
    }

    public Status(boolean status, String error){
        this.status = status;
        this.error = error;
    }
}
