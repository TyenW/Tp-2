#define _XOPEN_SOURCE 700   // Deve ser a PRIMEIRA linha

/* Includes */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <time.h>
#include <stdbool.h>

/* Constantes */
#define MAX_SHOWS 1369
#define MAX_LINE 2000
#define MAX_ARRAY 20

/* Variáveis Globais */
int contagem = 0;  // Variável global acessível em todo o programa


// Estrutura para armazenar informações de um show
typedef struct {
    char id[20]; // ID do show
    char titulo[200]; // Título do show
    char tipo[20]; // Tipo do show
    char diretores[MAX_ARRAY][100]; // Lista de diretores
    int qtdDiretores; // Quantidade de diretores
    char elenco[MAX_ARRAY][100]; // Lista de elenco
    int qtdElenco; // Quantidade de membros do elenco
    char pais[100]; // País de origem
    struct tm dataAdicionado; // Data em que foi adicionado
    int anoLancamento; // Ano de lançamento
    char classificacao[10]; // Classificação indicativa
    char duracao[20]; // Duração do show
    char categorias[MAX_ARRAY][100]; // Lista de categorias
    int qtdCategorias; // Quantidade de categorias
} Show;


// Função para ordenar uma lista de strings em ordem crescente
void ordenarListaCrescente(char arr[MAX_ARRAY][100], int qtd) {
    char temp[100];
    for (int i = 0; i < qtd - 1; i++) {
        for (int j = i + 1; j < qtd; j++) {
            if (strcmp(arr[i], arr[j]) > 0) { // Comparação para ordem crescente
                strcpy(temp, arr[i]);
                strcpy(arr[i], arr[j]);
                strcpy(arr[j], temp);
            }
        }
    }
}

// Função para ordenar uma lista de strings em ordem decrescente
void ordenarListaDecrescente(char arr[MAX_ARRAY][100], int qtd) {
    char temp[100];
    for (int i = 0; i < qtd - 1; i++) {
        for (int j = i + 1; j < qtd; j++) {
            if (strcmp(arr[i], arr[j]) < 0) { // Comparação para ordem decrescente
                strcpy(temp, arr[i]);
                strcpy(arr[i], arr[j]);
                strcpy(arr[j], temp);
            }
        }
    }
}

// Função para remover espaços extras no início e no final de uma string
void removerEspacos(char *str) {
    if (!str || *str == '\0') return;

    // Remove espaços no início
    char *inicio = str;
    while (isspace((unsigned char)*inicio)) inicio++;

    // Remove espaços no final
    char *fim = inicio + strlen(inicio) - 1;
    while (fim >= inicio && isspace((unsigned char)*fim)) fim--;
    *(fim + 1) = '\0';

    // Move a string para o início original, se necessário
    if (inicio != str) {
        memmove(str, inicio, strlen(inicio) + 1);
    }
}

// Função para dividir uma string em uma lista de strings
void dividirLista(char *src, char arr[MAX_ARRAY][100], int *qtd) {
    *qtd = 0;

    char *token = strtok(src, ",");
    while (token && *qtd < MAX_ARRAY) {
        removerEspacos(token);
        strcpy(arr[*qtd], token);
        (*qtd)++;
        token = strtok(NULL, ",");
    }

    // Ordenar em ordem alfabética
    for (int i = 0; i < *qtd - 1; i++) {
        for (int j = i + 1; j < *qtd; j++) {
            if (strcmp(arr[i], arr[j]) > 0) {
                char temp[100];
                strcpy(temp, arr[i]);
                strcpy(arr[i], arr[j]);
                strcpy(arr[j], temp);
            }
        }
    }
}

// Função para ordenar uma lista de strings em ordem crescente
void ordenarListatitulorecursivo(Show arr[], int qtd) {
    if (qtd <= 1) return;

    // Encontra o índice do menor título
    int menorIndice = 0;
    for (int i = 1; i < qtd; i++) {
        contagem++;
        if (strcmp(arr[i].titulo, arr[menorIndice].titulo) < 0) {
            menorIndice = i;
        }
    }

    // Troca o menor título com o primeiro, se necessário
    if (menorIndice != 0) {
        Show temp = arr[0];
        arr[0] = arr[menorIndice];
        arr[menorIndice] = temp;
    }

    // Chama recursivamente para o restante da lista
    ordenarListatitulorecursivo(&arr[1], qtd - 1);
}

