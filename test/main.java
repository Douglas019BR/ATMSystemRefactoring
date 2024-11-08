
public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide the password as an argument.");
            return;
        }
        String senha = args[0];
        TrmCxAut trm = new TrmCxAut(senha);
        trm.iniciarOperacao();
    }
}