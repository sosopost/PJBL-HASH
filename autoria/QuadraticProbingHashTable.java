public class QuadraticProbingHashTable implements HashTable {
    private Registro[] tabela;
    private int colisoes;
    private int tamanho;
    private static final Registro DELETED = new Registro("000000000");

    public QuadraticProbingHashTable(int capacidade) {
        this.tabela = new Registro[capacidade];
        this.colisoes = 0;
        this.tamanho = 0;
    }

    private int funcaoHash(String codigo) {
        // Função hash polinomial (método de Horner)
        int hash = 0;
        int p = 31; // primo pequeno
        for (int i = 0; i < codigo.length(); i++) {
            hash = (hash * p + (codigo.charAt(i) - '0')) & 0x7FFFFFFF;
        }
        return hash % tabela.length;
    }

    private int rehashQuadratico(int indice, int tentativa) {
        // i = (h(k) + c1 * i + c2 * i²) mod m
        // Usando c1 = 1, c2 = 1 (valores comuns)
        return (indice + tentativa + tentativa * tentativa) % tabela.length;
    }

    @Override
    public void inserir(Registro registro) {
        int indice = funcaoHash(registro.getCodigo());
        int tentativas = 0;

        while (tabela[indice] != null && tabela[indice] != DELETED && tentativas < tabela.length) {
            if (tabela[indice].equals(registro)) {
                return; // Elemento já existe
            }
            colisoes++;
            tentativas++;
            indice = rehashQuadratico(indice, tentativas);
            if (indice < 0) {
                indice += tabela.length;
            }
            indice = indice % tabela.length;
        }

        if (tentativas < tabela.length) {
            tabela[indice] = registro;
            tamanho++;
        } else {
            System.out.println("AVISO: Não foi possível inserir após " + tentativas + " tentativas");
        }
    }

    @Override
    public boolean buscar(Registro registro) {
        int indice = funcaoHash(registro.getCodigo());
        int tentativas = 0;

        while (tabela[indice] != null && tentativas < tabela.length) {
            if (tabela[indice] != DELETED && tabela[indice].equals(registro)) {
                return true;
            }
            tentativas++;
            indice = rehashQuadratico(indice, tentativas);
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
        System.out.println("** Estatísticas Quadratic Probing **");
        System.out.println("Fator de carga: " + String.format("%.4f", getFatorCarga()));
        System.out.println("Colisões: " + colisoes);

        int menorGap = Integer.MAX_VALUE;
        int maiorGap = 0;
        long somaGaps = 0;
        int gapsCount = 0;
        int ultimaPosicaoOcupada = -1;

        for (int i = 0; i < tabela.length; i++) {
            if (tabela[i] != null && tabela[i] != DELETED) {
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

        // Estatística específica do Quadratic: sequência de probes
        System.out.println("Sequência de probes para primeiro elemento:");
        int primeiroIndice = funcaoHash("000000001");
        System.out.print("  " + primeiroIndice);
        for (int i = 1; i <= 5; i++) {
            int probe = rehashQuadratico(primeiroIndice, i);
            System.out.print(" → " + probe);
        }
        System.out.println();
    }
    
    @Override
    public int[] getGaps() {
        int menorGap = Integer.MAX_VALUE;
        int maiorGap = 0;
        long somaGaps = 0;
        int gapsCount = 0;
        int ultimaPosicaoOcupada = -1;

        for (int i = 0; i < tabela.length; i++) {
            if (tabela[i] != null && tabela[i] != DELETED) {
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