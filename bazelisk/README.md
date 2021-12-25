# Bazelisk

**A user-friendly launcher for Bazel.**

## About Bazelisk

Bazelisk is a wrapper for Bazel written in Go.
It automatically picks a good version of Bazel given your current working directory, downloads it from the official server (if required) and then transparently passes through all command-line arguments to the real Bazel binary.
You can call it just like you would call Bazel.

## Installation

On MacOS, you can `brew install bazelisk`. This adds both `bazelisk` and `bazel` to the `PATH`.

Binary and source releases are provided on our [Releases](https://github.com/bazelbuild/bazelisk/releases) page.
You can download one of these and add it to your `PATH` manually.

Bazelisk is also published to npm.
Frontend developers may want to install it with `npm install -g @bazel/bazelisk`.

> You will notice that it serves an analogous function for Bazel as the
> [`nvm` utility](https://github.com/nvm-sh/nvm) which manages your version of Node.js.

Some ideas how to use it:
- Install it as the `bazel` binary in your `PATH` (e.g. copy it to `/usr/local/bin/bazel`).
  Never worry about upgrading Bazel to the latest version again.
- Check it into your repository and recommend users to build your software via `./bazelisk build //my:software`.
  That way, even someone who has never used Bazel or doesn't have it installed can build your software.
- As a company using Bazel or as a project owner, add a `.bazelversion` file to your repository.
  This will tell Bazelisk to use the exact version specified in the file when running in your workspace.
  The fact that it's versioned inside your repository will then allow for atomic upgrades of Bazel including all necessary changes.
  If you install Bazelisk as `bazel` on your CI machines, too, you can even test Bazel upgrades via a normal presubmit / pull request.
  It will also ensure that users will not try to build your project with an incompatible version of Bazel, which is often a cause for frustration and failing builds. (But see the note below about ensuring your developers install Bazelisk.)

Before Bazelisk was rewritten in Go, it was a Python script.
This still works and has the advantage that you can run it on any platform that has a Python interpreter, but is currently unmaintained and it doesn't support as many features.
The documentation below describes the newer Go version only.

## How does Bazelisk know which Bazel version to run?

It uses a simple algorithm:
- If the environment variable `USE_BAZEL_VERSION` is set, it will use the version specified in the value.
- Otherwise, if a `.bazeliskrc` file exists in the workspace root and contains the `USE_BAZEL_VERSION` variable, this version will be used.
- Otherwise, if a `.bazelversion` file exists in the current directory or recursively any parent directory, it will read the file and use the version specified in it.
- Otherwise it will use the official latest Bazel release.

A version can optionally be prefixed with a fork name.
The fork and version should be separated by slash: `<FORK>/<VERSION>`.
Please see the next section for how to work with forks.

Bazelisk currently understands the following formats for version labels:
- `latest` means the latest stable (LTS) version of Bazel as released on GitHub.
  Previous releases can be specified via `latest-1`, `latest-2` etc.
- A version number like `0.17.2` means that exact version of Bazel.
  It can also be a release candidate version like `0.20.0rc3`, or a rolling release version like `5.0.0-pre.20210317.1`.
- A floating version identifier like `4.x` that returns the latest release from the LTS series started by Bazel 4.0.0.
- The hash of a Git commit. Please note that Bazel binaries are only available for commits that passed [Bazel CI](https://buildkite.com/bazel/bazel-bazel).

Additionally, a few special version names are supported for our official releases only (these formats do not work when using a fork):
- `last_green` refers to the Bazel binary that was built at the most recent commit that passed [Bazel CI](https://buildkite.com/bazel/bazel-bazel).
  Ideally this binary should be very close to Bazel-at-head.
- `last_downstream_green` points to the most recent Bazel binary that builds and tests all [downstream projects](https://buildkite.com/bazel/bazel-at-head-plus-downstream) successfully.
- `last_rc` points to the most recent release candidate.
  If there is no active release candidate, Bazelisk uses the latest Bazel release instead.
- `rolling` refers to the latest rolling release (even if there is a newer LTS release).

## Where does Bazelisk get Bazel from?

By default Bazelisk retrieves Bazel releases, release candidates and binaries built at green commits from Google Cloud Storage.

As mentioned in the previous section, the `<FORK>/<VERSION>` version format allows you to use your own Bazel fork hosted on GitHub:

If you want to create a fork with your own releases, you have to follow the naming conventions that we use in `bazelbuild/bazel` for the binary file names.
The URL format looks like `https://github.com/<FORK>/bazel/releases/download/<VERSION>/<FILENAME>`.

You can also override the URL by setting the environment variable `$BAZELISK_BASE_URL`. Bazelisk will then append `/<VERSION>/<FILENAME>` to the base URL instead of using the official release server.

## Ensuring that your developers use Bazelisk rather than Bazel

Bazel installers typically provide Bazel's [shell wrapper script] as the `bazel` on the PATH.

When installed this way, Bazel checks the `.bazelversion` file itself, but the failure when it mismatches with the actual version of Bazel can be quite confusing to developers.
You may find yourself having to explain the difference between Bazel and Bazelisk (especially when you upgrade the pinned version).
To avoid this, you can add a check in your `tools/bazel` wrapper.
Since Bazelisk is careful to avoid calling itself in a loop, it always calls the wrapper with the environment variable `BAZELISK_SKIP_WRAPPER` set to `true'.
You can check for the presence of that variable, and when not found, report a useful error to your users about how to install Bazelisk.

Note that if users directly downloaded a Bazel binary and put it in their PATH, rather than running
an installer, then `tools/bazel` and `.bazelversion` are not checked. You could call the
[versions.check](https://github.com/bazelbuild/bazel-skylib/blob/1.1.1/docs/versions_doc.md#versionscheck) starlark module from the beginning of your WORKSPACE to
require users update their bazel.

[shell wrapper script]: https://github.com/bazelbuild/bazel/blob/master/scripts/packages/bazel.sh
## Other features

The Go version of Bazelisk offers two new flags.

`--strict` expands to the set of incompatible flags which may be enabled for the given version of Bazel.

```shell
bazelisk --strict build //...
```

`--migrate` will run Bazel multiple times to help you identify compatibility issues.
If the code fails with `--strict`, the flag `--migrate` will run Bazel with each one of the flag separately, and print a report at the end.
This will show you which flags can safely enabled, and which flags require a migration.

You can set `BAZELISK_GITHUB_TOKEN` to set a GitHub access token to use for API requests to avoid rate limiting when on shared networks.

You can set `BAZELISK_SHUTDOWN` to run `shutdown` between builds when migrating if you suspect this affects your results.

You can set `BAZELISK_CLEAN` to run `clean --expunge` between builds when migrating if you suspect this affects your results.

If `tools/bazel` exists in your workspace root and is executable, Bazelisk will run this file, instead of the Bazel version it downloaded.
It will set the environment variable `BAZEL_REAL` to the path of the downloaded Bazel binary.
This can be useful, if you have a wrapper script that e.g. ensures that environment variables are set to known good values.
This behavior can be disabled by setting the environment variable `BAZELISK_SKIP_WRAPPER` to any value (except the empty string) before launching Bazelisk.

You can control the user agent that Bazelisk sends in all HTTP requests by setting `BAZELISK_USER_AGENT` to the desired value.

# .bazeliskrc configuration file

The Go version supports a `.bazeliskrc` file in the root directory of a workspace. This file allows users to set environment variables persistently.

Example file content:


```shell
USE_BAZEL_VERSION=0.19.0
BAZELISK_GITHUB_TOKEN=abc
```

The following variables can be set:

- `BAZELISK_BASE_URL`
- `BAZELISK_CLEAN`
- `BAZELISK_GITHUB_TOKEN`
- `BAZELISK_HOME`
- `BAZELISK_SHUTDOWN`
- `BAZELISK_SKIP_WRAPPER`
- `BAZELISK_USER_AGENT`
- `USE_BAZEL_VERSION`

Please note that the actual environment variables take precedence over those in the `.bazeliskrc` file.

## Requirements

For ease of use, the Python version of Bazelisk is written to work with Python 2.7 and 3.x and only uses modules provided by the standard library.

The Go version can be compiled to run natively on Linux, macOS and Windows.
You need at least Go 1.11 to build Bazelisk, otherwise you'll run into errors like `undefined: os.UserCacheDir`.

To install the Go version, type:

```shell
go get github.com/bazelbuild/bazelisk
```
With Go 1.17 or later, the recommended way to install it is:
```shell
go install github.com/bazelbuild/bazelisk@latest
```

To add it to your PATH:

```shell
export PATH=$PATH:$(go env GOPATH)/bin
```

For more information, you may read about the [`GOPATH` environment variable](https://github.com/golang/go/wiki/SettingGOPATH).

## Ideas for the future

- Add support for checked-in Bazel binaries.
- When the version label is set to a commit hash, first download a matching binary version of Bazel, then build Bazel automatically at that commit and use the resulting binary.
- Add support to automatically bisect a build failure to a culprit commit in Bazel.
  If you notice that you could successfully build your project using version X, but not using version X+1, then Bazelisk should be able to figure out the commit that caused the breakage and the Bazel team can easily fix the problem.

## FAQ

### Where does Bazelisk store the downloaded versions of Bazel?
It creates a directory called "bazelisk" inside your [user cache directory](https://golang.org/pkg/os/#UserCacheDir) and will store them there.
Feel free to delete this directory at any time, as it can be regenerated automatically when required.
