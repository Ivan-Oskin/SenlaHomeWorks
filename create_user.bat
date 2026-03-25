@echo off
psql -U postgres -c "CREATE USER bank_admin WITH PASSWORD 'Admin';"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE bankdb TO bank_admin;"
psql -U postgres -d bankdb -c "GRANT CREATE ON SCHEMA public TO bank_admin;"
psql -U postgres -d bankdb -c "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO bank_admin;"
psql -U postgres -d bankdb -c "GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO bank_admin;"
psql -U postgres -d bankdb -c "ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO bank_admin;"
psql -U postgres -d bankdb -c "ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO bank_admin;"

pause