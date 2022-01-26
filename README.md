# idempotency
A java library for idempotency

[![Tests](https://github.com/weksler/idempotency/actions/workflows/main.yml/badge.svg)](https://github.com/weksler/idempotency/actions/workflows/main.yml)

# Building
1. Make a copy of `bazelrc.tmpl` into `.bazelrc`, and make sure it is appropriate for your machine.
2. run `./bazel build //...` to ensure everything is building.
3. run `./bazel test //...` to run all tests.
4. Import into intellij using the bazel plugin.

# Demo Application
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
