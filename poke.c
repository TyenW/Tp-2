#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <stdlib.h>

struct poke {
    int id;
    int gen;
    char nome[50];
    char desc[100];
    char tp1[50];
    char tp2[50];
    char hab[200];  // Para garantir espaço suficiente para as habilidades
    float peso;
    float altura;
    int cr;
    bool len;       // Representa se é lendário
    char capture_date[11];  // Para armazenar a data de captura (dd/mm/yyyy)
};

int main(int argc, char const *argv[]) {
    char linha[1000];
    char *token;
    int cont = 0;
    FILE* csv = fopen("/tmp/pokemon.csv", "r");
    struct poke lista[802];

    if (csv == NULL) {
        perror("Erro ao abrir o arquivo");
        return 1;
    }

    while (fgets(linha, sizeof(linha), csv) != NULL) {
        // Remover a nova linha no final da linha lida
        linha[strcspn(linha, "\n")] = 0;

        // Ler cada campo da linha CSV
        token = strtok(linha, ",");
        if (token != NULL) lista[cont].id = atoi(token); // Converte para int

        token = strtok(NULL, ",");
        if (token != NULL) lista[cont].gen = atoi(token); // Converte para int

        token = strtok(NULL, ",");
        if (token != NULL) strncpy(lista[cont].nome, token, sizeof(lista[cont].nome)); // Copia a string

        token = strtok(NULL, ",");
        if (token != NULL) strncpy(lista[cont].desc, token, sizeof(lista[cont].desc)); // Copia a string

        token = strtok(NULL, ",");
        if (token != NULL) strncpy(lista[cont].tp1, token, sizeof(lista[cont].tp1)); // Copia a string

        token = strtok(NULL, ",");
        if (token != NULL) strncpy(lista[cont].tp2, token, sizeof(lista[cont].tp2)); // Copia a string

        // Lê as habilidades, que estão entre aspas
        token = strtok(NULL, "\"");
        if (token != NULL) {
            // Remove os colchetes e salva apenas as habilidades
            char *subtoken = strtok(token + 1, ","); // Começa após o primeiro colchete
            strcpy(lista[cont].hab, "["); // Inicia a string com um colchete
            while (subtoken != NULL) {
                // Adiciona cada habilidade à string
                strncat(lista[cont].hab, subtoken, sizeof(lista[cont].hab) - strlen(lista[cont].hab) - 1);
                subtoken = strtok(NULL, ",");
                if (subtoken != NULL) {
                    strncat(lista[cont].hab, ", ", sizeof(lista[cont].hab) - strlen(lista[cont].hab) - 1); // Adiciona vírgula e espaço
                }
            }
            strcat(lista[cont].hab, "]"); // Finaliza a string com um colchete
        }

        token = strtok(NULL, ",");
        if (token != NULL) lista[cont].peso = atof(token); // Converte para float

        token = strtok(NULL, ",");
        if (token != NULL) lista[cont].altura = atof(token); // Converte para float

        token = strtok(NULL, ",");
        if (token != NULL) lista[cont].cr = atoi(token); // Converte para int

        token = strtok(NULL, ",");
        if (token != NULL) lista[cont].len = (atoi(token) != 0); // Converte para bool (1 se não-zero)

        // Captura da data de captura
        token = strtok(NULL, ",");
        if (token != NULL) strncpy(lista[cont].capture_date, token, sizeof(lista[cont].capture_date)); // Copia a data de captura

        cont++;
    }

    fclose(csv); // Lembre-se de fechar o arquivo após o uso

    char num[4];
    scanf("%s", num);
    while (strcmp(num, "FIM") != 0) {
        int n = atoi(num);
        
        if (n >= 0 && n < cont) { // Verifica se o índice está dentro dos limites
            printf("[#%d -> %s: %s - ", lista[n].id, lista[n].nome, lista[n].desc);
            
            // Exibição dos tipos
            if (strlen(lista[n].tp2) == 0) {
                printf("['%s']", lista[n].tp1); // Apenas o primeiro tipo
            } else {
                printf("['%s', '%s']", lista[n].tp1, lista[n].tp2); // Ambos os tipos com vírgula
            }

            // Exibição das habilidades
            printf(" - %s - %.1fkg - %.1fm - %d%% - %s - %d gen] - %s\n",
                   lista[n].hab, lista[n].peso, lista[n].altura, lista[n].cr,
                   lista[n].len ? "true" : "false", lista[n].gen, lista[n].capture_date); // Inclui a data de captura
        } else {
            printf("Índice fora dos limites. Digite um número entre 0 e %d.\n", cont - 1);
        }

        scanf("%s", num);
    }

    return 0;
}
