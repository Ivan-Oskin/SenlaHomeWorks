select model, speed, ram from pc 
where (cd = '12x' OR cd = '24x') and price < 600.00::money;