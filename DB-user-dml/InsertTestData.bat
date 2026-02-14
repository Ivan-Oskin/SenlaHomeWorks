@echo off

echo Inserting test data...
psql -U postgres -d carrepairdb -f dml.sql

pause