package tech.pursuiters.techpursuiters.uitsocieties.ecell;

/**
 * Created by Tarun for ecell on 29-Dec-17.
 */

public class EcellDataModel {

    private String referral;
    private String email;

    public EcellDataModel(){
        //EMPTY CONSTRUCTOR
    }

    public EcellDataModel(String referral, String email) {
        this.referral = referral;
        this.email = email;
    }

    public String getReferral() {
        return referral;
    }

    public String getEmail() {
        return email;
    }

    public void setReferral(String referral) {
        this.referral = referral;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
