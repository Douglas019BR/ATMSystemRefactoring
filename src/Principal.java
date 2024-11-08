package sistemaCaixaAutomatico;

public class Principal {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Instanciacao do caixa automatico com a senha do caixa
		TrmCxAut meuTrmCxAut = new TrmCxAut(123);
		
		//utilizacao do terminal do caixa
		meuTrmCxAut.iniciarOperacao();

	}

}