import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Define a class to represent assembly in the deductive database
class Assembly {
    // String predicate;
    String part;
    String subpart;
    int quantity;

    public Assembly(String subpart, String part, int quantity) {
        this.part = part;
        this.subpart = subpart;
        this.quantity = quantity;
    }
}

public class DeductiveDatabase {

    static List<Assembly> removeDuplicates(List<Assembly> assembly) {
        List<Assembly> uniqueAssembly = new ArrayList<>();
        for (Assembly fact : assembly) {
            boolean isDuplicate = false;
            for (Assembly uniqueFact : uniqueAssembly) {
                if (fact.part.equals(uniqueFact.part) &&
                        fact.subpart.equals(uniqueFact.subpart) &&
                        fact.quantity == uniqueFact.quantity) {
                    isDuplicate = true;
                    break;
                }
            }
            if (!isDuplicate) {
                uniqueAssembly.add(fact);
            }
        }
        return uniqueAssembly;
    }

    // Generate all subparts of a given component
    static void generateAllSubparts(String component, List<Assembly> assembly) {
        System.out.println("\n---Parts and Subparts of " + component + "---\n");
        System.out.println("Part Subpart Quantity");
        for (Assembly fact : assembly) {
            if (fact.part.equals(component)) {
                System.out.println(fact.part + " " + fact.subpart + "\t" + fact.quantity);
            }
        }
    }

    public static void main(String[] args) {
        List<Assembly> assembly = new ArrayList<>();
        assembly.add(new Assembly("Wheel", "Trike", 3));
        assembly.add(new Assembly("Frame", "Trike", 1));
        assembly.add(new Assembly("Spoke", "Wheel", 2));
        assembly.add(new Assembly("Tyre", "Wheel", 1));
        assembly.add(new Assembly("Seat", "Frame", 1));
        assembly.add(new Assembly("Pedal", "Frame", 1));
        assembly.add(new Assembly("Tube", "Tyre", 1));
        assembly.add(new Assembly("Rim", "Tyre", 1));


        System.out.println("---Applying first rule---\n");
        System.out.println("Part Subpart Quantity");
        for (Assembly result : assembly) {
            System.out.println(result.part + " " + result.subpart + "\t" + result.quantity);
        }

        // Applying the second rule
        assembly = applySecondRule(assembly);

        System.out.println("\n---Applying second rule---\n");
        System.out.println("Part Subpart Quantity");
        for (Assembly fact : assembly) {
            System.out.println(
                    fact.part + " " + fact.subpart + "\t" + fact.quantity);
        }

        // Applying the second rule twice
        assembly = applySecondRule(assembly);
        assembly = removeDuplicates(assembly);

        String component;
        System.out.print("\nEnter a component: ");
        Scanner sc = new Scanner(System.in);
        component = sc.nextLine();
        generateAllSubparts(component, assembly);
        sc.close();
    }

    static List<Assembly> applySecondRule(List<Assembly> assembly) {
        List<Assembly> newFacts = new ArrayList<>();
        for (Assembly fact1 : assembly) {
            for (Assembly fact2 : assembly) {
                if (fact1.part.equals(fact2.subpart)) {
                    newFacts.add(new Assembly(fact1.subpart, fact2.part, fact1.quantity));
                }
            }
        }
        assembly.addAll(newFacts);
        return assembly;
    }
}
