# Headwind MDM Server Development Guide

## Project Overview
Headwind MDM is an open-source Mobile Device Management platform for Android devices. The server component is built with Java/Tomcat and uses PostgreSQL for data storage.

## Architecture

### Core Components
- `common/` - Shared utilities and base classes
- `server/` - Main web application and REST API endpoints
- `plugins/` - Extensible plugin system for additional features
- `jwt/` - JWT authentication implementation
- `notification/` - Notification service implementation

### Key Design Patterns
1. **Plugin Architecture**: All plugins follow the structure in `plugins/` with:
   - `src/main/java/com/hmdm/` - Plugin backend code
   - `src/main/webapp/` - Plugin frontend resources
   - `src/main/resources/liquibase/` - Database migrations

2. **Dependency Injection**: Uses Google Guice for DI (see `pom.xml` for setup)

3. **Database Access**: MyBatis for SQL mapping (`mybatis.version: 3.5.3`)

## Development Workflow

### Setup Requirements
- JDK 8
- Maven
- PostgreSQL
- Tomcat 8/9

### Build Commands
```bash
# Initial build
mvn install

# Development build (server module only)
cd server && mvn clean install
```

### Database Setup
1. Create PostgreSQL database and user:
```sql
CREATE USER hmdm WITH PASSWORD 'topsecret';
CREATE DATABASE hmdm WITH OWNER=hmdm;
```

2. Run installer script:
```bash
sudo ./hmdm_install.sh
```

## Testing
- Unit tests are in `src/test/java/` directories
- Integration tests use JUnit 4 (`junit.version: 4.13.1`)

## Important Files
- `server/build.properties` - Server configuration (copy from .example)
- `pom.xml` - Main project dependencies and build settings
- `plugins/*/pom.xml` - Plugin-specific configurations

## Common Patterns
1. **API Endpoints**: Place in `server/src/main/java/com/hmdm/rest/`
2. **Database Changes**: Use Liquibase changelogs in `resources/liquibase/`
3. **Plugin Development**: Follow structure in `plugins/audit/` as reference