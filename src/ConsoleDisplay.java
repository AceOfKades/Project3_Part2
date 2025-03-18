//Console Driver

import java.util.ArrayList;

public class ConsoleDisplay {
    public static void main(String[] args){
        ArrayList<ArrayList<String>> dataset = DataProcess.tokenizeArray(DataProcess.createArray());
        String dataLine1 = DataProcess.multiArrayToString(dataset, 0);
        String dataLine10 = DataProcess.multiArrayToString(dataset, 9);
        String dataSize = String.valueOf(dataset.size());

        System.out.println("Line 1: " + dataLine1 + "\nLine 10: " + dataLine10 + "\nTotal Entries: " + dataSize);
    }
}
