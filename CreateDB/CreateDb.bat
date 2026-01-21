@echo off
echo Creating new_user...
psql -U postgres -c "CREATE USER carrepair_admin WITH PASSWORD 'Admin';"
echo Creating database...
psql -U postgres -c "CREATE DATABASE carrepairdb;"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE carrepairdb TO carrepair_admin;"
psql -U postgres -d carrepairdb -c "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO carrepair_admin;"
psql -U postgres -d carrepairdb -c "GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO carrepair_admin;"
psql -U postgres -d carrepairdb -c "ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO carrepair_admin;"
psql -U postgres -d carrepairdb -c "ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO carrepair_admin;"
echo Creating tables...
psql -U postgres -d carrepairdb -f ddl.sql

echo Inserting test data...
psql -U postgres -d carrepairdb -f dml.sql

echo Database setup completed!
pause
