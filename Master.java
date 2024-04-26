import java.util.Scanner;

public class Master extends Thread {
    private final Slave[] slaves;

    public Master(Slave[] slaves) {
        this.slaves = slaves;
    }

    @Override
    public void run() {
        System.out.println("Master: Sending commit message to all slaves");
        for (Slave slave : slaves) {
            slave.receiveCommitMessage();
        }

        boolean allYes = true;
        for (Slave slave : slaves) {
            if (!slave.waitForResponse()) {
                allYes = false;
                break;
            }
        }

        if (allYes) {
            System.out.println("Master: Received 'Yes' from all slaves. Committing now.");
            for (Slave slave : slaves) {
                slave.commit();
            }
        } else {
            System.out.println("Master: At least one slave voted 'No'. Aborting.");
        }
    }

    public static void main(String[] args) {
        Slave[] slaves = new Slave[4];
        for (int i = 0; i < 4; i++) {
            slaves[i] = new Slave("Slave " + (i + 1));
        }

        Master master = new Master(slaves);
        master.start();
    }
}

class Slave extends Thread {
    private final String name;

    public Slave(String name) {
        this.name = name;
    }

    public void receiveCommitMessage() {
        System.out.println(name + ": Received commit message from master.");
    }

    public boolean waitForResponse() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(name + ": Do you want to commit? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("yes");
    }

    public void commit() {
        System.out.println(name + ": Committing now.");
    }
}
