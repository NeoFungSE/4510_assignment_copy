import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import medicalERSystem.MedicalERSystem;

public class ERSimulationTest {

    public static void main(String[] args) {
        runEdgeCase1();
        runEdgeCase2();
        runEdgeCase3();
        runEdgeCase4();
    }

    private static void runEdgeCase1() {
        String scenarioName = "Edge case 1 — Sample / Mixed triage with 2 doctors";
        String simulatedInput = "y\n6\n2\n3\nAlice\n5\n3\nBob\n5\n3\nCharlie\n3\n1\nDavid\n4\n2\nEve\n3\n0\nn\n";
        String[] expectedStrings = {
            "Doctor 1 starts treating patient David",
            "Total minutes simulated: 6",
            "Number of doctors: 2",
            "Number of patients arrived: 5",
            "Number of patients served: 3",
            "Number of patients still waiting: 2",
            "Average queue length during the simulation: 1.33",
            "Maximum queue length during the simulation: 3"
        };
        runScenario(scenarioName, simulatedInput, expectedStrings);
    }

    private static void runEdgeCase2() {
        String scenarioName = "Edge case 2 — Single doctor overloaded; same triage FIFO check";
        String simulatedInput = "y\n5\n1\n2\nA\n3\n1\nB\n2\n1\nC\n1\n3\nD\n2\n0\nn\n";
        String[] expectedStrings = {
            "Doctor 1 starts treating patient A",
            "Doctor 1 starts treating patient B",
            "Total minutes simulated: 5",
            "Number of doctors: 1",
            "Number of patients arrived: 4",
            "Number of patients served: 2",
            "Number of patients still waiting: 2",
            "Average queue length during the simulation: 1.40",
            "Maximum queue length during the simulation: 2"
        };
        runScenario(scenarioName, simulatedInput, expectedStrings);
    }

    private static void runEdgeCase3() {
        String scenarioName = "Edge case 3 — Invalid inputs & re-prompting";
        String simulatedInput = "y\n-1\n31\n3\n0\n11\n1\n9\na\n2\n123$\nJohn\n-5\n3\n0\n0\nn\n";
        String[] expectedStrings = {
            "Invalid input. Please enter a valid number for the number of minutes.",
            "Warning: Simulating with a large number of minutes may lead to a very long simulation time or errors.",
            "Invalid input. Please enter a valid number for the number of doctors.",
            "Warning: Simulating with a large number of doctors may lead to a very long simulation time or errors.",
            "Invalid triage level. Please enter a number between 0 and 3.",
            "Invalid input. Please enter a number between 0 and 3 for the triage level.",
            "Invalid input. Please enter a valid patient name.",
            "Invalid treatment time. Please enter a positive number.",
            "Total minutes simulated: 3",
            "Number of doctors: 1",
            "Number of patients arrived: 1",
            "Number of patients served: 1",
            "Number of patients still waiting: 0"
        };
        runScenario(scenarioName, simulatedInput, expectedStrings);
    }

    private static void runEdgeCase4() {
        String scenarioName = "Edge case 4 — Multiple doctors freeing simultaneously and idle doctors";
        String simulatedInput = "y\n5\n3\n3\nAnna\n2\n3\nBen\n1\n0\n2\nCara\n3\n1\nDave\n2\nn\n";
        String[] expectedStrings = {
            "Doctor 2 is available but no patients are waiting.",
            "Doctor 3 is available but no patients are waiting.",
            "Doctor 1 is available but no patients are waiting.",
            "Doctor 2 is available but no patients are waiting.",
            "Total minutes simulated: 5",
            "Number of doctors: 3",
            "Number of patients arrived: 4",
            "Number of patients served: 4",
            "Number of patients still waiting: 0",
            "Average queue length during the simulation: 0.00",
            "Maximum queue length during the simulation: 0"
        };
        runScenario(scenarioName, simulatedInput, expectedStrings);
    }

    private static void runScenario(String scenarioName, String simulatedInput, String[] expectedStrings) {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            System.setOut(new PrintStream(outputStream));
            MedicalERSystem.main(new String[0]);
        } catch (Exception e) {
            originalOut.println("[ERROR] " + scenarioName + " threw exception: " + e);
            e.printStackTrace(originalOut);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }

        String output = outputStream.toString();
        boolean passed = true;
        for (String expected : expectedStrings) {
            if (!output.contains(expected)) {
                passed = false;
                System.out.println("[FAIL] " + scenarioName + ": missing expected output -> " + expected);
            }
        }
        if (passed) {
            System.out.println("[PASS] " + scenarioName);
            System.out.println("[DETAILS] " + scenarioName + " output:");
            System.out.println(output);
        } else {
            System.out.println("[DETAILS] " + scenarioName + " output:");
            System.out.println(output);
        }
    }
}
