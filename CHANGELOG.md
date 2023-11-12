# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

Format:

```
## [MAJOR.MINOR.PATCH] - DD.MM.YYYY - @user
### Added
- xyz
### Changed
- xyz
### Removed
- xyz
```

## [Unreleased]

## [0.0.1] - 12.11.2023 - @derghust
### Added
- Doobie database implementation for queries for:
  - `User` - id, username
  - `Subscription` - id, counter, entity_id, user_id
  - `Entities` - id, label, description
- Docker compose for postgres sql database for further development.