// Função para ordenar usando Shellsort com chave type e critério de desempate title
void shellsortPorTipoETitulo(Show arr[], int qtd) {
    for (int gap = qtd / 2; gap > 0; gap /= 2) {
        for (int i = gap; i < qtd; i++) {
            Show temp = arr[i];
            int j;
            for (j = i; j >= gap; j -= gap) {
                contagem++;
                // Comparação principal: type
                int cmp = strcmp(arr[j - gap].tipo, temp.tipo);
                if (cmp > 0 || (cmp == 0 && strcmp(arr[j - gap].titulo, temp.titulo) > 0)) {
                    arr[j] = arr[j - gap];
                } else {
                    break;
                }
            }
            arr[j] = temp;
        }
    }
}


// Função para analisar uma linha do CSV e criar um objeto Show
Show analisarLinha(char *linha) {
    Show show;
    char *campos[12];
    int indice = 0;
    int dentroAspas = 0;
    char temp[MAX_LINE] = "";
    int i = 0, tamanho = strlen(linha);
    
    // Quebra a linha CSV considerando aspas
    for (int j = 0; j <= tamanho; j++) {
        if (linha[j] == '"' && linha[j + 1] == '"') {
            if (strlen(temp) < sizeof(temp) - 1) {  // -1 para o novo caractere + null terminator
                strcat(temp, "\"");  // Mais seguro e simples
            }
            j++;
        } else if (linha[j] == '"') {
            dentroAspas = !dentroAspas;
        } else if (linha[j] == ',' && !dentroAspas) {
            campos[indice] = malloc(strlen(temp) + 1);
            strcpy(campos[indice++], temp);
            temp[0] = '\0';
        } else {
            char t[2] = {linha[j], '\0'};
            strcat(temp, t);
        }
    }
    if (strlen(temp) > 0) {
        campos[indice] = malloc(strlen(temp) + 1);
        strcpy(campos[indice++], temp);
    }

    // Corrigindo a leitura do ID
    strncpy(show.id, campos[0], sizeof(show.id) - 1); 
    show.id[sizeof(show.id) - 1] = '\0'; // Garantir terminação de string
    removerEspacos(show.id);  // Remover espaços extras no início e no final

    // O restante do código permanece igual
    strcpy(show.titulo, campos[2]); // título está no índice 2
    for (i = 0; show.titulo[i]; i++) {
        if (show.titulo[i] == '"') {
            memmove(&show.titulo[i], &show.titulo[i + 1], strlen(&show.titulo[i + 1]) + 1);
            i--;
        }
    }

    strcpy(show.tipo, campos[1]);

    // Diretor
    if (strlen(campos[3]) == 0) {
        strcpy(show.diretores[0], "NaN");
        show.qtdDiretores = 1;
    } else {
        dividirLista(campos[3], show.diretores, &show.qtdDiretores);
    }

    // Elenco
    if (strlen(campos[4]) == 0) {
        strcpy(show.elenco[0], "NaN");
        show.qtdElenco = 1;
    } else {
        dividirLista(campos[4], show.elenco, &show.qtdElenco);
        ordenarListaCrescente(show.elenco, show.qtdElenco);
    }

    strcpy(show.pais, strlen(campos[5]) ? campos[5] : "NaN");

    // Data
    if (strlen(campos[6]) == 0 || campos[6][0] == ' ') {
        strptime("January 1, 1900", "%B %d, %Y", &show.dataAdicionado);
    } else {
        // Aqui corrigimos para que a data seja lida com a formatação correta
        strptime(campos[6], "%B %d, %Y", &show.dataAdicionado);
    }    

    show.anoLancamento = strlen(campos[7]) ? atoi(campos[7]) : 0;
    strcpy(show.classificacao, strlen(campos[8]) ? campos[8] : "NaN");
    strcpy(show.duracao, strlen(campos[9]) ? campos[9] : "NaN");

    // Categorias
    if (strlen(campos[10]) == 0) {
        strcpy(show.categorias[0], "NaN");
        show.qtdCategorias = 1;
    } else {
        dividirLista(campos[10], show.categorias, &show.qtdCategorias);
        ordenarListaCrescente(show.categorias, show.qtdCategorias);
    }

    // Libera memória dos campos restantes
    for (int k = 0; k < indice; k++) {
        free(campos[k]);
    }

    return show;
}

