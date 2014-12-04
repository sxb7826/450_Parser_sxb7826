package edu.louisiana.cacs;

import edu.louisiana.cacs.csce450Project.Parser;

public class Main{
    public static void main(String[] args){
        System.out.println("Hello World from Main");
        Parser myParser = new Parser(args[0]);
        myParser.parse();
    }
}