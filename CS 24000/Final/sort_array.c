#include <string.h>
#include <stdlib.h>
#include <stdio.h>

typedef int (*COMP_FUNC)(void *a, void *b);

void sort_array(void *array, int num_elements, int element_size, COMP_FUNC comp_func) {

    void *temp = malloc(element_size);

    int i, j;

    for (i = 0; i < num_elements; i++) {
        for (j = 0; j < num_elements - i - 1; j++) {
            void *e1 = (void *) (char *) array + j * element_size;
            void *e2 = (void *) (char *) array + (j + 1) * element_size;
            if (comp_func(e1, e2) > 0) {
                memcpy(temp, e1, element_size);
                memcpy(e1, e2, element_size);
                memcpy(e2, temp, element_size);
            }
        }
    }
}

int compInt(void *a, void *b) {
    int *p1 = (int *) a;
    int *p2 = (int *) b;
    if (*p1 > *p2) {
        return 1;
    }
    else if (*p1 < *p2) {
        return -1;
    }
    else {
        return 0;
    }
}

int compStr(void *a, void *b) {
    char **p1 = (char **) a;
    char **p2 = (char **) b;
    if (strcmp(*p1, *p2) > 0) {
        return 1;
    }
    else if (strcmp(*p1, *p2) < 0) {
        return -1;
    }
    else {
        return 0;
    }
}

int main() {
    int a[] = {7, 8, 1, 4, 3, 2};
    int n = sizeof(a) / sizeof(int);
    sort_array(a, n, sizeof(int), compInt);
    int i = 0;
    for (i = 0; i < n; i++) {
        printf("%d\n", a[i]);
    }
    char *strings[] = {"pear", "banana", "apple", "strawberry"};
    n = sizeof(strings) / sizeof(char *);
    sort_array(strings, n, sizeof(char *), compStr);
    for (i = 0; i < n; i++) {
        printf("%s\n", strings[i]);
    }
}





