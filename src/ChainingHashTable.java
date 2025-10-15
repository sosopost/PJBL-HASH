import java.util.*;

public class ChainingHashTable implements HashTable {
    private LinkedList<Registro>[] tabela;
    private int colisoes;
    private int tamanho;

    public ChainingHashTable(int capacidade) {
        this.tabela = new LinkedList[capacidade];
        this.colisoes = 0;
        this.tamanho = 0;

        for (int i = 0; i < capacidade; i++) {
            tabela[i] = new LinkedList<>();
        }
    }

    private int funcaoHash(String codigo) {
        // Função hash por divisão com multiplicação (método mais sofisticado)
        int hash = 0;
        for (int i = 0; i < codigo.length(); i++) {
            hash = (hash * 37 + (codigo.charAt(i) - '0')) & 0x7FFFFFFF;
        }
        return hash % tabela.length;
    }

    @Override
    public void inserir(Registro registro) {
        int indice = funcaoHash(registro.getCodigo());

        // Conta colisões baseado no critério do trabalho:
        // Para encontrar posição vazia, cada elemento verificado = 1 colisão
        if (!tabela[indice].isEmpty()) {
            colisoes += tabela[indice].size(); // Colisões = elementos já na lista
        }

        if (!tabela[indice].contains(registro)) {
            tabela[indice].add(registro);
            tamanho++;
        }
    }

    @Override
    public boolean buscar(Registro registro) {
        int indice = funcaoHash(registro.getCodigo());
        return tabela[indice].contains(registro);
    }

    @Override
    public int getColisoes() {
        return colisoes;
    }

    @Override
    public int getTamanho() {
        return tamanho;
    }

    @Override
    public double getFatorCarga() {
        return (double) tamanho / tabela.length;
    }

    @Override
    public void estatisticas() {
        int listasVazias = 0;
        int maiorLista = 0;
        int totalElementos = 0;
        int listasNaoVazias = 0;
        PriorityQueue<Integer> maioresListas = new PriorityQueue<>(Collections.reverseOrder());

        for (LinkedList<Registro> lista : tabela) {
            if (lista.isEmpty()) {
                listasVazias++;
            } else {
                int tamanhoLista = lista.size();
                maiorLista = Math.max(maiorLista, tamanhoLista);
                totalElementos += tamanhoLista;
                listasNaoVazias++;
                maioresListas.add(tamanhoLista);
            }
        }

        System.out.println("** Estatísticas Encadeamento **");
        System.out.println("Listas vazias: " + listasVazias + " (" +
                String.format("%.2f", listasVazias * 100.0 / tabela.length) + "%)");
        System.out.println("Maior lista: " + maiorLista);

        if (listasNaoVazias > 0) {
            System.out.println("Média elementos por lista não vazia: " +
                    String.format("%.2f", (double) totalElementos / listasNaoVazias));
        }

        System.out.println("3 maiores listas:");
        for (int i = 0; i < 3 && !maioresListas.isEmpty(); i++) {
            System.out.println("  - " + maioresListas.poll() + " elementos");
        }
        
        // Calcular gaps entre listas não vazias
        int menorGap = Integer.MAX_VALUE;
        int maiorGap = 0;
        long somaGaps = 0;
        int gapsCount = 0;
        int ultimaPosicaoOcupada = -1;

        for (int i = 0; i < tabela.length; i++) {
            if (!tabela[i].isEmpty()) {
                if (ultimaPosicaoOcupada != -1) {
                    int gap = i - ultimaPosicaoOcupada - 1;
                    menorGap = Math.min(menorGap, gap);
                    maiorGap = Math.max(maiorGap, gap);
                    somaGaps += gap;
                    gapsCount++;
                }
                ultimaPosicaoOcupada = i;
            }
        }

        if (gapsCount > 0) {
            double mediaGaps = (double) somaGaps / gapsCount;
            System.out.println("Menor gap: " + menorGap);
            System.out.println("Maior gap: " + maiorGap);
            System.out.println("Média gaps: " + String.format("%.2f", mediaGaps));
        } else {
            System.out.println("Sem gaps para calcular");
        }
    }
    
    // Métodos para extrair estatísticas para o relatório
    public int getMaiorLista() {
        int maiorLista = 0;
        for (LinkedList<Registro> lista : tabela) {
            maiorLista = Math.max(maiorLista, lista.size());
        }
        return maiorLista;
    }
    
    public List<Integer> getTresMaioresListas() {
        PriorityQueue<Integer> maioresListas = new PriorityQueue<>(Collections.reverseOrder());
        for (LinkedList<Registro> lista : tabela) {
            if (!lista.isEmpty()) {
                maioresListas.add(lista.size());
            }
        }
        
        List<Integer> resultado = new ArrayList<>();
        for (int i = 0; i < 3 && !maioresListas.isEmpty(); i++) {
            resultado.add(maioresListas.poll());
        }
        return resultado;
    }
    
    public int[] getGaps() {
        // Retorna [menorGap, maiorGap, mediaGaps]
        int menorGap = Integer.MAX_VALUE;
        int maiorGap = 0;
        long somaGaps = 0;
        int gapsCount = 0;
        int ultimaPosicaoOcupada = -1;

        for (int i = 0; i < tabela.length; i++) {
            if (!tabela[i].isEmpty()) {
                if (ultimaPosicaoOcupada != -1) {
                    int gap = i - ultimaPosicaoOcupada - 1;
                    menorGap = Math.min(menorGap, gap);
                    maiorGap = Math.max(maiorGap, gap);
                    somaGaps += gap;
                    gapsCount++;
                }
                ultimaPosicaoOcupada = i;
            }
        }
        
        if (gapsCount == 0) {
            return new int[]{0, 0, 0};
        }
        
        return new int[]{menorGap, maiorGap, (int)(somaGaps / gapsCount)};
    }
}