#include "HashTableVoid.h"

int HashTableVoid::hash(const char *key) {
    int h = 0;
    const char *p = key;
    while (*p) {
        h += *p;
        p++;
    }
    return h % TableSize;
}

HashTableVoid::HashTableVoid() {
    _buckets = (HashTableVoidEntry **) malloc(TableSize * sizeof(HashTableVoidEntry *));
    for (int i = 0; i < TableSize; i++) {
        _buckets[i] = NULL;
    }
}

bool HashTableVoid::insertItem(const char *key, void *data) {
    int h = hash(key);

    HashTableVoidEntry *e = _buckets[h];
    while (e != NULL) {
        if (!strcmp(e->_key, key)) {
            e->_data = data;
            return true;
        }
        e = e->_next;
    }

    e = new HashTableVoidEntry;
    e->_key = strdup(key);
    e->_data = data;
    e->_next = _buckets[h];
    _buckets[h] = e;
    return false;
}

bool HashTableVoid::find(const char *key, void **data) {
    int h = hash(key);

    HashTableVoidEntry *e = _buckets[h];
    while (e != NULL) {
        if (!strcmp(e->_key, key)) {
            *data = e->_data;
            return true;
        }
        e = e->_next;
    }
    return false;
}

bool HashTableVoid::removeElement(const char *key) {
    int h = hash(key);

    HashTableVoidEntry *e = _buckets[h];
    HashTableVoidEntry *prev = NULL;
    while (e != NULL) {
        if (!strcmp(e->_key, key)) {
            if (prev != NULL) {
                prev->_next = e->_next;
            }
            else {
                _buckets[h] = e->_next;
            }
            delete e;
            return true;
        }
        prev = e;
        e = e->_next;
    }
    return false;
}
