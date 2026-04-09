package medicalERSystem;

import java.util.InputMismatchException;
import java.util.Scanner;

class ERSimulation {
    Boolean isRunning = true;
    Scanner scanner = new Scanner(System.in);
    public void run() throws InvalidInputException {
        System.out.print("Welcome to the Medical ER System! Do you want to start the simulation? (Y/N): ");
        while (isRunning) {
            String input = scanner.nextLine();
            if( input.equalsIgnoreCase("y") ) {
                String tempmin = "minute";
                Boolean noPatient = false;
                Boolean triageValid = false;
                Boolean treatmentTimeValid = false;
                Boolean nameValid = false;
                Boolean availableDoc = false;
                int numberOfDoctors = 0;
                int numberOfMinutes = 0;
                int triageLevel = 0;
                String patientName = "";
                int treatmentTime = 0;
                int patientCount = 0;
                int patientServed = 0;
                int maxQueueLength = 0;
                double averageQueueLength = 0.0;
                System.out.println("... Setting up the ER system simulation ...");
                while ( numberOfMinutes < 1 ) { // Validate that the number of minutes is a positive integer
                    try { // Prompt the user to enter the number of minutes to simulate and validate the input
                        System.out.print("Please enter the number of minutes( 1 minute per round ) to simulate: ");
                        numberOfMinutes = scanner.nextInt();
                        if (numberOfMinutes < 1) {
                            throw new InvalidInputException("Invalid input. Please enter a valid number for the number of minutes.");
                        }  else if ( numberOfMinutes > 30 ) {
                            System.out.println("Warning: Simulating with a large number of minutes may lead to a very long simulation time or errors. Please consider using a smaller number of minutes for a more manageable simulation. Maximum recommended minutes is 30.");
                            numberOfMinutes = 0;
                        }
                        scanner.nextLine(); // Clear the input buffer
                    } catch (InvalidInputException e) {
                        System.out.println("Invalid input. Please enter a valid number for the number of minutes.");
                        scanner.nextLine(); // Clear the invalid input
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid number for the number of minutes.");
                        scanner.nextLine(); // Clear the invalid input
                    }
                }
                while ( numberOfDoctors < 1 ) { // Validate that the number of doctors is a positive integer
                    try { // Prompt the user to enter the number of doctors to simulate and validate the input
                        System.out.print("Please enter the number of doctors to simulate: ");
                        numberOfDoctors = scanner.nextInt();
                        if (numberOfDoctors < 1) {
                            throw new InvalidInputException("Invalid input. Please enter a valid number for the number of doctors.");
                        } else if ( numberOfDoctors > 10 ) {
                            System.out.println("Warning: Simulating with a large number of doctors may lead to a very long simulation time or errors. Please consider using a smaller number of doctors for a more manageable simulation. Max number of doctors is set to 10.");
                            numberOfDoctors = 0;
                        }
                        scanner.nextLine(); // Clear the input buffer
                    } catch (InvalidInputException e) {
                        System.out.println("Invalid input. Please enter a valid number for the number of doctors.");
                        scanner.nextLine(); // Clear the invalid input
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid number for the number of doctors.");
                        scanner.nextLine(); // Clear the invalid input
                    }
                }
                int[] doctors = new int[numberOfDoctors]; // Array to keep track of doctor availability, where the value represents the time until the doctor is available ( positive number means when will be available for treatment )
                for (int i = 0; i < numberOfDoctors; i++) {
                    doctors[i] = 0; // Initialize all doctors as available (0 means available, positive number means minutes left for treatment)
                }
                System.out.println("... Starting the simulation..."); // Indicate the start of the simulation
                ListQueue patientQueue = new ListQueue(); // Queue to manage patients waiting for treatment, using ListQueue which is a linked list implementation of a queue

                for( int minuteCount = 1; minuteCount <= numberOfMinutes; minuteCount++ ) { // Main simulation loop that runs for the specified number of minutes
                    System.out.println("At the beginning of iteration " + minuteCount + " ..."); // Indicate the start of each iteration
                    while ( !triageValid ){
                        try { // Prompt the user to enter the triage level for a new patient and validate the input
                            System.out.print("Input triage level for a new patient [1-Critical, 2- Urgent, 3-Fair, 0-None]: ");
                            triageLevel = scanner.nextInt();
                            if ( triageLevel < 0 || triageLevel > 3 ) { // Validate that the triage level is between 0 and 3
                                throw new InvalidInputException("Invalid triage level. Please enter a number between 0 and 3.");
                            } else if ( triageLevel == 0 ) { // If triage level is 0, it means no patient came to the ER in this minute, so we can break out of the patient input loop and move on to processing doctors and patients in the queue.
                                System.out.println("No patient to came to ER in this minute.");
                                noPatient = true;
                                break;
                            } else {
                                triageValid = true; // If the input is valid, we can exit the patient input loop and proceed to ask for patient details if a patient came in this minute.
                            }
                            scanner.nextLine(); // Clear the input buffer
                        } catch ( InvalidInputException e ) {
                            System.out.println(e.getMessage());
                            scanner.nextLine(); // Clear the invalid input
                        } catch ( InputMismatchException e ) {
                            System.out.println("Invalid input. Please enter a number between 0 and 3 for the triage level.");
                            scanner.nextLine(); // Clear the invalid input
                        }
                    }
                    if( !noPatient ) {
                        while ( !nameValid ) { // Validate that the patient name is a non-empty string
                            try {
                                System.out.print("Input patient name: ");
                                patientName = scanner.nextLine().trim();
                                nameValid = patientName.matches("[\\p{L}'\\-\\s]+");
                                if ( !nameValid ) {
                                    throw new InvalidInputException("Invalid input. Please enter a valid patient name.");
                                }
                            } catch ( InvalidInputException e ) {
                                System.out.println(e.getMessage());
                            }
                        }
                        while ( !treatmentTimeValid ){
                            try {
                                System.out.print("Input treatment time (min): ");
                                treatmentTime = scanner.nextInt();
                                if ( treatmentTime < 1 ) {
                                    throw new InvalidInputException("Invalid treatment time. Please enter a positive number.");
                                } else {
                                    treatmentTimeValid = true;
                                }
                                scanner.nextLine(); // Clear the input buffer
                            } catch ( InvalidInputException e ) {
                                System.out.println(e.getMessage());
                                scanner.nextLine(); // Clear the invalid input
                            } catch ( InputMismatchException e ) {
                                System.out.println("Invalid input. Please enter a positive number for the treatment time.");
                                scanner.nextLine(); // Clear the invalid input
                            }
                        }
                        patientCount++;
                        Patient patient = new Patient(triageLevel, patientName, treatmentTime);
                        if( triageLevel == 3 || patientQueue.isEmpty() ) {
                            patientQueue.addToTail(patient);
                        } else {
                            insert(patientQueue, patient, triageLevel);
                        }
                        System.out.println("Patient " + patient.name + " with triage level " + patient.triage + " and treatment time " + patient.treatmentTime + " minutes added to the queue.");
                    }
                    // Here you would add the logic to process the patient based on their triage level and treatment time, and update the number of patients served and doctor availability accordingly.
                    
                    for ( int j = 0; j < numberOfDoctors; j++ ) {
                        if( doctors[j] <= minuteCount && !patientQueue.isEmpty()){
                            Patient patient = (Patient) patientQueue.dequeue();
                            int treatmentTimeLeft = patient.treatmentTime;
                            doctors[j] = minuteCount + treatmentTimeLeft; // Update the doctor's availability time
                            System.out.println("Doctor " + (j + 1) + " starts treating patient " + patient.name + " with triage level " + patient.triage + " for " + treatmentTimeLeft + " minutes.");
                            patientServed++;
                        } else if( doctors[j] <= minuteCount && patientQueue.isEmpty() ) {
                            System.out.println("Doctor " + (j + 1) + " is available but no patients are waiting.");
                            availableDoc = true;
                        }
                    }
                    if( availableDoc == false && !patientQueue.isEmpty() ) {
                        System.out.println("No available doctor to treat patients at this time.");
                    } else {
                        availableDoc = false; // Reset the available doctor flag for the next iteration
                    }
                    tempmin = (numberOfMinutes == 1 ? "minute" : "minutes");
                    System.out.println("After " + minuteCount + " " + tempmin + " ##");
                    for( int j = 0; j < numberOfDoctors; j++ ) {
                        // Print the status of each doctor here, including whether they are avaiYlable or treating a patient, and if treating a patient, how much time is left for that treatment.
                        if( doctors[j] <= minuteCount ) {
                            System.out.print("----- Doctor " + (j + 1) + " Available [ " + (doctors[j]) + " ] ");
                        } else {
                            System.out.print("----- Doctor " + (j + 1) + " Busy until minute [ " + (doctors[j]) + " ] ");
                        }
                        if( j == numberOfDoctors - 1 ) {
                            System.out.println("----- The Queue has " + patientQueue.count() + " patient(s) waiting: " + patientQueue.toString() + "");
                            averageQueueLength += patientQueue.count();
                        }
                    }
                    if( patientQueue.count() > maxQueueLength ) {
                        maxQueueLength = patientQueue.count();
                    }
                    noPatient = false;
                    triageValid = false;
                    treatmentTimeValid = false;
                    nameValid = false;
                }
                System.out.println("............. End of Simulation .............");
                System.out.println("Total " + tempmin + " simulated: " + numberOfMinutes);
                System.out.println("Number of doctors: " + numberOfDoctors);
                System.out.println("Number of patients arrived: " + patientCount);
                System.out.println("Number of patients served: " + patientServed);
                System.out.println("Number of patients still waiting: " + patientQueue.count());
                averageQueueLength = (double) averageQueueLength / numberOfMinutes;
                System.out.println("Average queue length during the simulation: " + String.format("%.2f", averageQueueLength));
                System.out.println("Maximum queue length during the simulation: " + maxQueueLength);

                System.out.print("Simulation ended. Would you like to run it again? (Y/N)");
                while (true) {
                    input = scanner.nextLine();
                    if( input.equalsIgnoreCase("y") ) {
                        break; // Break out of the restart input loop and start a new simulation
                    } else if( input.equalsIgnoreCase("n") ) {
                        isRunning = false;
                        break; // Break out of the restart input loop and end the program
                    } else {
                        System.out.print("Invalid input. Do you want to restart the simulation? Please enter 'Y' or 'N'.");
                    }
                }
            } else if (input.equalsIgnoreCase("n")) {
                isRunning = false;
            } else {
                System.out.println("Invalid input. Do you want to start the simulation? Please enter 'Y' or 'N'.");
                continue;
            }
        }
        System.out.println("Thank you for using the Medical ER System. Goodbye!");
        scanner.close();
    }

    void insert(ListQueue queue, Patient item, int priority) {
        if (queue.isEmpty()) {
            queue.addToHead(item);
            return;
        }
        ListNode current = queue.getHead();
        ListNode previous = null;
        while (current != null) {
            Patient currentPatient = (Patient) current.getData();
            if (currentPatient.triage > priority) {
                ListNode newNode = new ListNode(item, current);
                if (previous == null) queue.setHead(newNode);
                else previous.setNext(newNode);
                return;
            }
            previous = current;
            current = current.getNext();
        }
        queue.addToTail(item);
    }
}
