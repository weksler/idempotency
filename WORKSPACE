workspace(name = "idempotency")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
# rules_java defines rules for generating Java code from Protocol Buffers.
http_archive(
    name = "rules_java",
    url = "https://github.com/bazelbuild/rules_java/releases/download/4.0.0/rules_java-4.0.0.tar.gz",
    sha256 = "34b41ec683e67253043ab1a3d1e8b7c61e4e8edefbcad485381328c934d072fe",
)

load("@bazel_tools//tools/build_defs/repo:jvm.bzl", "jvm_maven_import_external")
jvm_maven_import_external(
    name = "junit4",
    artifact = "junit:junit:4.13.2",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

RULES_JVM_EXTERNAL_TAG = "4.2"
RULES_JVM_EXTERNAL_SHA = "cd1a77b7b02e8e008439ca76fd34f5b07aecb8c752961f9640dea15e9e5ba1ca"

http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    sha256 = RULES_JVM_EXTERNAL_SHA,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:defs.bzl", "maven_install")

load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")
git_repository(
    name = "graknlabs_bazel_distribution",
    remote = "https://github.com/graknlabs/bazel-distribution",
    commit = "e181add439dc1cfb7b1c27db771ec741d5dd43e6"
)

git_repository(
    name = "bazel_skylib",
    remote = "https://github.com/bazelbuild/bazel-skylib",
    commit = "b2ed61686ebca2a44d44857fef5b3e1d31cc2483"
)

load("@rules_java//java:repositories.bzl", "rules_java_dependencies", "rules_java_toolchains")
rules_java_dependencies()
rules_java_toolchains()

# rules_proto defines abstract rules for building Protocol Buffers.
http_archive(
    name = "rules_proto",
    sha256 = "66bfdf8782796239d3875d37e7de19b1d94301e8972b3cbd2446b332429b4df1",
    strip_prefix = "rules_proto-4.0.0",
    urls = [
        "https://mirror.bazel.build/github.com/bazelbuild/rules_proto/archive/refs/tags/4.0.0.tar.gz",
        "https://github.com/bazelbuild/rules_proto/archive/refs/tags/4.0.0.tar.gz",
    ],
)
load("@rules_proto//proto:repositories.bzl", "rules_proto_dependencies", "rules_proto_toolchains")
rules_proto_dependencies()
rules_proto_toolchains()

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_jar")
http_jar(
  name = "lombok_jar",
  url = "https://projectlombok.org/downloads/lombok-1.18.16.jar",
  sha256 = "7206cbbfd6efd5e85bceff29545633645650be58d58910a23b0d4835fbd15ed7"
)

new_local_repository(
  name = "lombok",
  path = "lombok/",
  build_file = "lombok/BUILD.bazel"
)

jvm_maven_import_external(
    name = "log4j",
    artifact = "org.apache.logging.log4j:log4j-api:jar:2.17.1",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)
