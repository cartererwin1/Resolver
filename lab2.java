import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
public class lab2 {
    public static String getPredicate(String clause) {
        String predicate = clause;
        predicate = clause.split("\\(")[0];

        if (predicate.contains("!")) {
            predicate = predicate.substring(1, predicate.length());
        }
        return predicate;
    }

    public static String getConstant(String clause) {
        String constant = clause;
        constant = clause.split("\\(")[1];
        constant = constant.substring(0, constant.length() - 2);
        return constant;
    }
    public static String resolve(String[] predicates, String[] variables, String[] constants, ArrayList<ArrayList<String>> clauses) {
        //ArrayList<String> resolve = new ArrayList<>();
        //resolve.addAll(clauses);
        for(int x = 0; x < clauses.size(); x++) {
            for(int y = 0; y < clauses.get(x).size(); y++) {
                String clause1 = clauses.get(x).get(y);
                //System.out.println(clauses.get(x).get(y));
                //System.out.println(getPredicate(clauses.get(x).get(y)));
                if(clause1.contains("!")) {
                    //System.out.println(clauses.get(x).get(y) + " is negated");
                    for(int a = 0; a < clauses.size(); a++) {
                        for(int b = 0; b < clauses.get(a).size(); b++) {
                            String clause2 = clauses.get(a).get(b);
                            if(!clause2.contains("!")) {
                                //System.out.println(getPredicate(clause1));
                                //System.out.println(getPredicate(clause2));
                                if(getPredicate(clause1).equals(getPredicate(clause2))) {
                                    if(clause1.contains("(") && clause2.contains("(")) {
                                        if(getConstant(clause1).equals(clause2)) {
                                            clauses.get(x).remove(y);
                                            clauses.get(a).remove(b);
                                        }
                                    } else {
                                        clauses.get(x).remove(y);
                                        clauses.get(a).remove(b);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    //System.out.println(clauses.get(x).get(y) + " is not negated");
                    for(int a = 0; a < clauses.size(); a++) {
                        for(int b = 0; b < clauses.get(a).size(); b++) {
                            String clause2 = clauses.get(a).get(b);
                            if(clause2.contains("!")) {
                                if(getPredicate(clause1).equals(getPredicate(clause2))) {
                                    if(clause1.contains("(") && clause2.contains("(")) {
                                        if(getConstant(clause1).equals(clause2)) {
                                            clauses.get(x).remove(y);
                                            clauses.get(a).remove(b);
                                        }
                                    } else {
                                        clauses.get(x).remove(y);
                                        clauses.get(a).remove(b);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //System.out.println(clauses);
        for(ArrayList<String> array : clauses) {
            if (!array.isEmpty()) {
                return "yes";
            }
        }
        return "no";            
        /*
            String clause = clauses.get(x).get(y).split("(")[0];
            System.out.println(clause);
            if(clauses.get(i).contains("!")) {
                for(int j = 0; j < clauses.size() -1; j++) {
                    if(i != j) {
                        if(!clauses.get(j).contains("!")) {
                            resolve.remove(i);
                            resolve.remove(j);
                        }
                    }
                }
            }
            else {
                for(int j = 0; j < clauses.size() -1; j++) {
                    if(i != j) {
                        if(clauses.get(j).contains("!")) {
                            resolve.remove(i);
                            resolve.remove(j);
                        }
                    }
                }
            }
        }
        System.out.println(resolve); 
        */
    }
    public static void main(String[] args) {
        Scanner sc;
        File inputFile = new File(args[0]);
        try {
            sc = new Scanner(inputFile);
            String[] line = sc.nextLine().strip().split(" ");
            String[] predicates = new String[line.length-1];
            for(int i = 1; i < line.length; i++) {
                predicates[i-1] = line[i];
            }

            line = sc.nextLine().strip().split(" ");
            String[] variables = new String[line.length-1];
            for(int i = 1; i < line.length; i++) {
                variables[i-1] = line[i];
            }

            line = sc.nextLine().strip().split(" ");
            String[] constants = new String[line.length-1];
            for(int i = 1; i < line.length; i++) {
                constants[i-1] = line[i];
            }
        
            line = sc.nextLine().strip().split(" ");
            String[] functions = new String[line.length-1];
            for(int i = 1; i < line.length; i++) {
                functions[i-1] = line[i];
            }
            ArrayList<ArrayList<String>> clauses = new ArrayList<>();
            sc.nextLine();
            int lineNum = 0;
            while(sc.hasNextLine()) {
                line = sc.nextLine().split(" ");
                clauses.add(new ArrayList<String>());
                for(int i = 0; i < line.length; i++) {
                    clauses.get(lineNum).add(line[i]);
                }
                lineNum++;
            }
            sc.close();
            System.out.println(resolve(predicates, variables, constants, clauses));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
