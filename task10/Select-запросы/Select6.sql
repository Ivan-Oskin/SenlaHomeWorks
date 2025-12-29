select product.maker, speed from laptop
join product on laptop.model = product.model
where hd > 100;
