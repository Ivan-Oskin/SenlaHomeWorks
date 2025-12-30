Select DISTINCT p1.model, p2.model, p1.speed, p1.ram from pc p1
join pc p2 on p1.speed = p2.speed
and p1.ram = p2.ram
and p1.code > p2.code 
