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
    artifact_sha256 = "8e495b634469d64fb8acfa3495a065cbacc8a0fff55ce1e31007be4c16dc57d3",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-lombok",
    artifact = "org.projectlombok:lombok:1.18.20",
    artifact_sha256 = "ce947be6c2fbe759fbbe8ef3b42b6825f814c98c8853f1013f2d9630cedf74b0",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-mockito",
    artifact = "org.mockito:mockito-core:4.2.0",
    artifact_sha256 = "8dba45f02aafe51d544a17d594f467d0f7b42ca82432d8bee2c29e5a36be4b21",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-mockito-inline",
    artifact = "org.mockito:mockito-inline:4.2.0",
    artifact_sha256 = "ee52e1c299a632184fba274a9370993e09140429f5e516e6c5570fd6574b297f",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-bytebuddy",
    artifact = "net.bytebuddy:byte-buddy:1.12.6",
    artifact_sha256 = "211918dc24f0fdef4335ce8af40ef5616e15e818b962a21146397c7701eb75a7",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-bytebuddy-agent",
    artifact = "net.bytebuddy:byte-buddy-agent:1.12.6",
    artifact_sha256 = "9b29421fe4650b75fc3ed53590f914c54f932e334b3506cc00296dff73024183",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-guava",
    artifact = "com.google.guava:guava:31.0.1-jre",
    artifact_sha256 = "d5be94d65e87bd219fb3193ad1517baa55a3b88fc91d21cf735826ab5af087b9",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-sqlite-jdbc",
    artifact = "org.xerial:sqlite-jdbc:3.34.0",
    artifact_sha256 = "605979c94e7fe00437f1e10dcfa657a23f125c8eb4d2f0ec17e3f84613894cc3",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-jooq",
    artifact = "org.jooq:jooq:3.16.2",
    artifact_sha256 = "7c439f00bffbe8f7719300150b25a759a1a12db5c439eb883d19e94120291fff",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-jooq-meta",
    artifact = "org.jooq:jooq-meta:3.16.2",
    artifact_sha256 = "2f757f7984eec71911908ea01aaf849cf9528137a13a23c0ca588378c97f88ca",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-jooq-codegen",
    artifact = "org.jooq:jooq-codegen:3.16.2",
    artifact_sha256 = "c49cd9fd3e56524f479d52ceea2350d28c988b9ccd31eaecf430c4b19b4c7cd8",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-r2dbc-spi",
    artifact = "io.r2dbc:r2dbc-spi:0.9.0.RELEASE",
    artifact_sha256 = "760c877790baf8529af14f87f623fb0703a72468d0d39af0651747bcdbfc49ed",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-reactive-streams",
    artifact = "org.reactivestreams:reactive-streams:1.0.3",
    artifact_sha256 = "1dee0481072d19c929b623e155e14d2f6085dc011529a0a0dbefc84cf571d865",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-flyway-core",
    artifact = "org.flywaydb:flyway-core:8.4.2",
    artifact_sha256 = "32516fbd0fe9d5330e33a454e4d3e5378157a7b627b8aec5e73902d3c3eca2d1",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-flyway-commandline",
    artifact = "org.flywaydb:flyway-commandline:8.4.2",
    artifact_sha256 = "182eadacd3f15a944b318fb803f28cc10d3eda3c9d3243352ddeb05b07ab7df8",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-gson",
    artifact = "com.google.code.gson:gson:2.8.9",
    artifact_sha256 = "d3999291855de495c94c743761b8ab5176cfeabe281a5ab0d8e8d45326fd703e",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-jaxb-api",
    artifact = "javax.xml.bind:jaxb-api:2.3.1",
    artifact_sha256 = "88b955a0df57880a26a74708bc34f74dcaf8ebf4e78843a28b50eae945732b06",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-jakarta-bind",
    artifact = "jakarta.xml.bind:jakarta.xml.bind-api:3.0.1",
    artifact_sha256 = "b8fb4bee3ff5b5c1ef77144d8411316018d7bbd41fcf1ede0646f7978546b867",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

jvm_maven_import_external(
    name = "maven-commons-cli",
    artifact = "commons-cli:commons-cli:1.5.0",
    artifact_sha256 = "bc8bb01fc0fad250385706e20f927ddcff6173f6339b387dc879237752567ac6",
    server_urls = ["https://repo.maven.apache.org/maven2"],
    licenses = ["notice"],
)

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
