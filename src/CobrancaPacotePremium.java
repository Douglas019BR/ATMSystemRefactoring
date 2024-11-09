package sistemaCaixaAutomatico;

public class CobrancaPacotePremium implements EstrategiaDeCobranca {
    private float MENSALIDADE = 9.99F;
    private int senha;

    public CobrancaPacotePremium(int senha){
        this.senha = senha;
    }

    @Override
    public float getTaxa(){
        return 0.0F;
    }

    @Override
    public boolean aplicarCobranca(ContaCor conta) {
        System.out.println("Aplicando cobrança para o Pacote Básico.");
        return true;
    }
}
