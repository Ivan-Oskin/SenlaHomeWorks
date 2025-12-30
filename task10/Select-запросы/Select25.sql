select DISTINCT maker from product
join pc on product.model = pc.model
where maker in (select maker from product where type = 'Printer')
and pc.ram = (select min(ram) from pc)
and pc.speed = (select max(speed) from pc where ram = (select min(ram) from pc))
