INSERT INTO tests.user (username, password, role, is_blocked, is_enabled)
VALUES('user1@gmail.com','user1', 'USER', FALSE, TRUE);
INSERT INTO tests.user (username, password, role, is_blocked, is_enabled)
VALUES('user2@gmail.com','user2', 'USER', FALSE, TRUE);

INSERT INTO tests.shopping_list (name, created_by_id, created)
VALUES('Biedronka', 1, '2024-05-20 15:30:00');
INSERT INTO tests.shopping_list (name, created_by_id, created)
VALUES('Lidl', 1, '2024-03-20 15:30:00');
INSERT INTO tests.shopping_list (name, created_by_id, created)
VALUES('Aldi', 1, '2024-04-20 15:30:00');
INSERT INTO tests.shopping_list (name, created_by_id, created)
VALUES('Castorama', 2, '2024-02-20 15:30:00');

INSERT INTO tests.share (shopping_list_id, user_with_share_id)
VALUES(1, 2);
INSERT INTO tests.share (shopping_list_id, user_with_share_id)
VALUES(3, 2);

INSERT INTO tests.item (name, quantity, unit, description, is_bought, shopping_list_id)
VALUES("paprika", 2, "kg", "red", FALSE, 1);
INSERT INTO tests.item (name, quantity, unit, description, is_bought, shopping_list_id)
VALUES("milk", 3, "L", "2%", FALSE, 1);
INSERT INTO tests.item (name, quantity, unit, description, is_bought, shopping_list_id)
VALUES("banana", 1.5, "kg", "", FALSE, 1);