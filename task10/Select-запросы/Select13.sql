Select avg(speed) from pc
join product on pc.model = product.model
where product.maker = 'A';