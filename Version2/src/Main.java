import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Collections;

public class Main{
    public static Scanner in = new Scanner(System.in);

    public static String[][] DATA;
    public static int rows,cols;
    public static String atribute_type;
    //public HashSet<String> Atributes;

    public static void delimitDataSet(String filename){
        int r=0,c=0;
        try {
            Scanner f = new Scanner(new BufferedReader( new FileReader(filename)));
            String s;
            while(f.hasNextLine()){
                s=f.nextLine();
                if(c==0) c=s.split(",").length;
                r++;
            }
            f.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        rows=r;
        cols=c-1;
        DATA=new String[rows][cols];
    }
    public static void readFile(String filename){
        delimitDataSet(filename);

        File file = new File(filename);
        try {   
            Scanner filein = new Scanner(file);
            String[] data=filein.next().split(",");
            int r=0;
            addLine(data,r);
            r++;
            while(filein.hasNext()){
               data=filein.next().split(",");
               //for(String d : data) System.out.print(d+" ");
               addLine(data, r);
               r++;
            }
            filein.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void addLine(String[] data,int r){
        for(int c=1;c<data.length;c++)
            DATA[r][c-1]=data[c];
    }
    public static File[] displayFiles(String dir){
        File folder = new File("../"+dir);
        File[] listOfFiles = folder.listFiles();

        if(listOfFiles.length==0){
            System.out.println("No files");
            System.exit(0);
        }
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println((i+1)+")" + listOfFiles[i].getName());
            }
        }
        return listOfFiles;
    }
    public static void printDataSetArray(String[][] data){
        System.out.print("{");
        for(String line[] : data){
            System.out.print("[");
            for(String ele : line)
                System.out.print(ele+",");
            System.out.println("]");
        }
        System.out.println("}");
    }

    public static void printAtributeArray(String[] atributes){
        System.out.print("\nAtributes\n\t");
        for(String at:atributes)
            System.out.print("<"+at+">\n\t");
        System.out.println();
    }
    public static String[] makeAtributeArray(){
        String[] atributes=new String[DATA[0].length-1];
        int i=0;
        for(String atribute : DATA[0]){
            if(i<DATA[0].length-1)
                atributes[i++]=atribute;
        }
        return atributes;
    }

    public static void printAtributeSet(HashSet<Atribute> atribute_set){
        System.out.println("\nAttribute set:");
        for(Atribute a : atribute_set)
            System.out.print(a);
        System.out.println();
    }
    public static LinkedHashSet<Atribute> makeAtributeSet(String[] atributes){
        LinkedHashSet<Atribute> atributes_set=new LinkedHashSet<>();
        int i=0;
        for(String atribute_name : atributes){
            ArrayList<String> values=new ArrayList<>();
            for(int r=1;r<DATA.length;r++)
                if(!values.contains(DATA[r][i]))
                    values.add(DATA[r][i]);
            try {
                //data of atribute is continuous
                ArrayList<Double> continuous = new ArrayList<>();
                for(String val : values)
                    continuous.add(Double.parseDouble(val));
                
                Collections.sort(continuous);
                //System.out.println(continuous);
                //Calculates the mid points in data
                ArrayList<Double> average = new ArrayList<>();
                double avg=0;
                for(int j=1; j<continuous.size();j++){
                    avg=(double)((continuous.get(j-1)+continuous.get(j))/2);
                    average.add(avg);
                }
                Collections.sort(average);
                continuous.clear();
                continuous=average;
                values.clear();
                for(Double val : continuous)
                    values.add(""+val);
            } catch (NumberFormatException e){}
            Atribute atribute = new Atribute(atribute_name, values);
            atributes_set.add(atribute);
            i++;
        }
        return atributes_set;
    }
    
    public static ArrayList<DataEntry> makeDataSet(String[] atributes){
        int index=DATA[0].length-1;
        ArrayList<DataEntry> data_set=new ArrayList<>();
        for(int r=1;r<DATA.length;r++){
            ArrayList<String> values=new ArrayList<>();
            for(int c=0;c<index;c++) values.add(DATA[r][c]);
            
            String[] data_values = new String[values.size()];
            int i=0;
            for(String val : values) data_values[i++]=val;
            
            DataEntry data= new DataEntry(DATA[r][index], atributes, data_values);
            data_set.add(data);
        }
        return data_set;
    }

    public static void buildTree(){
        File[] listOfFiles = displayFiles("CSV");
        int i=in.nextInt();
        String str=listOfFiles[i-1].getName();
        String s=str.substring(0, str.lastIndexOf('.'));
                
        System.out.println("Building tree for "+s);
        readFile("../CSV/"+str);
        //printDataSetArray(DATA);

        //Create atributes array
            String[] atributes=makeAtributeArray();
                
        //Create set of atributes
            LinkedHashSet<Atribute> atributes_set=makeAtributeSet(atributes);
                   
        //entry exploration
            ArrayList<DataEntry> data_set=makeDataSet(atributes);

        DecisionTree tree = new DecisionTree(data_set, data_set, atributes_set);
        tree.buildTree();
        tree.prunTree();
        tree.printTree();

        
            //System.out.println("\n\n\n"+tree);

    }
    public static void showOptions() {
        System.out.println("What do you whant to do?");
        System.out.println("1)Build Tree;\n2)Test existing Tree;\n3)exit;");
        switch(in.nextInt()){
            case 1: 
                buildTree();
                System.exit(0);                
            break;
            case 2:
            default: System.exit(0);
        }
    }
    public static void main(String[] args){
        showOptions();
    }
}