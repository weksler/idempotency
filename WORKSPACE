load("@bazel_tools//tools/build_defs/repo:jvm.bzl", "jvm_maven_import_external")
load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")


jvm_maven_import_external(
    name = "junit4",
    artifact = "junit:junit:4.13.2",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

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