// Função para imprimir um array de strings
void imprimirArray(char arr[MAX_ARRAY][100], int qtd) {
    if (qtd == 1 && strcmp(arr[0], "NaN") == 0) {
        printf("NaN");
    } else if (qtd == 1) {
        printf("%s", arr[0]);
    } else {
        printf("[");
        for (int i = 0; i < qtd; i++) {
            printf("%s%s", arr[i], i < qtd - 1 ? ", " : "");
        }
        printf("]");
    }
}

// Função para imprimir as informações de um show
void imprimirShow(Show *show) {
    char dataStr[100];
    strftime(dataStr, sizeof(dataStr), "%B %d, %Y", &show->dataAdicionado);
    // Remove leading zero from the day if present
    if (dataStr[8] == '0') {
        memmove(&dataStr[8], &dataStr[9], strlen(&dataStr[9]) + 1);
    }

    printf("=> %s ## %s ## %s ## ", show->id, show->titulo, show->tipo);

    // Ordenar e imprimir os diretores em ordem alfabética crescente (A a Z)
    if (show->qtdDiretores == 1 && strcmp(show->diretores[0], "NaN") == 0) {
        printf("NaN");
    } else {
        // Ordenação crescente (A-Z)
        for (int i = 0; i < show->qtdDiretores - 1; i++) {
            for (int j = i + 1; j < show->qtdDiretores; j++) {
                if (strcmp(show->diretores[i], show->diretores[j]) > 0) {
                    char temp[100];
                    strcpy(temp, show->diretores[i]);
                    strcpy(show->diretores[i], show->diretores[j]);
                    strcpy(show->diretores[j], temp);
                }
            }
        }

        // Imprimir diretores ordenados
        for (int i = 0; i < show->qtdDiretores; i++) {
            removerEspacos(show->diretores[i]); // Remover espaços extras
            printf("%s", show->diretores[i]);
            if (i < show->qtdDiretores - 1) printf(", ");
        }
    }

    printf(" ## ");

    // Elenco: sempre com colchetes
    printf("[");
    for (int i = 0; i < show->qtdElenco; i++) {
        printf("%s", show->elenco[i]);
        if (i < show->qtdElenco - 1) printf(", ");
    }
    printf("]");

    printf(" ## %s ## %s ## %d ## %s ## %s ## ", show->pais, dataStr, show->anoLancamento, show->classificacao, show->duracao);

    // Categorias: sempre com colchetes
    printf("[");
    if (show->qtdCategorias == 1 && strcmp(show->categorias[0], "NaN") == 0) {
        printf("NaN");
    } else {
        for (int i = 0; i < show->qtdCategorias; i++) {
            printf("%s", show->categorias[i]);
            if (i < show->qtdCategorias - 1) printf(", ");
        }
    }
    printf("]");  // Garantir fechamento dos colchetes

    printf(" ##\n");
}

// Função para clonar um show
Show clonarShow(Show show) {
    Show novoShow;

    // Copiar strings e valores simples
    strcpy(novoShow.id, show.id);
    strcpy(novoShow.titulo, show.titulo);
    strcpy(novoShow.tipo, show.tipo);
    strcpy(novoShow.pais, show.pais);
    novoShow.anoLancamento = show.anoLancamento;
    strcpy(novoShow.classificacao, show.classificacao);
    strcpy(novoShow.duracao, show.duracao);

    // Copiar diretores
    novoShow.qtdDiretores = show.qtdDiretores;
    for (int i = 0; i < show.qtdDiretores; i++) {
        strcpy(novoShow.diretores[i], show.diretores[i]);
    }

    // Copiar elenco
    novoShow.qtdElenco = show.qtdElenco;
    for (int i = 0; i < show.qtdElenco; i++) {
        strcpy(novoShow.elenco[i], show.elenco[i]);
    }

    // Copiar categorias
    novoShow.qtdCategorias = show.qtdCategorias;
    for (int i = 0; i < show.qtdCategorias; i++) {
        strcpy(novoShow.categorias[i], show.categorias[i]);
    }

    // Copiar data
    novoShow.dataAdicionado = show.dataAdicionado;

    return novoShow;
}

