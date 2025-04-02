#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

// Inclua o arquivo com a definição da struct diretamente no código principal
typedef struct {
    char show_id[50];
    char type[20];
    char title[200];
    char director[100];
    char **cast;
    int cast_count;
    char country[50];
    struct tm date_added;
    int release_year;
    char rating[10];
    char duration[20];
    char **listed_in;
    int listed_in_count;
} Disney;

#define MAX_LINE_LENGTH 1024

// Função para dividir uma string em tokens com base em um delimitador, respeitando aspas
char **split_csv_line(const char *line, int *count) {
    char **result = NULL;
    *count = 0;

    const char *start = line;
    char *token = NULL;
    int in_quotes = 0;

    while (*line) {
        if (*line == '"') {
            in_quotes = !in_quotes; // Alterna o estado de dentro/fora de aspas
        } else if (*line == ',' && !in_quotes) {
            token = strndup(start, line - start);
            result = realloc(result, sizeof(char *) * (*count + 1));
            result[*count] = token;
            (*count)++;
            start = line + 1;
        }
        line++;
    }

    // Adiciona o último token
    token = strndup(start, line - start);
    result = realloc(result, sizeof(char *) * (*count + 1));
    result[*count] = token;
    (*count)++;

    return result;
}

// Função para converter uma string de data no formato "Mês Dia, Ano" para struct tm
struct tm parse_date(const char *date_str) {
    struct tm date = {0};
    char month[20];
    int day, year;

    if (sscanf(date_str, "%s %d, %d", month, &day, &year) == 3) {
        char *months[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for (int i = 0; i < 12; i++) {
            if (strcmp(month, months[i]) == 0) {
                date.tm_mon = i;
                break;
            }
        }
        date.tm_mday = day;
        date.tm_year = year - 1900;
    }

    return date;
}

// Função para ler o arquivo CSV e preencher a struct Disney
Disney *read_disney_csv(const char *filename, int *count) {
    FILE *file = fopen(filename, "r");
    if (!file) {
        perror("Erro ao abrir o arquivo. Verifique se o caminho e o nome do arquivo estão corretos.");
        return NULL;
    }

    char line[MAX_LINE_LENGTH];
    fgets(line, MAX_LINE_LENGTH, file); // Ignorar o cabeçalho

    Disney *shows = NULL;
    *count = 0;

    while (fgets(line, MAX_LINE_LENGTH, file)) {
        shows = realloc(shows, sizeof(Disney) * (*count + 1));
        Disney *current = &shows[*count];

        int field_count;
        char **fields = split_csv_line(line, &field_count);

        strcpy(current->show_id, fields[0]);
        strcpy(current->type, fields[1]);
        strcpy(current->title, fields[2]);
        strcpy(current->director, fields[3] ? fields[3] : "");
        current->cast = split_csv_line(fields[4] ? fields[4] : "", &current->cast_count);
        strcpy(current->country, fields[5] ? fields[5] : "");
        current->date_added = parse_date(fields[6] ? fields[6] : "");
        current->release_year = atoi(fields[7] ? fields[7] : "0");
        strcpy(current->rating, fields[8] ? fields[8] : "");
        strcpy(current->duration, fields[9] ? fields[9] : "");
        current->listed_in = split_csv_line(fields[10] ? fields[10] : "", &current->listed_in_count);

        for (int i = 0; i < field_count; i++) {
            free(fields[i]);
        }
        free(fields);

        (*count)++;
    }

    fclose(file);
    return shows;
}

// Função para liberar a memória alocada para a struct Disney
void free_disney(Disney *shows, int count) {
    for (int i = 0; i < count; i++) {
        for (int j = 0; j < shows[i].cast_count; j++) {
            free(shows[i].cast[j]);
        }
        free(shows[i].cast);

        for (int j = 0; j < shows[i].listed_in_count; j++) {
            free(shows[i].listed_in[j]);
        }
        free(shows[i].listed_in);
    }
    free(shows);
}

int main() {
    int count;
    Disney *shows = read_disney_csv("disneyplus.csv", &count);

    if (shows) {
        for (int i = 0; i < count; i++) {
            printf("Show ID: %s\n", shows[i].show_id);
            printf("Title: %s\n", shows[i].title);
            printf("Type: %s\n", shows[i].type);
            printf("Director: %s\n", shows[i].director);
            printf("Release Year: %d\n", shows[i].release_year);
            printf("Rating: %s\n", shows[i].rating);
            printf("Duration: %s\n", shows[i].duration);
            printf("\n");
        }

        free_disney(shows, count);
    }

    return 0;
}
