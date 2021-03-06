#include <string.h>
#include <stdio.h>

#include "TreeDictionary.h"

TreeDictionary::TreeDictionary() {
    _root = NULL;
    _numberOfEntries = 0;
}

bool TreeDictionary::addRecord(const char *key, int data) {
    TreeNode **n = &_root;
    while (*n != NULL) {
        int result = strcmp(key, (*n)->_key);
        if (result < 0) {
            n = &(*n)->_left;
        }
        else if (result > 0) {
            n = &(*n)->_right;
        }
        else {
            (*n)->_data = data;
            return false;
        }
    }
    *n = new TreeNode();
    (*n)->_key = strdup(key);
    (*n)->_data = data;
    (*n)->_left = NULL;
    (*n)->_right = NULL;
    _numberOfEntries++;
    return true;
}

int TreeDictionary::numberOfEntries() {
    return _numberOfEntries;
}

bool TreeDictionary::findRecord(const char *key, int &data) {
    TreeNode *n = _root;
    while (n != NULL) {
        int result = strcmp(key, n->_key);
        if (result < 0) {
            n = n->_left;
        }
        else if (result > 0) {
            n = n->_right;
        }
        else {
            data = n->_data;
            return true;
        }
    }
    return false;
}

void TreeDictionary::printIndented(TreeNode *node, int level) {
    for (int i = 0; i < level; i++) printf("  ");
    if (node == NULL) {
        printf("NULL\n");
        return;
    }
    printf("%s:%d\n", node->_key, node->_data);
    printIndented(node->_left, level + 1);
    printIndented(node->_right, level + 1);
}

void TreeDictionary::printIndented() {
    printIndented(_root, 0);
}

void TreeDictionary::print(TreeNode * node) {
    if (node == NULL) return;
    printf("%s:%d\n", node->_key, node->_data);
    print(node->_left);
    print(node->_right);
}

void TreeDictionary::print() {
    print(_root);
}

// Max depth in tree
void TreeDictionary::computeDepthHelper(TreeNode *node, int depth, int &currentMaxDepth) {
    if (node == NULL) return;
    if (node->_left == NULL && node->_right == NULL) {
        // Found a leaf. Update maxDepth and minDepth
        if (currentMaxDepth == -1 || depth > currentMaxDepth) {
            currentMaxDepth = depth;
        }
    }
    computeDepthHelper(node->_left, depth + 1, currentMaxDepth);
    computeDepthHelper(node->_right, depth + 1, currentMaxDepth);

    return;
}

int TreeDictionary::maxDepth() {
    int currentMaxDepth = -1;

    computeDepthHelper(_root, 0, currentMaxDepth);

    return currentMaxDepth;
}





