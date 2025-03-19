package src;
//Console + GUI Driver

import javax.swing.*;
import java.util.ArrayList;

public class Display {

    public static void displayConsole(){
        //Console Display
        ArrayList<ArrayList<String>> dataset = DataProcess.tokenizeArray(DataProcess.createArray());
        String dataLine1 = DataProcess.multiArrayToString(dataset, 0);
        String dataLine10 = DataProcess.multiArrayToString(dataset, 9);
        String dataSize = String.valueOf(dataset.size());

        System.out.println("Line 1: " + dataLine1 + "\nLine 10: " + dataLine10 + "\nTotal Entries: " + dataSize);
    }

    public static void displayGUI(){
        //GUI Display
        SwingUtilities.invokeLater(TablePanel::displayTable);
    }

    public static void main(String[] args){
        //Display.displayConsole();
        Display.displayGUI();
    }
}
