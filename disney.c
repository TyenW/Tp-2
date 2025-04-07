#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdbool.h>

typedef struct {
    char show_id[50];
    char type[20];
    char title[200];
    char director[100];
    char **cast;
    int cast_count;
    char country[50];
    int time[3]; // time[0] = dia, time[1] = mês, time[2] = ano
    int release_year;
    char rating[10];
    char duration[20];
    char **listed_in;
    int listed_in_count;
} Disney;

Disney lista[1368];
int total = 0;

void ler_csv(const char *filename) {
    FILE *file = fopen(filename, "r");
    if (!file) {
        perror("Erro ao abrir o arquivo");
        return;
    }

    char line[1024];
    fgets(line, sizeof(line), file); // Ignora o cabeçalho

    while (fgets(line, sizeof(line), file)) {
        char *tokens[20]; // Array para armazenar os campos da linha
        int token_count = 0;

        // Divide a linha em campos, considerando aspas
        char *start = line;
        while (*start) {
            if (*start == '"') {
                start++;
                char *end = strchr(start, '"');
                if (end) {
                    *end = '\0';
                    tokens[token_count++] = start;
                    start = end + 1;
                }
            } else {
                char *end = strpbrk(start, ",\n");
                if (end) {
                    *end = '\0';
                    tokens[token_count++] = start;
                    start = end + 1;
                } else {
                    tokens[token_count++] = start;
                    break;
                }
            }
            if (*start == ',') start++;
        }

        // Lê os campos básicos
        if (token_count > 0) strcpy(lista[total].show_id, tokens[0]);
        if (token_count > 1) strcpy(lista[total].type, tokens[1]);
        if (token_count > 2) strcpy(lista[total].title, tokens[2]);
        if (token_count > 3) strcpy(lista[total].director, tokens[3]);

        // Processa o elenco
        if (token_count > 4 && tokens[4][0] != '\0') {
            lista[total].cast_count = 0;
            lista[total].cast = malloc(sizeof(char *) * 50);
            if (!lista[total].cast) {
                perror("Erro ao alocar memória para elenco");
                exit(EXIT_FAILURE);
            }
            char *cast_token = strtok(tokens[4], ",");
            while (cast_token) {
                lista[total].cast[lista[total].cast_count] = strdup(cast_token);
                lista[total].cast_count++;
                cast_token = strtok(NULL, ",");
            }
        } else {
            lista[total].cast_count = 0;
            lista[total].cast = NULL;
        }

        if (token_count > 5) strcpy(lista[total].country, tokens[5]);

        // Lê a data
        if (token_count > 6) sscanf(tokens[6], "%d/%d/%d", &lista[total].time[0], &lista[total].time[1], &lista[total].time[2]);

        if (token_count > 7) lista[total].release_year = atoi(tokens[7]);
        if (token_count > 8) strcpy(lista[total].rating, tokens[8]);
        if (token_count > 9) strcpy(lista[total].duration, tokens[9]);

        // Processa as categorias
        if (token_count > 10 && tokens[10][0] != '\0') {
            lista[total].listed_in_count = 0;
            lista[total].listed_in = malloc(sizeof(char *) * 50);
            if (!lista[total].listed_in) {
                perror("Erro ao alocar memória para categorias");
                exit(EXIT_FAILURE);
            }
            char *category_token = strtok(tokens[10], ",");
            while (category_token) {
                lista[total].listed_in[lista[total].listed_in_count] = strdup(category_token);
                lista[total].listed_in_count++;
                category_token = strtok(NULL, ",");
            }
        } else {
            lista[total].listed_in_count = 0;
            lista[total].listed_in = NULL;
        }

        total++;
    }
    fclose(file);
}
void imprimir_dados() {
    for (int i = 0; i < total; i++) {
        printf("[=> %s ## ", lista[i].show_id); // Imprime o ID do show
        printf("%s ## ", lista[i].type); // Imprime o tipo do show
        printf("%s ## ", lista[i].title); // Imprime o título do show
        printf("%s ## ", (strlen(lista[i].director) == 0 ? "NaN" : lista[i].director)); // Imprime o diretor ou "NaN" se vazio
        printf("[");
        for (int j = 0; j < lista[i].cast_count; j++) {
            printf("%s%s", lista[i].cast[j], (j < lista[i].cast_count - 1) ? ", " : ""); // Imprime o elenco
        }
        printf("] ## ");
        printf("%s ## ", (strlen(lista[i].country) == 0 ? "NaN" : lista[i].country)); // Imprime o país ou "NaN" se vazio
        if (lista[i].time[0] == 0 && lista[i].time[1] == 0 && lista[i].time[2] == 0) {
            printf("NaN ## "); // Imprime "NaN" se a data for inválida
        } else {
            printf("%02d/%02d/%04d ## ", lista[i].time[0], lista[i].time[1], lista[i].time[2]); // Imprime a data
        }
        printf("%d ## ", lista[i].release_year); // Imprime o ano de lançamento
        printf("%s ## ", (strlen(lista[i].rating) == 0 ? "NaN" : lista[i].rating)); // Imprime a classificação indicativa ou "NaN" se vazio
        printf("%s ## ", lista[i].duration); // Imprime a duração
        printf("[");
        for (int j = 0; j < lista[i].listed_in_count; j++) {
            printf("%s%s", lista[i].listed_in[j], (j < lista[i].listed_in_count - 1) ? ", " : ""); // Imprime os gêneros
        }
        printf("]\n");
    }
}

int main() {
    printf("Lendo dados do arquivo...\n");
    ler_csv("disneyplus.csv");
    printf("Dados lidos com sucesso!\n\n");
    imprimir_dados();
    printf("Total de filmes lidos: %d\n", total);

    // Libera a memória alocada
    for (int i = 0; i < total; i++) {
        for (int j = 0; j < lista[i].cast_count; j++) {
            free(lista[i].cast[j]);
        }
        free(lista[i].cast);
        for (int j = 0; j < lista[i].listed_in_count; j++) {
            free(lista[i].listed_in[j]);
        }
        free(lista[i].listed_in);
    }
    return 0;
}