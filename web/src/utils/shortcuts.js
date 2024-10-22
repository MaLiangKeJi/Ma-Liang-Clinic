export default[
    {
      text: "今天",
      value: () => {
        const start = new Date();
        const end = new Date();

        end.setDate(end.getDate()+1);
        return [start, end];
      },
    },
    {
      text: "昨天",
      value: () => {
        const start = new Date();
        const end = new Date();
        start.setHours(0);
        start.setMinutes(0);
        start.setSeconds(0);
        start.setMilliseconds(0);
        end.setHours(23);
        end.setMinutes(59);
        end.setSeconds(59);
        end.setMilliseconds(0);
        start.setTime(start.getTime() - 3600 * 1000 * 24 * 1);
        return [start, end - (60 * 60 * 24 - 1) * 1000];
      },
    },
    {
      text: "前三天",
      value: () => {
        const start = new Date();
        const end = new Date();
        start.setHours(0);
        start.setMinutes(0);
        start.setSeconds(0);
        start.setMilliseconds(0);
        end.setHours(23);
        end.setMinutes(59);
        end.setSeconds(59);
        end.setMilliseconds(0);
        start.setTime(start.getTime() - 3600 * 1000 * 24 * 3);
        end.setTime(end.getTime() - 3600 * 1000 * 24 * 1);
        return [start, end - (60 * 60 * 24 - 1) * 1000];
      },
    },
    {
      text: "本周内",
      value: () => {
        const start = new Date();
        const end = new Date();

        start.setHours(0);
        start.setMinutes(0);
        start.setSeconds(0);
        start.setMilliseconds(0);

        const nows = start.getDay() || 7;
        start.setTime(start.getTime() - 3600 * 1000 * 24 * (nows - 1));
        return [start, end - (60 * 60 * 24 - 1) * 1000];
      },
    },
    {
      text: "本月内",
      value: () => {
        const start = new Date();
        const end = new Date();

        start.setHours(0);
        start.setMinutes(0);
        start.setSeconds(0);
        start.setMilliseconds(0);

        start.setDate(1);
        return [start, end - (60 * 60 * 24 - 1) * 1000];
      },
    },
    {
      text: "本季内",
      value: () => {
        const currentDate = new Date();
        const currentYear = currentDate.getFullYear();
        const currentMonth = currentDate.getMonth();
        let start = null;
        let end = new Date();
        if (0 <= currentMonth && currentMonth <= 2) {
          start = new Date(currentYear, 0, 1);
        } else if (3 <= currentMonth && currentMonth <= 5) {
          start = new Date(currentYear, 3, 1);
        } else if (6 <= currentMonth && currentMonth <= 8) {
          start = new Date(currentYear, 6, 1);
        } else if (9 <= currentMonth && currentMonth <= 11) {
          start = new Date(currentYear, 9, 1);
        }

        return [start, end - (60 * 60 * 24 - 1) * 1000];
      },
    },
    {
      text: "一周内",
      value: () => {
        const end = new Date();
        const start = new Date();
        start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
        return [start, end];
      },
    },
    {
      text: "一月内",
      value: () => {
        const end = new Date();
        const start = new Date();
        start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
        return [start, end];
      },
    },
    {
      text: "三个月内",
      value: () => {
        const end = new Date();
        const start = new Date();
        start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
        return [start, end];
      },
    },
]