package sistemaCaixaAutomatico;

public class CobrancaPorOperacao implements EstrategiaDeCobranca {
    private float TAXA_POR_OPERACAO = 6.99F;
    private int senha;

    public CobrancaPorOperacao(int senha){
        this.senha = senha;
    }

    @Override
    public float getTaxa(){
        return this.TAXA_POR_OPERACAO;
    }

    @Override
    public boolean aplicarCobranca(ContaCor conta) {
        if (conta.obterSaldo(this.senha) > TAXA_POR_OPERACAO) {
            conta.debitarValor("taxa de operacao", TAXA_POR_OPERACAO);
            System.out.println("Aplicando cobrança para o Pacote Básico.");
            return true;
        } else {
            System.out.println("Não foi possível debitar a taxa de cobrança");
            return false;
        }
    }
}
