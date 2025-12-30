select maker, printer.price from product
join printer on product.model = printer.model
where printer.price = (select min(price) from printer where color = 'y')