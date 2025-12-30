select
(select model from laptop where price = (select max(price) from laptop)),
(select model from pc where price = (select max(price) from pc)),
(select model from printer where price = (select max(price) from printer))