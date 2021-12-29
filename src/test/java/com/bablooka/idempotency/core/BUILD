load("@rules_java//java:defs.bzl", "java_test")

java_test(
    name = "core",
    srcs = glob(["*.java"]),
    test_class = "com.bablooka.idempotency.core.IdempotencyTest",
    deps = [
        "//src/main/java/com/bablooka/idempotency/core:core",
        "@junit4",
    ],
)
