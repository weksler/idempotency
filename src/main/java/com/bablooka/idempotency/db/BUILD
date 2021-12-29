load("@rules_java//java:defs.bzl", "java_library")

java_library(
    name = "db",
    srcs = glob(["*.java"]),
    visibility = ["//visibility:public"],
)

filegroup(
   name = "sources",
   srcs = glob(["*.java"]),
   visibility = ["//:__subpackages__"],
)
