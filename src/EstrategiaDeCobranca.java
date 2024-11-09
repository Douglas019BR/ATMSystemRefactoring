package sistemaCaixaAutomatico;

public interface EstrategiaDeCobranca {
    boolean aplicarCobranca(ContaCor conta);

    float getTaxa();
}
