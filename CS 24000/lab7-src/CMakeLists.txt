cmake_minimum_required(VERSION 3.1)
project(lab7_src)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

set(SOURCE_FILES
    entry.c
    hello.c
    panned.c
    radio.c
    talk-client.c
    TestIRCServer.c
    timer.c
    IRCClient.cc)

find_package (PkgConfig REQUIRED)
pkg_check_modules (GTK2 REQUIRED gtk+-2.0)

include_directories (${GTK2_INCLUDE_DIRS})
link_directories (${GTK2_LIBRARY_DIRS})
add_definitions (${GTK2_CFLAGS_OTHER})
target_link_libraries (gtk-test ${GTK2_LIBRARIES})

add_executable(lab7_src ${SOURCE_FILES})