// Função para ler o arquivo CSV e preencher a lista de shows
void lerCSV(Show lista[], int *total) {
    FILE *arquivo = fopen("/tmp/disneyplus.csv", "r");
    if (!arquivo) {
        printf("Erro ao abrir o arquivo.\n");
        exit(1);
    }

    char linha[MAX_LINE];
    fgets(linha, MAX_LINE, arquivo); // pula o cabeçalho
    int contador = 0;

    while (fgets(linha, MAX_LINE, arquivo) != NULL && contador < MAX_SHOWS) {
        linha[strcspn(linha, "\r\n")] = '\0'; // remove \n ou \r\n do final
        lista[contador++] = analisarLinha(linha);
    }

    fclose(arquivo);
    *total = contador;
}

// Função para obter o ID de um show
char *obterId(Show *show) { return show->id; }

int compararTitulos(const void *a, const void *b) {
    const Show *showA = (const Show *)a;
    const Show *showB = (const Show *)b;
    return strcmp(showA->titulo, showB->titulo);
}

bool procurartitulo(Show lista[], int qtdShows, char *titulo) {
    int inicio = 0, fim = qtdShows - 1;

    while (inicio <= fim) {
        
        int meio = (inicio + fim) / 2;
        int comparacao = strcmp(lista[meio].titulo, titulo);

        if (comparacao == 0) {
            return true;
        } else if (comparacao < 0) {
            inicio = meio + 1;
        } else {
            fim = meio - 1;
        }
    }
    return false;       
}

void lerCSVporid(Show lista[], int *qtdShows, char *idprocurado) {
    // Limpa o ID procurado
    idprocurado[strcspn(idprocurado, "\r\n")] = '\0';
    FILE *arquivo = fopen("disneyplus.csv", "r");
    //FILE *arquivo = fopen("/tmp/disneyplus.csv", "r");
    if (!arquivo) {
        printf("Erro ao abrir o arquivo.\n");
        exit(1);
    }

    char linha[MAX_LINE];
    fgets(linha, MAX_LINE, arquivo); // Pula cabeçalho

    while (fgets(linha, MAX_LINE, arquivo) != NULL && *qtdShows < MAX_SHOWS) {
        linha[strcspn(linha, "\r\n")] = '\0';
        char linhacopia[MAX_LINE];
        strcpy(linhacopia, linha);
        
        char *id = strtok(linhacopia, ",");
        if (id != NULL) {
            id[strcspn(id, " \r\n")] = '\0';
            
            if (strcmp(id, idprocurado) == 0) {
                lista[*qtdShows] = analisarLinha(linha);
                (*qtdShows)++; // Incrementa qtdShows para cada linha adicionada
            }
        }
    }

    fclose(arquivo);
}
void ordenarshows(Show *array, int qtd, char tipo) {
    for (int i = 0; i < qtd - 1; i++) {
        for (int j = i + 1; j < qtd; j++) {
            bool trocar = false;

            switch (tipo) {
                case 't': // Ordenar por títulos
                    if (strcmp(array[i].titulo, array[j].titulo) > 0) {
                        trocar = true;
                    }
                    break;

                case 'i': // Ordenar por ID
                    if (strcmp(array[i].id, array[j].id) > 0) {
                        trocar = true;
                    }
                    break;

                case 'c': // Ordenar por tipo
                    if (strcmp(array[i].tipo, array[j].tipo) > 0) {
                        trocar = true;
                    }
                    break;

                default:
                    printf("Tipo de ordenação inválido.\n");
                    return;
            }

            if (trocar) {
                Show temp = clonarShow(array[i]);
                array[i] = clonarShow(array[j]);
                array[j] = clonarShow(temp);
            }
        }
    }
}

