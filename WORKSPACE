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

jvm_maven_import_external(
    name = "maven-lombok",
    artifact = "org.projectlombok:lombok:1.18.20",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-mockito",
    artifact = "org.mockito:mockito-core:4.2.0",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-mockito-inline",
    artifact = "org.mockito:mockito-inline:4.2.0",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-bytebuddy",
    artifact = "net.bytebuddy:byte-buddy:1.12.6",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-bytebuddy-agent",
    artifact = "net.bytebuddy:byte-buddy-agent:1.12.6",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-guava",
    artifact = "com.google.guava:guava:31.0.1-jre",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-sqlite-jdbc",
    artifact = "org.xerial:sqlite-jdbc:3.34.0",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-jooq",
    artifact = "org.jooq:jooq:3.16.2",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-r2dbc-spi",
    artifact = "io.r2dbc:r2dbc-spi:0.9.0.RELEASE",
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

jvm_maven_import_external(
    name = "log4j-api",
    artifact = "org.apache.logging.log4j:log4j-api:jar:2.17.1",
    artifact_sha256 = "b0d8a4c8ab4fb8b1888d0095822703b0e6d4793c419550203da9e69196161de4",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "log4j-core",
    artifact = "org.apache.logging.log4j:log4j-core:jar:2.17.1",
    artifact_sha256 = "c967f223487980b9364e94a7c7f9a8a01fd3ee7c19bdbf0b0f9f8cb8511f3d41",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

DAGGER_TAG = "2.40.5"
DAGGER_SHA = "5a6923e56edbc1e34c8089ecab5338a1b8ddb79a3a54b6c86cdcf31212680d32"
http_archive(
    name = "dagger",
    strip_prefix = "dagger-dagger-%s" % DAGGER_TAG,
    sha256 = DAGGER_SHA,
    urls = ["https://github.com/google/dagger/archive/dagger-%s.zip" % DAGGER_TAG],
)

load("@dagger//:workspace_defs.bzl", "DAGGER_ARTIFACTS", "DAGGER_REPOSITORIES")

maven_install(
  artifacts = DAGGER_ARTIFACTS,
  repositories = DAGGER_REPOSITORIES,
)
