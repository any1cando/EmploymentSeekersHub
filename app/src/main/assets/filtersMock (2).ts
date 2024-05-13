interface CheckBoxData {
	title: string;
	isCheck?: boolean;
}

interface RangeData {
	from: string;
	to: string;
}

// я ожидаю
const filtersMockForResponse = [
	{
		title: "Test",
		filters: [
			{
				type: FiltersType.checkBox,
				data: [
					{ title: "test", isCheck: false },
					{ title: "test23", isCheck: false },
				],
			},
		],
	},
	{
		title: "Test23",
		filters: [
			{
				type: FiltersType.checkBox,
				data: [
					{ title: "test44", isCheck: false },
					{ title: "test55", isCheck: false },
				],
			},
		],
	},
	{
		title: "Test range",
		filters: [{ type: FiltersType.range, data: { from: "0", to: "10" } }],
	},
];

// если выбраны фильтры отправляю следующе
const filtersMockForRequst = [
	{
		title: "Test",
		filters: [
			{
				type: FiltersType.checkBox,
				data: [{ title: "test", isCheck: true }],
			},
		],
	},
	// Если пользователь менял значения в фильтре типа range,то он добавится в фильтры(массив,который я тебе отсылаю в запросах),если нет,то они не добавляются и мы фильтруем по тем значениям,которые ты присылаешь сам
	{
		title: "Test range",
		filters: [{ type: FiltersType.range, data: { from: "0", to: "10" } }],
	},
];

// пример запроса,в который я прокидываю фильтры,в данном случае это запрос на получение вакансий,для которого в запрос в бади я доававляю следующее поле filters,так же может сюда еще будет добавлять сортировку отдельным полем(обсудим это завтра)
// getVacancies: async (filters: FiltersProps[] | []): Promise<VacnacyCardProps[] | []> =>
// 	instance.post("/vacancies", { filters }),
