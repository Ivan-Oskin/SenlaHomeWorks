Select speed, AVG(price::numeric) from pc
where speed > 600
group by speed;