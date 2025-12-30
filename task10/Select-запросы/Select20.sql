select maker, count(*) from product
where type = 'PC'
Group by maker
HAVING COUNT(*) > 2