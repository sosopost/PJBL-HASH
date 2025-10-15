import java.util.*;
public class ResultadoExperimento {
    private String tipoHash;
    private int tamanhoTabela;
    private int tamanhoDados;
    private long tempoInsercao;
    private long tempoBusca;
    private int colisoes;
    private double fatorCarga;
    private int maiorLista;
    private int menorGap;
    private int maiorGap;
    private double mediaGaps;
    private List<String> tresMaioresListas;

    public ResultadoExperimento(String tipoHash, int tamanhoTabela, int tamanhoDados) {
        this.tipoHash = tipoHash;
        this.tamanhoTabela = tamanhoTabela;
        this.tamanhoDados = tamanhoDados;
        this.tresMaioresListas = new ArrayList<>();
    }

    // Getters e Setters
    public String getTipoHash() { return tipoHash; }
    public int getTamanhoTabela() { return tamanhoTabela; }
    public int getTamanhoDados() { return tamanhoDados; }
    public long getTempoInsercao() { return tempoInsercao; }
    public void setTempoInsercao(long tempo) { this.tempoInsercao = tempo; }
    public long getTempoBusca() { return tempoBusca; }
    public void setTempoBusca(long tempo) { this.tempoBusca = tempo; }
    public int getColisoes() { return colisoes; }
    public void setColisoes(int colisoes) { this.colisoes = colisoes; }
    public double getFatorCarga() { return fatorCarga; }
    public void setFatorCarga(double fator) { this.fatorCarga = fator; }
    public int getMaiorLista() { return maiorLista; }
    public void setMaiorLista(int maior) { this.maiorLista = maior; }
    public int getMenorGap() { return menorGap; }
    public void setMenorGap(int menor) { this.menorGap = menor; }
    public int getMaiorGap() { return maiorGap; }
    public void setMaiorGap(int maior) { this.maiorGap = maior; }
    public double getMediaGaps() { return mediaGaps; }
    public void setMediaGaps(double media) { this.mediaGaps = media; }
    public List<String> getTresMaioresListas() { return tresMaioresListas; }
    public void setTresMaioresListas(List<String> listas) { this.tresMaioresListas = listas; }

    public String toCSV() {
        return String.format("%s,%d,%d,%d,%d,%d,%.4f,%d,%d,%d,%.2f,\"%s\"",
                tipoHash, tamanhoTabela, tamanhoDados, tempoInsercao, tempoBusca,
                colisoes, fatorCarga, maiorLista, menorGap, maiorGap, mediaGaps,
                String.join("; ", tresMaioresListas));
    }

    public static String getCSVHeader() {
        return "TipoHash,TamanhoTabela,TamanhoDados,TempoInsercao,TempoBusca,Colisoes,FatorCarga,MaiorLista,MenorGap,MaiorGap,MediaGaps,TresMaioresListas";
    }
    
    @Override
    public String toString() {
        return String.format(
            "Tipo: %s | Tabela: %d | Dados: %d | Inserção: %dms | Busca: %dms | " +
            "Colisões: %d | Fator: %.4f",
            tipoHash, tamanhoTabela, tamanhoDados, tempoInsercao, tempoBusca,
            colisoes, fatorCarga
        );
    }
}