select maker, max(pc.price) from product
join pc on product.model = pc.model
where type = 'PC'
Group By maker;
