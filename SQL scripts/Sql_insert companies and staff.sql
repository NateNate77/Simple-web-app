-- Заполнение таблицы организаций

INSERT INTO public."Companies"(
"Name", "HeadCompanyID")
	VALUES ('СИТЭК', null),
        ('АВИТЕК', null),
		('Bucher', null),
		('ООО НОВАТОР', null),	
		('ВесГрупп', null),
		('KOROTA', null),
		('Агро-Деталь', null);

-- Заполнение таблицы сотрудников

INSERT INTO public."Staff"(
"Name", "BossID", "CompanyID")
	VALUES('Сонин Игорь', null, (Select "ID" from public."Companies" Where "Name" = 'АВИТЕК')),
	('Тимиряев Ефим', null, (Select "ID" from public."Companies" Where "Name" = 'АВИТЕК')),
	('Маслак Якуб', null, (Select "ID" from public."Companies" Where "Name" = 'АВИТЕК')),
	('Долженко Демьян', null, (Select "ID" from public."Companies" Where "Name" = 'АВИТЕК')),
	('Юханцев Константин', null, (Select "ID" from public."Companies" Where "Name" = 'АВИТЕК')),
	('Янукович Роман', null, (Select "ID" from public."Companies" Where "Name" = 'АВИТЕК')),
	('Куратник Кондрат', null, (Select "ID" from public."Companies" Where "Name" = 'ООО НОВАТОР')),
	('Репин Панкратий', null, (Select "ID" from public."Companies" Where "Name" = 'ООО НОВАТОР')),
	('Шипицына Эмилия', null, (Select "ID" from public."Companies" Where "Name" = 'ООО НОВАТОР')),
	('Гальцева Ангелина', null, (Select "ID" from public."Companies" Where "Name" = 'ООО НОВАТОР')),
	('Катасонова Римма', null, (Select "ID" from public."Companies" Where "Name" = 'KOROTA')),
	('Судленкова Кристина', null, (Select "ID" from public."Companies" Where "Name" = 'KOROTA')),
	('Сиянко Ефросинья', null, (Select "ID" from public."Companies" Where "Name" = 'KOROTA')),
	('Кярбера Зоя', null, (Select "ID" from public."Companies" Where "Name" = 'KOROTA')),
	('Ивашова Инна', null, (Select "ID" from public."Companies" Where "Name" = 'KOROTA')),
	('Носкова Александра', null, (Select "ID" from public."Companies" Where "Name" = 'Bucher')),
	('Бобрик Эрнест', null, (Select "ID" from public."Companies" Where "Name" = 'Bucher')),
	('Строганов Артур', null, (Select "ID" from public."Companies" Where "Name" = 'Bucher')),
	('Мазаев Назар', null, (Select "ID" from public."Companies" Where "Name" = 'Bucher')),
	('Созонтов Фадей', null, (Select "ID" from public."Companies" Where "Name" = 'ВесГрупп')),
	('Яцышина Агния', null, (Select "ID" from public."Companies" Where "Name" = 'ВесГрупп')),
	('Золина Эмма', null, (Select "ID" from public."Companies" Where "Name" = 'ВесГрупп'));


		