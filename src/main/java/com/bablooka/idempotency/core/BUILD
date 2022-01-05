load("@rules_java//java:defs.bzl", "java_library")

load("@dagger//:workspace_defs.bzl", "dagger_rules")
dagger_rules()

java_library(
    name = "core",
    srcs = [":sources"],
    deps = [
        ":dagger",
        "@log4j",
        "@lombok//:lombok",
        "//src/main/java/com/bablooka/idempotency/proto:idempotency_record_java_proto"
    ],
    visibility = ["//visibility:public"],
)

filegroup(
   name = "sources",
   srcs = glob(["*.java"]),
   visibility = ["//:__subpackages__"],
)
