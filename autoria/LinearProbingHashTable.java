public class LinearProbingHashTable implements HashTable {
    private Registro[] tabela;
    private int colisoes;
    private int tamanho;
    private static final Registro DELETED = new Registro("000000000");

    public LinearProbingHashTable(int capacidade) {
        this.tabela = new Registro[capacidade];
        this.colisoes = 0;
        this.tamanho = 0;
    }

    private int funcaoHash(String codigo) {
        long hash = 0;
        long A = 2654435761L;
        for (int i = 0; i < codigo.length(); i++) {
            hash = hash * A + (codigo.charAt(i) - '0');
        }
        return (int)((hash & 0x7FFFFFFF) % tabela.length);
    }

    private int rehash(int indice) {
        return (indice + 1) % tabela.length;
    }

    @Override
    public void inserir(Registro registro) {
        int indice = funcaoHash(registro.getCodigo());
        int tentativas = 0;

        while (tabela[indice] != null && tabela[indice] != DELETED && tentativas < tabela.length) {
            if (tabela[indice].equals(registro)) {
                return;
            }
            colisoes++;
            indice = rehash(indice);
            tentativas++;
        }

        if (tentativas < tabela.length) {
            tabela[indice] = registro;
            tamanho++;
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
            indice = rehash(indice);
            tentativas++;
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
        System.out.println("** Estatísticas Linear Probing **");
        System.out.println("Fator de carga: " + String.format("%.4f", getFatorCarga()));

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
