INSERT INTO Places (id, name) VALUES
(1, 'place1'),
(2, 'place2'),
(3, 'place3');

INSERT INTO Masters (id, name) VALUES
(1, 'master1'),
(2, 'master2'),
(3, 'master3');

INSERT INTO Orders (id, name, status, timeCreate, timeStart, timeComplete, cost, place_id) VALUES
(1, 'order1', 'Active', 
 '2026-01-01 12:00:00', '2026-01-02 13:00:00', '2026-01-02 14:00:00', 5000, 1),

(2, 'order2', 'Cancel', 
 '2026-01-01 12:00:00', '2026-01-02 15:00:00', '2026-01-02 16:00:00', 2000, 2),

(3, 'order3', 'Close', 
 '2026-01-02 14:00:00', '2026-01-05 12:00:00', '2026-01-06 16:00:00', 3500, 3);

INSERT INTO OrdersByMaster (id, master_id, order_id) VALUES
(1, 1, 1), 
(2, 1, 2),  
(3, 2, 3);