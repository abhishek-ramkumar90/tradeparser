package Model;

public class Trade {

    private String sym;
    private Number P;
    private Number Q;
    private Long TS2;

    public String getSym ( ) {
        return sym;
    }

    public void setSym (String sym) {
        this.sym = sym;
    }

    public Number getP ( ) {
        return P;
    }

    public void setP (Number p) {
        P = p;
    }

    public Number getQ ( ) {
        return Q;
    }

    public void setQ (Number q) {
        Q = q;
    }

    public Long getTS2 ( ) {
        return TS2;
    }

    public void setTS2 (Long TS2) {
        this.TS2 = TS2;
    }


}
