package medicalERSystem;
import java.util.InputMismatchException;
import medicalERSystem.InvalidInputException;

public class MedicalERSystem {
    // The actual implementation of the MedicialER application would go here.

    public static void main(String[] args) throws InvalidInputException {
        ERSimulation simulation = new ERSimulation();
        try {
            simulation.run();
        } catch (InvalidInputException e) {
            System.out.println("Invalid input. Please enter a valid number for the number of doctors.");
        } catch (InputMismatchException e) {
            System.out.println(e.getMessage());
        }
    }
}

