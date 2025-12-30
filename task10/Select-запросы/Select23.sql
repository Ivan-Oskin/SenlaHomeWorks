Select distinct maker from product
where maker in (	
	select maker from product
	join pc on product.model = pc.model
	where pc.speed > 750
) and maker in (
	select maker from product
	join laptop on product.model = laptop.model
	where laptop.speed > 750
)