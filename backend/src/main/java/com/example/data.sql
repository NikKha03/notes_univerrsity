update users set email = 'Kolya@gmail.com', username = 'Kolya' where user_id = 1;
update users set email = 'Georgiy@gmail.com', username = 'Georgiy' where user_id = 2;
update users set email = 'Misha@gmail.com', username = 'Misha' where user_id = 3;


update notes set owner_id = 1, header = 'Планы на выходные', text = 'Посетить новую кофейню, прогуляться в парке, посмотреть новый фильм' where note_id = 1;

update notes set owner_id = 2, header = 'Список покупок', text = 'Молоко, яйца, овощи, фрукты, хлеб, кофе' where note_id = 2;
update notes set owner_id = 2, header = 'Идеи для подарка', text = 'Книга, подписка на стриминговый сервис, растения для дома' where note_id = 3;

update notes set owner_id = 3, header = 'Планы на отпуск', text = 'Посетить пляжи, попробовать местную кухню, сделать экскурсию по достопримечательностям' where note_id = 4;
update notes set owner_id = 3, header = 'Список задач на неделю', text = 'Сделать уборку, погулять каждый день, начать новую книгу, заняться йогой' where note_id = 5;
update notes set owner_id = 3, header = 'Идеи для ужина', text = 'Паста с томатным соусом, курица с овощами на гриле, салат Цезарь' where note_id = 6;
