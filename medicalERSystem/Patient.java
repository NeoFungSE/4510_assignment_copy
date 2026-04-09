package medicalERSystem;
public class Patient {
    public final int triage;
    public final String name;
    public final int treatmentTime;
    public Patient(int triage, String name, int treatmentTime) {
        this.triage = triage;
        this.name = name;
        this.treatmentTime = treatmentTime;
    }
    public String toString() {
        return "(" + name + ", L" + triage + ", " + treatmentTime + ")";
    }
}