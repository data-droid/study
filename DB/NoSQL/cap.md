 # CAP
 * CAP 정리, 브루어의 정리라 불림.
 * 세 가지 조건을 모두 만족하는 분산 시스템 존재하지 않음!
 * 즉, Trade Off 관계!
 ![](http://eincs.com/images/2013/06/truth-of-cap-theorem-diagram.png)
 
 #
 * Consistency(일관성) 
   * 모든 노드가 같은 순간에 같은 데이터를 볼 수 있음. 
   * 서로 다른 노드에서 데이터 조회시 버전이 항상 일치, 최신 데이터를 보장.
 * Availability(가용성) 
   * 모든 요청이 성공 또는 실패 결과를 반환할 수 있음. 
   * 모든 요청에 최신 데이터가 아니더라도 에러가 아닌 데이터를 반환.
 * Partition Tolerance(분할내성)
   * 일부 메시지가 전달되지 않거나, 시스템 일부가 망가져도 시스템이 동작.
  
 
