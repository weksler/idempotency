# idempotency
A java library for idempotency

[![Tests](https://github.com/weksler/idempotency/actions/workflows/main.yml/badge.svg)](https://github.com/weksler/idempotency/actions/workflows/main.yml)

# Building
1. Make a copy of `bazelrc.tmpl` into `.bazelrc`, and make sure it is appropriate for your machine.
2. run `./bazel build //...` to ensure everything is building.
3. run `./bazel test //...` to run all tests.
4. Import into intellij using the bazel plugin.

## Adding dependencies
Dependencies are pinned in the `maven-deps_install.json` file, which is managed by a set of blaze commands.
Don't edit the file directly!

To add a dependency on a library hosted on maven, add it to the `maven-deps` target at the bottom of `WORKSPACE.bazel`, and then run the following command:
```shell
bazel run @unpinned_maven-deps//:pin
```
You should check in the updated `maven-deps_install.json` file.

For more information on pinning dependencies, see [the documentation](https://github.com/bazelbuild/rules_jvm_external ).
# Demo Application
## Running
To run the demo application, use the following command:
```shell
./bazel run //src/main/java/com/bablooka/idempotency/application:application
```
## Database
### Writing migrations
Migrations need to be put in the `resources/db/sql` directory,
and should conform to the pattern
```
Vxxxx__name_of_migration.sql
```
Where `xxxx` is a monotonically increasing number starting at 0001.

After you write a migration, import the project into intellij using the bazel
plugin and your code should auto generate jOOQ classes to match the new or modified migrations.

### Migrating the dev database
Use ` ./bazel run //scripts:migrate_db ` to migrate the dev database.
The dev database location is provided by the `DEVELOPMENT_DB_DIRECTORY`
and `DEVELOPMENT_DB_NAME` parameters set in `.bazelrc`
