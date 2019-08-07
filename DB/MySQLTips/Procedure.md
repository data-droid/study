# Stored Procedure
* 일련의 쿼리를 마치 하나의 함수처럼 실행시킴.
* 장점
    * 쿼리 보호 : 프로시저 수정하기전까지 프로시저내부의 쿼리를 확인 할 수 없음.
    * SQL 인젝션 보호
    * 권한제어
    * 일괄작업
    * 절차적인 처리 : IF ELSE WHILE
    * 동적 쿼리 생성 가능
* 단점
    * 디버깅이 어려움
    * 유지보수가 어려움
    * 짧은 쿼리에서는 비효율적
    * 낮은 처리성능.

### DELIMITER
코드블럭 내 세미콜론으로 SQL이 끊어져서 실행되는것을 막고자 블럭처리를 가능케 함.
```SQL
DELIMITER$$
    ...
$$
DELIMITER;
```

### Parameter
* [참고](https://blog.duveen.me/14)
* IN
    * 호출하는 곳에서 프로시저에 매개변수를 전달함.
    * IN 매개변수의 값은 보호됨.
        * 원본 값은 프로시저가 끝나도 유지 됨.
        * IN 매개변수의 값은 프로시저 내부에서 변경 될 수는 있음.
* OUT
    * 호출하는 곳에서 프로시저에 값 전달 X
    * 프로시저 내부에서 변경하여 호출 한 곳으로 다시 전달.
* INOUT
    * 호출하는 곳에서 값이 전달되고, 프로시저는 변수를 수정 후 호출한 곳에 새로운 값을 다시 전달.
        * 호출하는 곳에서 받은 원본 값은 프로시저가 끝나면 수정 될 수 있음.
        
```SQL
--선언
IN param_name param_type(param_size)
OUT param_name param_type(param_size)
INOUT param_name param_type(param_size)
```
    
### Create Procedure
```SQL
CREATE PROCEDURE IF EXISTS PRECEDURE_NAME (PRAMS)
BEGIN
    작업할 내용
END
```

### Drop Procedure
```SQL
Drop PROCEDURE PRECEDURE_NAME;
```

### 사용
```
CALL PROCEDURE_NAME(IN_PARAM, @OUT_PARAM, @INOUT_PARAM);
SELECT @OUT_PARAM, @INOUT_PARAM;
```

### Example
```SQL
-- 선언
DELIMITER $$
CREATE PROCEDURE test(
 IN in_test VARCHAR(25),
 OUT out_test INT
 INOUT rnk INT)
BEGIN
 SELECT count(*)
 INTO out_test
 FROM Table
 WHERE check = in_test;
 SET rnk = rnk+1;
END$$
DELIMITER ;

-- 사용
SET @rnk = 1;
CALL test(in_test, @out_test, @rnk);
SELECT @out_test,@rnk;
```

