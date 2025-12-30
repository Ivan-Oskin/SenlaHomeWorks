select maker, avg(laptop.screen) from product
join laptop on laptop.model = product.model
where type = 'Laptop'
Group by maker