// Função para ordenar usando Insertion Sort com chave type e critério de desempate title (parcial para k primeiras posições)
void insertionSortParcialPorTipoETitulo(Show *arr, int qtd) {
    int k = 10; // Número de elementos a serem ordenados

    // Ordenar todo o array com Insertion Sort
    for (int i = 1; i < qtd; i++) {
        Show temp = arr[i];
        int j = i - 1;

        // Comparação principal: type
        while (j >= 0 && (strcmp(arr[j].tipo, temp.tipo) > 0 || 
                         (strcmp(arr[j].tipo, temp.tipo) == 0 && strcmp(arr[j].titulo, temp.titulo) > 0))) {
            contagem++;
            arr[j + 1] = arr[j];
            j--;
        }
        arr[j + 1] = temp;
    }

    // Após ordenar todo o array, reorganizar os k primeiros no início
    for (int i = k; i < qtd; i++) {
        Show temp = arr[i];
        int j = i - 1;

        while (j >= k - 1 && (strcmp(arr[j].tipo, temp.tipo) > 0 || 
                             (strcmp(arr[j].tipo, temp.tipo) == 0 && strcmp(arr[j].titulo, temp.titulo) > 0))) {
            contagem++;
            arr[j + 1] = arr[j];
            j--;
        }
        arr[j + 1] = temp;
    }
}

