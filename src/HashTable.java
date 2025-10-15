import java.util.*;

public interface HashTable {
    void inserir(Registro registro);
    boolean buscar(Registro registro);
    int getColisoes();
    int getTamanho();
    void estatisticas();
    double getFatorCarga();
    
    // Métodos para extrair estatísticas detalhadas
    default int getMaiorLista() { return 0; }
    default List<Integer> getTresMaioresListas() { return new ArrayList<>(); }
    default int[] getGaps() { return new int[]{0, 0, 0}; }
}