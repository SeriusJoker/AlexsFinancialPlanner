import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class InputTest {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Please type something: ");
        
        try {
            String input = reader.readLine();
            System.out.println("You typed: " + input);
        } catch (IOException e) {
            System.out.println("Error reading input: " + e.getMessage());
        }
    }
}
