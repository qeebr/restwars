# Change log

## [Unreleased] - [unreleased]
### Fixed:
- Webservice: Fixed a bug where the building metadata and technology metadata endpoint responded with 500 Server Error when passing a level smaller than 1.

### Changed
- Webservice: Improved telescope scan results. Now empty planets are included in the response, the `owner` field is `null` in that case.
- Build: Only include scope runtime when building JAR with all dependencies. This reduces the size of the assembly.
- Build: JAR with all dependencies is now built on package phase instead of install phase.

## [0.1.0] - 2015-02-21
### Fixed
- Started writing a changelog.