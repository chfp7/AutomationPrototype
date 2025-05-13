package application;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Project {
    int projID;
    private String mTypeA;
    private String mTypeB;
    private int numMachineA;
    private int numMachineB;
    private double sizeA;
    private double sizeB;
    private LocalDateTime issued;
    private String custEmail;
    private String userEmail;

    public Project() {}

    public Project(String mTypeA, String mTypeB, int numMachineA, int numMachineB,
    		double sizeA, double sizeB, LocalDateTime issued, String custEmail, String userEmail) {
        this.mTypeA = mTypeA;
        this.mTypeB = mTypeB;
        this.numMachineA = numMachineA;
        this.numMachineB = numMachineB;
        this.sizeA = sizeA;
        this.sizeB = sizeB;
        this.issued = issued;
        this.custEmail = custEmail;
        this.userEmail = userEmail;
    }

    public int getProjID() { return projID; }

    public String getMTypeA() { return mTypeA; }
    public void setMTypeA(String mTypeA) { this.mTypeA = mTypeA; }

    public String getMTypeB() { return mTypeB; }
    public void setMTypeB(String mTypeB) { this.mTypeB = mTypeB; }

    public int getNumMachineA() { return numMachineA; }
    public void setNumMachineA(int numMachineA) { this.numMachineA = numMachineA; }

    public int getNumMachineB() { return numMachineB; }
    public void setNumMachineB(int numMachineB) { this.numMachineB = numMachineB; }

    public double getSizeA() { return sizeA; }
    public void setSizeA(double sizeA) { this.sizeA = sizeA; }

    public double getSizeB() { return sizeB; }
    public void setSizeB(double sizeB) { this.sizeB = sizeB; }

    public LocalDateTime getIssued() { return issued; }
    public void setIssued(LocalDateTime issued) { this.issued = issued; }

    public String getCustEmail() { return custEmail; }
    public void setCustEmail(String custEmail) { this.custEmail = custEmail; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    
    

    @Override
    public String toString() {
        return "Project{" +
                "projID=" + projID +
                ", mTypeA='" + mTypeA + '\'' +
                ", mTypeB='" + mTypeB + '\'' +
                ", numMachineA=" + numMachineA +
                ", numMachineB=" + numMachineB +
                ", sizeA=" + sizeA +
                ", sizeB=" + sizeB +
                ", issued=" + issued +
                ", custEmail='" + custEmail + '\'' +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }
}

