load("@rules_java//java:defs.bzl", "java_test")

java_test(
    name = "idempotency",
    srcs = glob(["*.java"]),
    test_class = "com.bablooka.idempotency.IdempotencyTest",
    deps = [
        "//src/main/java/com/bablooka/idempotency:idempotency",
        "@junit4",
    ],
)