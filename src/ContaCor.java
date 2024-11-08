package sistemaCaixaAutomatico;

public class ContaCor {

	public static int ATIVA = 1;
	public static int ENCERRADA = 2;
	public static int QTDMAXLANC = 10;

	private int estado;  		  // Ativa ou Encerrada
	private String titular; 	  // nome do titular
	private int numConta;		  // número da conta
	private int senha;			  // senha
	private float saldoAnterior;  // saldo anterior
	private String historico[];   // históricos e
	private float valorLanc[];	  /* valores dos últimos lançamentos > 0 p/ créditos; < 0 p/ débitos.
	Nessa versão do código, a opção de crédito não foi implementada */
	private int ultLanc;		  // topo dos vetores acima
	private float saldoAtual;     // saldo atual da conta

	public ContaCor(String titular, float saldoAtual, int numConta, int senha) {
		this.estado = ContaCor.ATIVA; 		// A conta se torna ativa, ppodendo receber lançamentos.
		this.titular = titular;
		this.saldoAtual = saldoAtual;
		this.numConta = numConta;
		this.senha = senha;
		this.ultLanc = 0; 					// A conta sem nenhum lançamento.
		this.historico = new String[ContaCor.QTDMAXLANC]; 		// cria vetores ...
		this.valorLanc = new float[ContaCor.QTDMAXLANC];		// ... com QTDMAXLANC elementos
	}

	private boolean estaAtiva() {
		return estado == ContaCor.ATIVA;
	}

	public float obterSaldo(int senhaCliente) {
		//A conta deve estar ativa
		if (!this.estaAtiva()){
			System.out.println("Conta inativa");
			return (-1);
		}
		//A senha de entrada deve ser igual à senha da conta
		if (senha!=senhaCliente){
			System.out.println("Senha incorreta");
			return (-1);
		}
		return (saldoAtual);		// retorna o saldo atual
	}

	/**
	 * Gerencia o histórico de lançamentos da conta, mantendo apenas os QTDMAXLANC últimos registros
	 * @param descricao Descrição da operação
	 * @param valor Valor da operação (positivo para crédito, negativo para débito)
	 */
	private void gerenciarHistorico(String descricao, float valor) {
		if (ultLanc == (ContaCor.QTDMAXLANC - 1)) {
			for(int i = 0; i < (ContaCor.QTDMAXLANC - 1); i++) {
				this.historico[i] = this.historico[i+1];
				this.valorLanc[i] = this.valorLanc[i+1];
			}
		} else {
			ultLanc++;
		}

		this.historico[ultLanc] = descricao;
		this.valorLanc[ultLanc] = valor;
	}

	/**
	 * Realiza um débito genérico na conta corrente.
	 * @param descricao Descrição da operação
	 * @param valor Valor a ser debitado (deve ser positivo)
	 * @param senhaCliente Senha do cliente para autorização
	 * @return true se o débito for realizado com sucesso, false caso contrário
	 */
	public boolean debitarValor(String descricao, float valor, int senhaCliente) {
		gerenciarHistorico(descricao, -valor);
		saldoAnterior = this.saldoAtual;
		this.saldoAtual -= valor;

		verificarEncerramentoConta();
		return true;
	}

	private void verificarEncerramentoConta() {
		if (saldoAtual == 0) {
			estado = ContaCor.ENCERRADA;
			System.out.println("Conta de " + this.titular + ", número " + this.numConta + " foi encerrada.");
		}
	}

	/**
	 * Realiza um crédito genérico na conta corrente.
	 * @param contaOrigem número da conta de origem
	 * @param valor Valor a ser creditado (deve ser positivo)
	 * @return true se o crédito for realizado com sucesso, false caso contrário
	 */
	public boolean creditarValor(int contaOrigem, float valor) {
		String descricao;
		if (contaOrigem == this.numConta) {
			descricao = "Extorno no valor R$" + valor;
			System.out.println(descricao);
		} else{
			descricao = "Transferencia recebida da conta " + contaOrigem + " no valor R$" + valor;
			System.out.println(descricao);
		}
		gerenciarHistorico(descricao, valor);
		saldoAnterior = this.saldoAtual;
		this.saldoAtual += valor;
		return true;
	}

	private boolean saqueValido(int senhaCliente, float valor){
		if (!this.estaAtiva()) {
			System.out.println("Conta Inativa");
			return false;
		}

		if (senha != senhaCliente) {
			System.out.println("Senha incorreta");
			return false;
		}

		if (valor <= 0 || valor > this.obterSaldo(senha)) {
			System.out.println("Valor de débito inválido: " + valor);
			return false;
		}

		if (valor > 200) {
			System.out.println("Valor de saque excede o limite de R$ 200,00");
			return false;
		}

		if (valor % 10 != 0) {
			System.out.println("Valor de saque deve ser múltiplo de R$ 10,00");
			return false;
		}
		return true;
	}

	/**
	 * Realiza um saque na conta corrente.
	 * @param valor o valor do saque deve ser: (i) maior que zero; (ii) menor ou igual a R$200,00;
	 *             (iii) múltiplo de 10; (iv) menor ou igual que o saldo do cliente.
	 * @param senhaCliente Senha do cliente para autorização
	 * @return true se o saque for realizado com sucesso, false caso contrário
	 */
	public boolean debitarSaque(float valor, int senhaCliente) {
		if (!this.saqueValido(senhaCliente,valor)) { return false;}
		boolean sucesso = debitarValor("Saque em dinheiro", valor, senhaCliente);
		if (sucesso) {
			System.out.println("Saque realizado com sucesso no valor de: " + valor);
		}
		return sucesso;
	}
}
