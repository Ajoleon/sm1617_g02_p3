package git.ujaen.es.practica2;

/**
 * Created by Antonio on 10/11/2016.
 */

public class Sesion{
    public String mSessionId;
    public String mExpires;

    public Sesion(String sessionId, String expires){
        this.mSessionId = sessionId;
        this.mExpires = expires;
    }
    public void setmSessionId(String sessionid){
        this.mSessionId = sessionid ;
    }
    public void setmExpires(String expires){
        this.mExpires = expires ;
    }
    public String getmSessionId(){
        return this.mSessionId;
    }
    public String getmExpires(){
        return this.mExpires;
    }
}
