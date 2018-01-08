import java.sql.*;
import java.util.*;
import java.lang.*;

public class Calculator {
    public static Connection conn=null;
    public static PreparedStatement pstm=null;
    public static Statement stm=null;
    public static void main(String[] args) throws SQLException {
        System.out.println(" Welcome to Calculator ");
        Scanner scanner = new Scanner(System.in);
        char c='y';
        do {
            System.out.println("\n ------------- Menu ------------");
            System.out.println("\n 1) View All Operations");
            System.out.println("\n 2) View All Operations based on Operator");
            System.out.println("\n 3) Perform Operations");

            System.out.print("\n\n Enter Choice: ");
            int ch=scanner.nextInt();

            switch(ch){
                case 1:
                    viewAll();
                    continue;
                case 2:
                    viewOperatorSpecific();
                    continue;
                case 3:
                    performOperation();
                    continue;
                    default:{
                        System.out.println("Invalid Choice");
                        continue;
                    }
            }
        }while(true);
    }

    public static void viewAll(){
        try {
            conn=ConnectionConfig.getConnection();
            stm=conn.createStatement();
            ResultSet rs=stm.executeQuery("select * from transactions");
            while (rs.next()){
                String output=getString(rs.getString("operation"),rs.getInt("number1"),rs.getInt("number2"));
                System.out.println(output);
            }
        }catch (SQLException se) {
            se.printStackTrace();
        }

    }
    public static void viewOperatorSpecific() throws SQLException
    {
        String operationName=null;
        Scanner scanner = new Scanner(System.in);
        char c='y';
        try {
            conn=ConnectionConfig.getConnection();
            pstm=conn.prepareStatement("select * from transactions where operation=?");
        }catch (SQLException se) {
            se.printStackTrace();
        }
        do{
            if(conn!=null) {
                System.out.println("Choose operator");
                System.out.println("1.Add\n" + "2.Subtract\n" + "3.Multiplication\n" + "4.Division\n" + "5.Power\n" + "6.Absolute\n" +
                        "7.Modulus\n" + "8.Maximum of Two\n" + "9.Minimum of Two");
                int operator = scanner.nextInt();

                switch (operator) {
                    case 1:
                        operationName = "Add";
                        break;
                    case 2:
                        operationName = "Substract";
                        break;
                    case 3:
                        operationName = "Multiply";
                        break;
                    case 4:
                        operationName = "Divide";
                        break;
                    case 5:
                        operationName = "Power";
                        break;
                    case 6:
                        operationName = "Abs";
                        break;
                    case 7:
                        operationName = "Mod";
                        break;
                    case 8:
                        operationName = "Max";
                        break;
                    case 9:
                        operationName = "Min";
                        break;
                    default:
                        System.out.println("\n Please select a valid character");
                        continue;
                }
            }
            else{
                System.out.println("Connection Failed");
                return;
            }
            try {
                pstm.setString(1,operationName);
                ResultSet rs=pstm.executeQuery();
                if(rs.next()==false)
                    System.out.println("No Operations performed of "+operationName);
                rs.beforeFirst();
                while (rs.next()){
                    String output=getString(rs.getString("operation"),rs.getInt("number1"),rs.getInt("number2"));
                    System.out.println(output);
                }
            }finally {
                System.out.println("Do you want to view operations based on another Operator? (y/n)");
                c=scanner.next().charAt(0);
            }
        }while(c=='y'||c=='Y');
        if(conn!=null)
            conn.close();
        if(pstm!=null)
            pstm.close();
    }

    public static void performOperation() throws SQLException
    {
        Scanner scanner = new Scanner(System.in);
        char c='y';
        do{
            System.out.println("\n Please enter two numbers");
            System.out.print("\n First number: ");
            float firstNumber = scanner.nextFloat();
            System.out.print("\n Second number: ");
            float secondNumber = scanner.nextFloat();
            System.out.println("Choose operation");
            System.out.println("1.Add\n" + "2.Subtract\n" + "3.Multiplication\n" + "4.Division\n" + "5.Power\n" + "6.Absolute\n" +
                    "7.Modulus\n" + "8.Maximum of Two\n" + "9.Minimum of Two");

            int operation = scanner.nextInt();
            double result=getResult(firstNumber,secondNumber,operation);
            System.out.println("Ans : "+result);
            System.out.println("Do you want to perform more operations? (y/n)");
            c=scanner.next().charAt(0);
        }while(c=='y'||c=='Y');
    }

    public static double getResult(float firstNumber,float secondNumber,int operation) throws SQLException {
        double result=0;
        String operationName=null;

        try{
            conn=ConnectionConfig.getConnection();
            pstm=conn.prepareStatement("insert into transactions(operation,number1,number2,result) values(?,?,?,?)");
        }
        finally {
            if(conn!=null){
                switch (operation)
                {
                    case 1:
                        operationName="Add";
                        result=firstNumber + secondNumber;
                        break;
                    case 2:
                        operationName="Substract";
                        result=firstNumber - secondNumber;
                        break;
                    case 3:
                        operationName="Multiply";
                        result=firstNumber * secondNumber;
                        break;
                    case 4:
                        operationName="Divide";
                        if(secondNumber==0) {
                            System.out.println("Not a valid Division, with zero as second number");
                            return 0;
                        }
                        else {
                            result = firstNumber / secondNumber;
                        }
                        break;
                    case 5:
                        operationName="Power";
                        double n1=firstNumber;
                        double n2=secondNumber;
                        result=Math.pow(n1,n2);
                        break;
                    case 6:
                        operationName="Abs";
                        result=Math.abs(firstNumber);
                        break;
                    case 7:
                        operationName="Mod";
                        result=firstNumber%secondNumber;
                        break;
                    case 8:
                        operationName="Max";
                        result=Math.max(firstNumber,secondNumber);
                        break;
                    case 9:
                        operationName="Min";
                        result=Math.min(firstNumber,secondNumber);
                        break;
                    default: System.out.println("\n Please select a valid character");
                        break;

                }
                try{
                    pstm.setString(1,operationName);
                    pstm.setFloat(2,firstNumber);
                    pstm.setFloat(3,secondNumber);
                    pstm.setDouble(4,result);
                    pstm.executeUpdate();
                }finally {
                    if(pstm!=null)
                        pstm.close();
                    if(conn!=null)
                        conn.close();
                }
            }
            else{
                System.out.println("Connection Failed");
            }
        }
        return result;
    }

    public static String getString(String op,int n1,int n2){
        String rv="";
        switch (op) {
            case "Add":
                rv="=> "+n1 + " + "+n2;
                break;
            case "Substract":
                rv="=> "+n1 + " - "+n2;
                break;
            case "Multiply":
                rv="=> "+n1 + " X "+n2;
                break;
            case "Divide":
                rv="=> "+n1 + " / "+n2;
                break;
            case "Power":
                rv="=> "+n1 + " ^ "+n2;
                break;
            case "Abs":
                rv="=> "+"| " + n1 + " |";
                break;
            case "Mod":
                rv="=> "+n1 + " % "+n2;
                break;
            case "Max":
                rv="=> "+"Maximum of "+n1+" & "+n2;
                break;
            case "Min":
                rv="=> "+"Minimum of "+n1+" & "+n2;
                break;
        }
        return  rv;
    }
}