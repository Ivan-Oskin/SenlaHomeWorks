select maker from product
where type = 'PC' and maker not IN (
	select maker from product
	where type = 'Laptop'
);