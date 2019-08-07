# Event
* 스케쥴러 이벤트 기능
* 일간 summary 등에 유용함!

### Global Variable 확인 및 변경
```SQL
SHOW VARIABLES LIKE 'event%';
SET GLOBAL event_scheduler=ON;
```

### Event Scheduler 확인
```SQL
SELECT * FROM information_schema.events;
```

### Create Event Scheduler
```SQL
CREATE 
    EVENT 이벤트이름 ON SCHEDULE 주기(EVERY 1 DAY) STARTS 시작일('2019-08-06 00:00:00')
    DO
     수행할 작업!
```

### Alter Event Scheduler
```SQL
ALTER 
    EVENT 이벤트이름 ON SCHEDULE 주기(EVERY 1 DAY) STARTS 시작일('2019-08-06 00:00:00')
    DO
     수행할 작업!
```

### Drop Event Scheduler
```SQL
DROP
    EVENT 이벤트 이름;
```