# DV033G — Apache Commons CSV testing project

This repository contains a focused test-suite extension for Apache Commons CSV 1.14.1. The investigation
checks that `CSVFormat.RFC4180` preserves quoted content and adjacent empty fields, including a trailing empty field at 
end-of-file.

## Requirements and source versions

- Git, Maven, and a JDK on PATH. Recorded environment: Maven **3.9.1**, Java **21.0.9**, Windows10.
- Apache Commons CSV release: `rel/commons-csv-1.14.1`.
- Exact upstream commit: `e14ef86d34211f9d1b9b6040c5f6dbafc93ba907`.
- Python is needed only to repeat the optional Lizard analysis.

## Reproduce the baseline and extension

Use a new working directory with no existing `csv-test-artifacts` or `commons-csv-under-test` subdirectory.

### 1. Obtain the artifacts and pinned upstream source

~~~powershell
git clone --config core.autocrlf=input https://github.com/kjpr02/dv033g-commons-csv-project-kero2200.git csv-test-artifacts
if ($LASTEXITCODE -ne 0) { throw "Artifact clone failed" }
Set-Location .\csv-test-artifacts
$artifactRoot = (Get-Location).Path
git rev-parse HEAD

git clone --config core.autocrlf=input --branch rel/commons-csv-1.14.1 https://github.com/apache/commons-csv.git ..\commons-csv-under-test
if ($LASTEXITCODE -ne 0) { throw "Upstream clone failed" }
Push-Location ..\commons-csv-under-test
$upstreamRevision = git rev-parse HEAD
if ($upstreamRevision -ne "e14ef86d34211f9d1b9b6040c5f6dbafc93ba907") {
    throw "Unexpected upstream revision"
}
java -version
mvn -version
~~~


### 2. Run the unchanged upstream suite

~~~powershell
mvn clean test *> ..\commons-csv-baseline-reproduced.log
if ($LASTEXITCODE -ne 0) { throw "Baseline failed; inspect the log" }
Get-Content ..\commons-csv-baseline-reproduced.log -Tail 30
~~~

Expected historical result: **923 tests, 0 failures, 0 errors, 11 skipped, BUILD SUCCESS**. Investigate any difference before comparing it with the extension. The test must not be copied into the checkout until this baseline finishes.

### 3. Add the test and run the extended suite

~~~powershell
Copy-Item (Join-Path $artifactRoot "src\test\java\org\apache\commons\csv\Rfc4180FieldPreservationTest.java") .\src\test\java\org\apache\commons\csv\
mvn clean test *> ..\commons-csv-extended-reproduced.log
if ($LASTEXITCODE -ne 0) { throw "Extended suite failed; inspect the log" }
Get-Content ..\commons-csv-extended-reproduced.log -Tail 30
~~~

Expected historical result: **927 tests, 0 failures, 0 errors, 11 skipped, BUILD SUCCESS**. The log should include four passing invocations of `Rfc4180FieldPreservationTest`.

For a focused rerun while still in the upstream checkout:

~~~powershell
mvn "-Dtest=Rfc4180FieldPreservationTest" test
~~~

Return to this repository after testing:

~~~powershell
Pop-Location
~~~

## Submission reference and final tag

The uploaded test and all five evidence logs were reviewed at commit [13518b8134f99343fb23db4166aee10e406437a6](https://github.com/kjpr02/dv033g-commons-csv-project-kero2200/commit/6e2d6ebd990fc7fb99b7e3d1ecc53aaa79d2b45a).

