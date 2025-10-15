public class DoubleHashingHashTable implements HashTable {
    private Registro[] tabela;
    private int colisoes;
    private int tamanho;

    public DoubleHashingHashTable(int capacidade) {
        this.tabela = new Registro[capacidade];
        this.colisoes = 0;
        this.tamanho = 0;
    }

    private int hash1(String codigo) {
        // Primeira função hash: método da divisão simples
        int hash = 0;
        for (int i = 0; i < codigo.length(); i++) {
            hash = (hash + (codigo.charAt(i) - '0')) & 0x7FFFFFFF;
        }
        return hash % tabela.length;
    }

    private int hash2(String codigo) {
        // Segunda função hash: importante que seja diferente e não dê 0
        // Usando um primo menor que o tamanho da tabela
        int hash = 1;
        for (int i = 0; i < codigo.length(); i++) {
            hash = (hash * 7 + (codigo.charAt(i) - '0')) & 0x7FFFFFFF;
        }
        int step = 1 + (hash % (tabela.length - 1));
        return step;
    }

    @Override
    public void inserir(Registro registro) {
        int h1 = hash1(registro.getCodigo());
        int h2 = hash2(registro.getCodigo());
        int indice = h1;
        int tentativas = 0;

        while (tabela[indice] != null && tentativas < tabela.length) {
            if (tabela[indice].equals(registro)) {
                return;
            }
            colisoes++;
            tentativas++;
            indice = (h1 + tentativas * h2) % tabela.length;
            if (indice < 0) {
                indice += tabela.length;
            }
            indice = indice % tabela.length;
        }

        if (tentativas < tabela.length) {
            tabela[indice] = registro;
            tamanho++;
        }
    }

    @Override
    public boolean buscar(Registro registro) {
        int h1 = hash1(registro.getCodigo());
        int h2 = hash2(registro.getCodigo());
        int indice = h1;
        int tentativas = 0;

        while (tabela[indice] != null && tentativas < tabela.length) {
            if (tabela[indice].equals(registro)) {
                return true;
            }
            tentativas++;
            indice = (h1 + tentativas * h2) % tabela.length;
            if (indice < 0) {
                indice += tabela.length;
            }
            indice = indice % tabela.length;
        }

        return false;
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
        System.out.println("** Estatísticas Double Hashing **");
        System.out.println("Fator de carga: " + String.format("%.4f", getFatorCarga()));

        int menorGap = Integer.MAX_VALUE;
        int maiorGap = 0;
        long somaGaps = 0;
        int gapsCount = 0;
        int ultimaPosicaoOcupada = -1;

        for (int i = 0; i < tabela.length; i++) {
            if (tabela[i] != null) {
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
    
    @Override
    public int[] getGaps() {
        int menorGap = Integer.MAX_VALUE;
        int maiorGap = 0;
        long somaGaps = 0;
        int gapsCount = 0;
        int ultimaPosicaoOcupada = -1;

        for (int i = 0; i < tabela.length; i++) {
            if (tabela[i] != null) {
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