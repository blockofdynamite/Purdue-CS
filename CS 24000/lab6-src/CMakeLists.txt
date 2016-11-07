cmake_minimum_required(VERSION 3.1)
project(lab6_src)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES
    HashTableVoid.cc
    HashTableVoid.h
    HashTableVoidTest.cc
    IRCServer.cc
    IRCServer.h
    LinkedList.c
    LinkedList.h)

add_executable(lab6_src ${SOURCE_FILES})