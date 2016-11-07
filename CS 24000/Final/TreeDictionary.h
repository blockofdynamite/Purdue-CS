#include "Dictionary.h"

class TreeNode {
    friend class TreeDictionary;

    const char * _key;
    int  _data;
    TreeNode *_left;
    TreeNode *_right;

};

class TreeDictionary : public Dictionary {
    friend class TreeDictionaryIterator;

    int _numberOfEntries;
    TreeNode * _root;

public:
    TreeDictionary();
    bool addRecord( const char * key, int data);
    bool findRecord(const  char * key, int & data);
    int numberOfEntries();
    void print();
    void print(TreeNode * node);
    void printIndented();
    void printIndented(TreeNode * node, int level);
    int maxDepth();
    void computeDepthHelper(TreeNode * node, int depth, int & currentMaxDepth);
};