cmake_minimum_required(VERSION 3.1)
project(Final)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES
    count_ones.c
    DLList.cpp
    DLList.h
    HashTableVoid.cc
    RArray.c
    RArray.h
    SLList.c
    SLList.h
    sort_array.c
    string.c
    Tree.cpp
    TreeDictionary.cpp)

add_executable(Final ${SOURCE_FILES} TreeDictionary.h HashTableVoid.h)