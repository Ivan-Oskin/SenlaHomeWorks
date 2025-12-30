select laptop.model, laptop.speed, product.type from laptop
join product on laptop.model = product.model
where laptop.speed < ALL(select speed from pc)