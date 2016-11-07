#include <stdio.h>
#include <string.h>
#include <stdlib.h>
struct X{
  char a;
  int i;
  char b;
  int *p;
};

struct tree_node
{
    char *str;
    struct tree_node *left;
    struct tree_node *right;
};

void memdump(char * p , int len)
/***
 * memdump: dump memory in bytes and in ascii code.
 * Example of dump:
 * 0xbeab36e0: 41 76 00 40 09 00 00 00 30 00 00 00 e4 36 ab be Av.@....0....6.. 
 * 0xbeab36f0: 00 00 00 00 00 00 28 40 48 65 6c 6c 6f 20 77 6f ......(@Hello wo
 *
 * Argument:
 *  p: the pointer to the target memory
 *  len: the length of dump output
 *
 ***/
 {

    int test = ((unsigned long) p) % 16;

    int d;
    for (d = test ; d > 0; d--) {
       p--;
   }

    int i; //for use in for loops
    int j; // " "
    int k; // " "

    int counter = test;
    int internalCounter = 0;

    for (i = 0; i < len; i += 16) {

        fprintf(stdout, "0x%016lX: ", (unsigned long) p + i);

        int c;

        if (i == len - 16) {
            internalCounter = 0;
        }

        for (j = i; j < i + 16; j++) {
			//fprintf(stdout, "%d", i==len-16);
            c = p[j] & 0xFF;
            if (counter > 0) {
                fprintf(stdout, ".. ");
                counter--;
            } else if (internalCounter + test >= 16 && i == len-16) {
                fprintf(stdout, ".. ");
                continue;
            }
            else if (j > len - 1) {
                fprintf(stdout, "   ");
            }
            else fprintf(stdout, "%02X ", c);
            internalCounter++;
        }

        fprintf(stdout, " ");

        for (j = i; j < i + 16; j++) {
            c = p[j] & 0xFF;
            if (j > len - 1) {
                break;
            }
            fprintf(stdout, "%c", (c >= 32 && c <= 127) ? c : '.');
        }
        fprintf(stdout,"\n");
    }

}

struct tree_node *create_tree_node(char *str, struct tree_node *left, struct tree_node *right)
/***
 * create_tree_node: wrapper function for creating tree_node
 * 
 * You don't need to modify this part
 *
 * Argument:
 *  root: pointer to struct tree_node
 *
 ***/
 {
    struct tree_node *node = malloc(sizeof(struct tree_node));
    node->str = str;
    node->left = left;
    node->right = right;

    return node;
}

void free_tree(struct tree_node *root)
/***
 * free_tree: free allocated tree_node recursively
 * 
 * You don't need to modify this part
 *
 * Argument:
 *  root: pointer to struct tree_node
 *
 ***/
 {
    if(root == NULL)
    {
        return;
    }

    free(root->str);
    free_tree(root->left);
    free_tree(root->right);
    free(root);

    return;
}

void preorder_traverse(struct tree_node *node)
/***
 * preorder_traverse traverse tree in preorder,
 * which mean that give a tree B<-A->C, the print out order will be:
 * A, C, B
 *
 * Argument:
 *  node: pointer to struct tree_node
 *
 ***/
 {
    if(node == NULL)
    {
        return;
    }

    /**This printf just give you an idea how preorder_traverse print out element from tree**/
    printf("%s ", node->str);
    
    /**
     * You will need to modify this part to dump memory of current tree node.
     * Remember, you need dump current node, str, left, right. Think carefully what do you actually need to dump here.
     **/

     preorder_traverse(node->left);
     preorder_traverse(node->right);
 }

 int main() {

    char str[20];
    int a;
    int b;
    double y;
    int int_array[4];
    struct X x;
    char *s;

    printf("str:       %p\n", str);
    printf("&a:        %p\n", &a);
    printf("&b:        %p\n", &b);
    printf("&y:        %p\n", &y);
    printf("&x:        %p\n", &x);
    printf("int_array: %p\n", int_array);
    printf("\n");

    // memdump before variable assignment
    printf("\n               -----Part 3.1 before variable assignment-----\n\n");
    memdump((char *) &a, 256);

    strcpy(str, "Hello World\n");
    a = 5;
    b = -5;
    y = 12.625;
    int_array[0] = 0;
    int_array[1] = 10;
    int_array[2] = 20;
    int_array[3] = 30;
    x.a = 'A';
    x.i = 9;
    x.b = '0';
    x.p = &x.i;

    // memdump after variable assignment
    printf("\n               -----Part 3.1 after variable assignment-----\n\n");
    memdump((char *) &a, 256);

    // The following assignments may cause a compiler warning. 'Safely' ignore the warning.
    // However, using *(int_array + n) instead of int_array[n] does not cause a warning.
    int_array[4] = 1685024583;
    int_array[5] = 543521122;
    int_array[6] = 1819438935;
    int_array[7] = 100;
    
    // memdump after buffer overflow
    printf("\n               -----Part 3.1 after buffer overflow-----\n\n");
    memdump((char *) &a, 256);

    struct tree_node *root = create_tree_node(NULL, NULL, NULL);
    printf("\n               -----Part 3.2 tree_node before variable assignment-----\n\n");
    memdump((char *) root, 256);

    /* Create the binary tree */
    s = strdup("to");
    struct tree_node *left = create_tree_node(s, NULL, NULL);
    s = strdup("lab8");
    struct tree_node *right = create_tree_node(s, NULL, NULL);
    s = strdup("Welcome");
    root->str = s;
    root->left = left;
    root->right = right;

    printf("\n               -----Part 3.2 tree_node after variable assignment-----\n\n");
    memdump((char *)root, 256);

    free_tree(root);
}
