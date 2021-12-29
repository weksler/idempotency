load("@rules_java//java:defs.bzl", "java_library")
load("@graknlabs_bazel_distribution//maven/templates:rules.bzl", "deploy_maven", "assemble_maven")

java_library(
    name = "idempotency-core",
    srcs = [
        "//src/main/java/com/bablooka/idempotency/core:sources",
#        "//src/main/java/com/bablooka/idempotency/db:sources",
    ],
    tags = ["maven_coordinates=com.bablooka:idempotency-core:{pom_version}", "manual"],
    visibility = ["//:__subpackages__"],
)

assemble_maven(
    name = "assemble-maven",
    target = "//:idempotency-core",
    package = "{maven_packages}",
    project_name = "Idempotency Core",
    project_description = "Idempotecy - Java library for idempotency",
    project_url = "https://github.com/weksler/idempotecy",
    scm_url = "https://github.com/weksler/idempotency.git",
    version_file = "//:VERSION",
    developers = {"1": ["name=Michael Weksler", "email=m...@b...m", "organization=Bablooka"]},
    license = "mit"
)

deploy_maven(
    name = "deploy-maven",
    target = ":assemble-maven",
    deployment_properties = "//:deployment.properties"
)
