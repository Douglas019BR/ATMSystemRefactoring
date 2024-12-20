package sistemaCaixaAutomatico;

import sistemaCaixaAutomatico.ContaCor;


public class ControladorCaixa {
	//Atributos
	private CadastroContas dbContas;  // Banco de dados das contas
	private Caixa caixa;


	//Operacoes

	public ControladorCaixa(int senhaCaixa) {
		dbContas = new CadastroContas();
		caixa = new Caixa(senhaCaixa);
	}

	
	public float consultarSaldo (int num, int senha){
		ContaCor cta;
		cta = dbContas.buscarConta(num); // obtem referencia para o objeto que representa a conta 'num'
		if (cta==null)   // se numero de conta invalido ...
			return -1; // ... retorna -1 
		else             // caso contrario ... 
			return cta.obterSaldo(senha); // efetua consulta 
	} 

	/**
	 * Saques permitidos deve ser menor ou igual que o saldo disponível no caixa.
	 * @param num numero da conta corrente
	 * @param senha senha do cliente 
	 * @param val valor que sera retirado da conta
	 * @return true se o numero da conta e a senha estiverem corretos e se o valor a ser retirado esta disponível
	 * e é menor que o saldo atual. Caso contrário, retorna false.
	 */
	public boolean efetuarSaque (int num, int senha, float val){

		ContaCor cta;

		float saldoCaixa = this.caixa.obterSaldoCaixa();

		if (saldoCaixa < val ){
			System.out.println("O caixa nao possui R$"+val+" e precisa ser recarregado.");
			return false;
		}

		cta=dbContas.buscarConta(num);  // obtem a referencia para o objeto que representa a conta 'num'

		if (cta==null)  // se número de conta inválido ...
			return false;  // ... retorna false

		if (!cta.debitarSaque(val, senha)) // se saque recusado ...
			return false;  // retorna false
		else{
			this.caixa.liberarNotas((int)(val/10)); // libera pagamento
			return true;
		}


	}



	public void recarregar(int senha){
		this.caixa.recarregar(senha);
	}

	public boolean validarSenha(int senha){
		return this.caixa.validarSenha(senha);
	}



	public void alternarModo(int senhaCaixa){
		this.caixa.alternarModo(senhaCaixa);
	}

	public int obterModoOperacaoAtual(){
		return this.caixa.obterModoOperacaoAtual();
	}

	private boolean transferenciaValida(ContaCor contaCorrenteOrigem, int senhaOrigem, ContaCor contaCorrenteDestino, float valor, float saldoCaixa) {
		if (saldoCaixa < valor) {
			System.out.println("Saldo do caixa insuficiente");
			return false;
		}
		if (contaCorrenteOrigem == null){
			System.out.println("Conta de Origem inválida");
			return false;
		}
		if (contaCorrenteOrigem.obterSaldo(senhaOrigem) < valor){
			System.out.println("Saldo insuficiente");
			return false;
		}
		if (contaCorrenteDestino == null){
			System.out.println("Conta de destino inválida");
			return false;
		}
		return true;
	}

	public boolean efetuarTransferencia(int contaOrigem, int senhaOrigem, int contaDestino, float valor){
		float saldoCaixa = this.caixa.obterSaldoCaixa();
		ContaCor contaCorrenteOrigem = dbContas.buscarConta(contaOrigem);
		ContaCor contaCorrenteDestino = dbContas.buscarConta(contaDestino);
		if (this.transferenciaValida(contaCorrenteOrigem,senhaOrigem,contaCorrenteDestino,valor,saldoCaixa)){
			boolean taxaCobradaOrigem = contaCorrenteOrigem.aplicarCobranca();
			if (!taxaCobradaOrigem) {return false;}
			boolean debitou = contaCorrenteOrigem.debitarValor("transferencia para a conta de numero :" + contaDestino,valor);
			if (!debitou) {return false;}
			boolean taxaCobradaDestino = contaCorrenteDestino.aplicarCobranca();
			if (!taxaCobradaDestino) {
				contaCorrenteOrigem.creditarValor(contaOrigem,valor);
				return false;
			}
			boolean creditou = contaCorrenteDestino.creditarValor(contaOrigem,valor);
			if (creditou) {
				System.out.println("transferencia para a conta de numero :" + contaDestino+ "no valor de "+valor+ "realizada!");
				return true;
			}
			else {
				System.out.println("Não foi possivel creditar na conta de destino");
				contaCorrenteOrigem.creditarValor(contaOrigem,valor);
				contaCorrenteOrigem.creditarValor(contaOrigem,contaCorrenteDestino.getTaxaOperacao());
				return false;
			}
		}
		return false;
	}
}