// Função para particionar o array com base na data adicionada e título
int particionarPorDataEPorTitulo(Show *arr, int low, int high) {
    Show pivot = arr[high];
    int i = low - 1;

    for (int j = low; j < high; j++) {
        contagem++;
        
        // Compare year first
        if (arr[j].dataAdicionado.tm_year < pivot.dataAdicionado.tm_year) {
            i++;
            Show temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
            continue;
        }
        if (arr[j].dataAdicionado.tm_year > pivot.dataAdicionado.tm_year) {
            continue;
        }
        
        // If years equal, compare month
        if (arr[j].dataAdicionado.tm_mon < pivot.dataAdicionado.tm_mon) {
            i++;
            Show temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
            continue;
        }
        if (arr[j].dataAdicionado.tm_mon > pivot.dataAdicionado.tm_mon) {
            continue;
        }
        
        // If months equal, compare day
        if (arr[j].dataAdicionado.tm_mday < pivot.dataAdicionado.tm_mday) {
            i++;
            Show temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
            continue;
        }
        if (arr[j].dataAdicionado.tm_mday > pivot.dataAdicionado.tm_mday) {
            continue;
        }
        
        // If dates are equal, compare title
        if (strcmp(arr[j].titulo, pivot.titulo) < 0) {
            i++;
            Show temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    Show temp = arr[i + 1];
    arr[i + 1] = arr[high];
    arr[high] = temp;
    return i + 1;
}

// Função Quicksort com chave date_added e critério de desempate title
void quicksortPorDataEPorTitulo(Show *arr, int low, int high) {
    if (low < high) {
        int pi = particionarPorDataEPorTitulo(arr, low, high);

        quicksortPorDataEPorTitulo(arr, low, pi - 1);
        quicksortPorDataEPorTitulo(arr, pi + 1, high);
    }
}


// Função para ordenar usando Bubble Sort com chave date_added e critério de desempate title
void bubbleSortPorDataEPorTitulo(Show *arr, int qtd) {
    for (int i = 0; i < qtd - 1; i++) {
        for (int j = 0; j < qtd - i - 1; j++) {
            contagem++;

            // Compare year first
            if (arr[j].dataAdicionado.tm_year > arr[j + 1].dataAdicionado.tm_year) {
                Show temp = clonarShow(arr[j]);
                arr[j] = clonarShow(arr[j + 1]);
                arr[j + 1] = clonarShow(temp);
                continue;
            }
            if (arr[j].dataAdicionado.tm_year < arr[j + 1].dataAdicionado.tm_year) {
                continue;
            }

            // If years equal, compare month
            if (arr[j].dataAdicionado.tm_mon > arr[j + 1].dataAdicionado.tm_mon) {
                Show temp = clonarShow(arr[j]);
                arr[j] = clonarShow(arr[j + 1]);
                arr[j + 1] = clonarShow(temp);
                continue;
            }
            if (arr[j].dataAdicionado.tm_mon < arr[j + 1].dataAdicionado.tm_mon) {
                continue;
            }

            // If months equal, compare day
            if (arr[j].dataAdicionado.tm_mday > arr[j + 1].dataAdicionado.tm_mday) {
                Show temp = clonarShow(arr[j]);
                arr[j] = clonarShow(arr[j + 1]);
                arr[j + 1] = clonarShow(temp);
                continue;
            }
            if (arr[j].dataAdicionado.tm_mday < arr[j + 1].dataAdicionado.tm_mday) {
                continue;
            }

            // If dates are equal, compare title
            if (strcmp(arr[j].titulo, arr[j + 1].titulo) > 0) {
                Show temp = clonarShow(arr[j]);
                arr[j] = clonarShow(arr[j + 1]);
                arr[j + 1] = clonarShow(temp);
            }
        }
    }
}

// Função para criar um heap com chave primária diretor (ordem alfabética) e critério de desempate título
void heapifyPorDiretorETitulo(Show *arr, int n, int i) {
    int maior = i; // Inicializa o maior como raiz
    int esquerda = 2 * i + 1; // Filho esquerdo
    int direita = 2 * i + 2; // Filho direito

    // Verifica se o filho esquerdo é maior que a raiz
    if (esquerda < n) {
        if (strcmp(arr[esquerda].diretores[0], "NaN") == 0 || arr[esquerda].diretores[0][0] == '\0') {
            // Move para o fim se o diretor for vazio ou especial
            maior = direita < n ? direita : maior;
        } else if (strcmp(arr[maior].diretores[0], "NaN") == 0 || arr[maior].diretores[0][0] == '\0') {
            maior = esquerda;
        } else if (strcmp(arr[esquerda].diretores[0], arr[maior].diretores[0]) > 0) {
            maior = esquerda;
        } else if (strcmp(arr[esquerda].diretores[0], arr[maior].diretores[0]) == 0 &&
                   strcmp(arr[esquerda].titulo, arr[maior].titulo) > 0) {
            maior = esquerda;
        }
    }

    // Verifica se o filho direito é maior que o maior até agora
    if (direita < n) {
        if (strcmp(arr[direita].diretores[0], "NaN") == 0 || arr[direita].diretores[0][0] == '\0') {
            // Move para o fim se o diretor for vazio ou especial
            maior = maior == esquerda ? maior : direita;
        } else if (strcmp(arr[maior].diretores[0], "NaN") == 0 || arr[maior].diretores[0][0] == '\0') {
            maior = direita;
        } else if (strcmp(arr[direita].diretores[0], arr[maior].diretores[0]) > 0) {
            maior = direita;
        } else if (strcmp(arr[direita].diretores[0], arr[maior].diretores[0]) == 0 &&
                   strcmp(arr[direita].titulo, arr[maior].titulo) > 0) {
            maior = direita;
        }
    }

    // Se o maior não for a raiz
    if (maior != i) {
        Show temp = arr[i];
        arr[i] = arr[maior];
        arr[maior] = temp;

        // Recursivamente aplica o heapify na subárvore afetada
        heapifyPorDiretorETitulo(arr, n, maior);
    }
}

// Função para ordenar usando Heap Sort com chave diretor e critério de desempate título
void heapSortPorDiretorETitulo(Show *arr, int n) {
    // Constrói o heap (reorganiza o array)
    for (int i = n / 2 - 1; i >= 0; i--) {
        heapifyPorDiretorETitulo(arr, n, i);
    }

    // Extrai elementos do heap um por um
    for (int i = n - 1; i > 0; i--) {
        // Move a raiz atual para o fim
        Show temp = arr[0];
        arr[0] = arr[i];
        arr[i] = temp;

        // Chama heapify na heap reduzida
        heapifyPorDiretorETitulo(arr, i, 0);
    }
}

// Função auxiliar para obter o dígito em uma posição específica
int obterDigito(int numero, int posicao) {
    contagem++; // Incrementa a contagem para cada chamada
    return (numero / posicao) % 10;
}

// Função para encontrar o maior número no array (base para o Radixsort)
int encontrarMaiorAno(Show *arr, int qtd) {
    int maior = arr[0].anoLancamento;
    for (int i = 1; i < qtd; i++) {
        contagem++; // Incrementa a contagem para cada comparação
        if (arr[i].anoLancamento > maior) {
            maior = arr[i].anoLancamento;
        }
    }
    return maior;
}

// Função para realizar a contagem e ordenação por dígito
void countingSortPorAno(Show *arr, int qtd, int posicao) {
    Show *saida = malloc(sizeof(Show) * qtd);
    int contagemDigitos[10] = {0};

    // Contar a frequência de cada dígito
    for (int i = 0; i < qtd; i++) {
        contagem++; // Incrementa a contagem para cada iteração
        int digito = obterDigito(arr[i].anoLancamento, posicao);
        contagemDigitos[digito]++;
    }

    // Atualizar contagem para obter posições acumuladas
    for (int i = 1; i < 10; i++) {
        contagem++; // Incrementa a contagem para cada iteração
        contagemDigitos[i] += contagemDigitos[i - 1];
    }

    // Construir o array de saída
    for (int i = qtd - 1; i >= 0; i--) {
        contagem++; // Incrementa a contagem para cada iteração
        int digito = obterDigito(arr[i].anoLancamento, posicao);
        saida[contagemDigitos[digito] - 1] = arr[i];
        contagemDigitos[digito]--;
    }

    // Copiar o array de saída de volta para o array original
    for (int i = 0; i < qtd; i++) {
        contagem++; // Incrementa a contagem para cada iteração
        arr[i] = saida[i];
    }

    free(saida);
}

// Função principal do Radixsort com critério de desempate por título
void radixsortPorAnoETitulo(Show *arr, int qtd) {
    int maiorAno = encontrarMaiorAno(arr, qtd);

    // Ordenar por cada dígito, começando da unidade
    for (int posicao = 1; maiorAno / posicao > 0; posicao *= 10) {
        contagem++; // Incrementa a contagem para cada iteração
        countingSortPorAno(arr, qtd, posicao);
    }

    // Critério de desempate: ordenar por título para anos iguais
    for (int i = 0; i < qtd - 1; i++) {
        for (int j = i + 1; j < qtd; j++) {
            contagem++; // Incrementa a contagem para cada comparação
            if (arr[i].anoLancamento == arr[j].anoLancamento &&
                strcmp(arr[i].titulo, arr[j].titulo) > 0) {
                Show temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
    }
}






// Função principal
int main() {
    
struct timespec inicio, fim;

    double tempo_gasto;

    Show *catalogo = malloc(sizeof(Show) * MAX_SHOWS);
    if (catalogo == NULL) {
        printf("Erro ao alocar memória.\n");
        return 1;
    }
   
    
    int qtdShows = 0;
    

    char idprocurado[100];
    // Lê o arquivo CSV e preenche o catálogo
    while (fgets(idprocurado, sizeof(idprocurado), stdin) && strcmp(idprocurado, "FIM\n") != 0) {
        lerCSVporid(catalogo, &qtdShows, idprocurado);
        
    }
    char title[1000];
    //iniciando cronometro
    clock_gettime(CLOCK_MONOTONIC, &inicio);
    // Ordena o catálogo por Titulo
    heapSortPorDiretorETitulo(catalogo, qtdShows);
    // findando cronometro
    clock_gettime(CLOCK_MONOTONIC, &fim);
    // Calcula o tempo gasto
    tempo_gasto = (fim.tv_sec - inicio.tv_sec) + (fim.tv_nsec - inicio.tv_nsec) / 1e9;
    
    
    // Imprimir todos os shows do catálogo
    for (int i = 0; i < 10; i++) {
        imprimirShow(&catalogo[i]);
    }


    FILE *arquivo_saida = fopen("804517_heapsort.txt   ", "w");
    if (!arquivo_saida) {
        printf("Erro ao criar o arquivo de saída.\n");
        free(catalogo);
        return 1;
    }
    fprintf(arquivo_saida, "804517\t");

    fprintf(arquivo_saida, "%f\t", tempo_gasto);
    fprintf(arquivo_saida, "%d", contagem);

    fclose(arquivo_saida);
    

    free(catalogo);
    return 0;
}

