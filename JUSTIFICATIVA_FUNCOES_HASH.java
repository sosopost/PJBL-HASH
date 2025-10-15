/*
 * JUSTIFICATIVA DAS ESCOLHAS DE FUNÇÕES HASH
 * ===========================================
 * 
 * Este arquivo documenta as decisões tomadas na implementação das funções hash
 * para cada tipo de tabela, explicando as razões técnicas por trás de cada escolha.
 */

/**
 * 1. ENCADEAMENTO - Função Hash por Multiplicação
 * 
 * Escolha: hash = (hash * 37 + digito) % tamanho
 * 
 * Razões:
 * - O primo 37 oferece boa distribuição para códigos numéricos
 * - Multiplicação distribui melhor que simples soma
 * - Não há restrições de fator de carga para encadeamento
 * - Performance constante O(1) amortizado para inserção
 * 
 * Vantagens:
 * - Nunca falha por overflow da tabela
 * - Distribuição uniforme para dados sequenciais
 * - Simples de implementar e entender
 * 
 * Aplicação ideal:
 * - Quando o número de elementos é imprevisível
 * - Quando há muitas inserções dinâmicas
 * - Quando a memória não é limitação crítica
 */

/**
 * 2. LINEAR PROBING - Função Hash Multiplicativa de Knuth
 * 
 * Escolha: hash = (hash * A + digito) onde A = (√5 - 1)/2 * 2^32
 * 
 * Razões:
 * - A constante áurea oferece distribuição matemática ótima
 * - Complementa bem o linear probing que precisa de uniformidade
 * - Reduz agrupamento primário na posição inicial
 * - Método comprovado por Knuth para multiplicação
 * 
 * Vantagens:
 * - Excelente localidade de cache (elementos próximos)
 * - Implementação simples do rehashing
 * - Performance muito boa para fatores baixos (< 0.5)
 * 
 * Desvantagens:
 * - Agrupamento primário inevitável
 * - Degradação rápida com aumento do fator de carga
 * 
 * Aplicação ideal:
 * - Dados com bom controle de fator de carga
 * - Quando cache locality é importante
 * - Sistemas com memória limitada
 */

/**
 * 3. QUADRATIC PROBING - Função Hash Polinomial (Horner)
 * 
 * Escolha: hash = (hash * 31 + digito) usando regra de Horner
 * 
 * Razões:
 * - O primo 31 é otimizado em muitos processadores (shift + subtract)
 * - Horner é eficiente para avaliação polinomial
 * - Distribui bem códigos alfanuméricos e numéricos
 * - Complementa quadratic probing que evita agrupamento primário
 * 
 * Vantagens:
 * - Evita agrupamento primário do linear probing
 * - Boa distribuição inicial reduz clusters
 * - Performance melhor que linear para fatores médios
 * 
 * Desvantagens:
 * - Agrupamento secundário ainda pode ocorrer
 * - Pode não encontrar posição livre mesmo com espaço
 * 
 * Aplicação ideal:
 * - Quando linear probing está gerando muito agrupamento
 * - Fator de carga médio (0.3 - 0.7)
 * - Código numérico sequencial (como CPF, códigos de barra)
 */

/**
 * 4. DOUBLE HASHING - Duas Funções Complementares
 * 
 * Primeira: hash1 = soma dos dígitos % tamanho
 * Segunda:  hash2 = 1 + ((multiplicação * 7) % (tamanho-1))
 * 
 * Razões para hash1 (simples):
 * - Distribuição básica mas eficiente
 * - Não precisa ser perfeita, pois hash2 compensa
 * - Soma simples é rápida de calcular
 * 
 * Razões para hash2 (multiplicativa):
 * - DEVE retornar valor >= 1 (nunca zero)
 * - Primo 7 garante boa distribuição do step
 * - % (tamanho-1) evita step igual ao tamanho
 * - Multiplicação oferece variação suficiente
 * 
 * Vantagens:
 * - Melhor distribuição estatística de todas
 * - Evita agrupamento primário E secundário
 * - Excelente para fatores médios-altos
 * - Sequência de probes quasi-aleatória
 * 
 * Desvantagens:
 * - Duas funções hash = mais tempo de CPU
 * - Implementação mais complexa
 * - Hash2 nunca pode retornar 0
 * 
 * Aplicação ideal:
 * - Quando qualidade da distribuição é crítica
 * - Sistema com CPU suficiente para duas funções
 * - Dados com padrões difíceis de prever
 * - Necessidade de minimizar colisões
 */

/**
 * ESCOLHAS ESPECÍFICAS PARA CÓDIGOS DE 9 DÍGITOS
 * ==============================================
 * 
 * Características dos dados:
 * - Códigos numéricos de 9 dígitos (000000001 a 999999999)
 * - Podem ter padrões sequenciais
 * - Distribuição pode não ser uniforme
 * 
 * Estratégias adotadas:
 * 
 * 1. Multiplicação por primos:
 *    - Quebra padrões sequenciais
 *    - Primos pequenos (7, 31, 37) são eficientes
 * 
 * 2. Operações bit-wise:
 *    - & 0x7FFFFFFF remove bit de sinal
 *    - Evita índices negativos
 * 
 * 3. Diferentes abordagens:
 *    - Cada método usa estratégia diferente
 *    - Permite comparação justa
 *    - Nenhuma função "favorecida"
 * 
 * 4. Módulo final:
 *    - Sempre % tamanho_tabela
 *    - Garante índice válido
 *    - Mantém distribuição proporcional
 */

/**
 * CRITÉRIO DE COLISÕES IMPLEMENTADO
 * =================================
 * 
 * Conforme especificação do trabalho:
 * 
 * Encadeamento:
 * "Para encontrar o null para inserir, seriam X colisões"
 * - Cada elemento já na lista = 1 colisão
 * - Inserir em lista com 3 elementos = 3 colisões
 * 
 * Rehashing:
 * - Cada probe/tentativa = 1 colisão
 * - Inserir na 4ª tentativa = 3 colisões
 * 
 * Esta implementação segue exatamente o critério especificado.
 */