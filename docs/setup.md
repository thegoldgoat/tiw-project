# Setup

## MySQL

### Create Users

1. `CREATE USER 'testing'@'%' IDENTIFIED BY 'testing';`
2. `GRANT ALL PRIVILEGES ON dbfortests.* TO 'testing'@'%';`
3. `CREATE USER 'servlets'@'%' IDENTIFIED BY 'servlets';`
4. ` GRANT ALL PRIVILEGES ON gallery.* TO 'servlets'@'%';`

### Create Tables

1. `CREATE DATABASE gallery;`
2. `USE gallery`
3. Run `src/test/resources/tables.sql`
