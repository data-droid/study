# Trie
* Retrieval Tree
* 빠르게 문자열을 검색할 수 있는 자료구조!
* 활용!! 검색어 자동완성에서 사용가능한 자료구조!
![](https://upload.wikimedia.org/wikipedia/commons/thumb/b/be/Trie_example.svg/375px-Trie_example.svg.png)

## Radix Trie
* Trie의 압축버전!
![](https://t1.daumcdn.net/cfile/tistory/1578164050B05ABB12)

## c Trie 구현! ([참고](https://jason9319.tistory.com/129))
```c
struct Trie {
  bool check;
  Trie* next[26]; //알파벳
  Trie() : check(false) {
    memset(next,0,sizeof(next));
  }
  ~Trie() {
    for(int i=0; i<26; i++) {
      if(next[i])
        delete next[i];
    }
  }
  
  void insert(const char* query) {
    if(*query == '\0')
      check = true;
    else {
      if(next[*query-'A'] == NULL)
        next[*query-'A'] = new Trie();
      next[*query-'A']->insert(query+1);
    }
  }
  
  Trie* find(const char* query) {
    if(*query == '\0')
      return this;
    if (next[*query-'A'] == NULL)
      return NULL;
    return next[*query-'A']->find(query+1)
  }
  
```
