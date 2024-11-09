package sistemaCaixaAutomatico;

public class CobrancaPacoteBasico implements EstrategiaDeCobranca {
    private float MENSALIDADE = 9.99F;
    private int  MAX_OPERACOES = 10;
    private float TAXA_POR_OPERACAO = 3.99F;
    private int senha;
    private int contagemOperacoes;

    public CobrancaPacoteBasico(int senha){
        this.contagemOperacoes=0;
        this.senha = senha;
    }

    @Override
    public float getTaxa(){
        return this.TAXA_POR_OPERACAO;
    }

    @Override
    public boolean aplicarCobranca(ContaCor conta) {
        if (MAX_OPERACOES < this.getContagemOperacoes()) {
            System.out.println("Operacao sem cobranca");
            this.incrementarContagemOperacoes();
            return true;
        } else {
            if (conta.obterSaldo(this.senha) > TAXA_POR_OPERACAO) {
                conta.debitarValor("taxa de operacao", TAXA_POR_OPERACAO);
                System.out.println("Aplicando cobrança para o Pacote Básico.");
                this.incrementarContagemOperacoes();
                return true;
            }
            else {
                System.out.println("Não foi possível debitar a taxa de cobrança");
                return false;
            }
        }
    }

    private void incrementarContagemOperacoes(){
            this.contagemOperacoes += 1;
        }

    private int getContagemOperacoes(){
        return this.contagemOperacoes;
    }
}
