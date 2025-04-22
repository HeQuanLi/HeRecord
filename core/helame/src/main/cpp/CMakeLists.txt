cmake_minimum_required(VERSION 3.4.1)

SET(lame_dir ${CMAKE_SOURCE_DIR}/lame)
# 将给定目录添加到编译器用于搜索包含文件的目录中。相对路径被解释为相对于当前源目录
# 是用来提供编译搜索头文件路径的
include_directories(${lame_dir})
#设定一个目录
aux_source_directory(${lame_dir} SRC_LIST)

set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${CMAKE_BINARY_DIR}/lib)

#确保生成的 .so 文件包含完整的调试信息和符号表
set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -g")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -g")

#生成以 libLames.so 文件文件名的so文件
add_library( # Sets the name of the library.
        Lame
        # Sets the library as a shared library.
        SHARED
        # Provides a relative path to your source file(s).
        ${SRC_LIST}
        Lame.cpp)

find_library( # Sets the name of the path variable.
        log-lib
        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

target_link_libraries( # Specifies the target library.
        Lame
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})
# 16K 对齐
target_link_options(Lame PRIVATE "-Wl,-z,max-page-size=16384")