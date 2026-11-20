# Evidence index

The four Maven logs match the supplied results archive after CRLF-to-LF normalization. The repository stores LF text, so checksums of the original Windows files differ even though the reported results are unchanged. Lizard output was regenerated on 20 November 2026 against the same upstream and tool revisions used in the earlier review.

## Test results

| Log | Tests reported | Failures | Errors | Skipped | Build |
|---|---:|---:|---:|---:|---|
| [commons-csv-baseline-lf.log](commons-csv-baseline-lf.log) | 923 | 0 | 0 | 11 | SUCCESS |
| [commons-csv-baseline.log](commons-csv-baseline.log) | 923 | 3 | 0 | 11 | FAILURE |
| [commons-csv-extended-lf.log](commons-csv-extended-lf.log) | 927 | 0 | 0 | 11 | SUCCESS |

Surefire totals include skips: 912 tests executed and passed in the LF baseline, and 916 in the extended run. The extended log explicitly reports four passing invocations of Rfc4180FieldPreservationTest.

[The first attempt](commons-csv-first-attempt.log) stopped at Apache RAT before tests ran. The original checkout subsequently produced three test failures. Use the successful LF baseline for comparison with the extension.

## Static analysis

[Lizard output](lizard-parser-review.log) covers CSVParser.java and Lexer.java at upstream commit e14ef86d34211f9d1b9b6040c5f6dbafc93ba907. Lizard revision: b41021bab5df757d13f9da0472a50c8d648b866e.

| Method | Cyclomatic complexity | Testing implication |
|---|---:|---|
| Lexer.parseEncapsulatedToken | 13 | Quoting and record termination have several decision paths. |
| CSVParser.nextRecord | 11 | Compare a final empty field at EOF with one followed by CRLF. |
| CSVParser.addRecordValue | 4 | Check final empty values under the selected format. |
| Lexer.nextToken | 20 | Exceeds the default warning threshold of 15; token transitions merit attention. |
| CSVParser.createHeaders | 18 | Exceeds that threshold; header handling is outside this scope. |

Complexity guides prioritization. These measurements do not establish incorrect behaviour or test coverage.

Reproduce from the pinned `commons-csv-under-test` checkout described in the [project README](../README.md), with Python and Git. Use a new sibling directory named `lizard-dv033g`:

~~~powershell
git clone https://github.com/terryyin/lizard.git ..\lizard-dv033g
git -C ..\lizard-dv033g checkout b41021bab5df757d13f9da0472a50c8d648b866e
python ..\lizard-dv033g\lizard.py src/main/java/org/apache/commons/csv/CSVParser.java src/main/java/org/apache/commons/csv/Lexer.java *> ..\lizard-parser-review-reproduced.log
~~~

The default threshold warnings are findings for prioritization; do not treat their presence as evidence that the parser fails these four tests. Lizard output is a separate static review and is not a Maven test result.

## Provenance and revision

The five logs below are preserved in reviewed artifact commit [13518b8134f99343fb23db4166aee10e406437a6](https://github.com/kjpr02/dv033g-commons-csv-project-kero2200/tree/13518b8134f99343fb23db4166aee10e406437a6/logs). The LF baseline ran on 11 November 2026 and the extended suite on 17 November 2026. The Lizard output was regenerated on 20 November 2026. New reproduction runs will have different timestamps, timings, and possibly dependency-download messages.

The table identifies the Git blob objects reviewed, independent of local checkout line-ending conversion. Inspect a stored version with `git show 13518b8134f99343fb23db4166aee10e406437a6:logs/FILE_NAME` from the artifact repository.

| Evidence file | Git blob SHA-1 |
|---|---|
| [commons-csv-baseline-lf.log](commons-csv-baseline-lf.log) | `66de3bd9cb714333bc54bfc60abac97efb0a5991` |
| [commons-csv-baseline.log](commons-csv-baseline.log) | `89d25d4d7edace262bcb5153e76e1f538d0f3b05` |
| [commons-csv-extended-lf.log](commons-csv-extended-lf.log) | `a0eada66af35a25d9c533d0e5d2a8431a44e9f36` |
| [commons-csv-first-attempt.log](commons-csv-first-attempt.log) | `91201a6a4debc1169612a9e7ea2d5f4a3fbcde69` |
| [lizard-parser-review.log](lizard-parser-review.log) | `e09aed27909da00e8d3d3fe7c2100d1f39dcef52` |
