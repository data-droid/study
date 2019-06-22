# KMP 알고리즘
* 문장속의 단어찾기!

알고리즘 문제에 빈번하게 나오며, Ctrl + f를 통해 자주 문서 속 단어를 찾아본 경험이 있다. 해당 단어를 찾기 위해서는 문서의 한글자씩 이동하면서 원하는 단어 길이만큼 반복하면서 찾아야 함. 즉, 문서(m), 단어(n)의 복잡도는 O(m*n)이 된다.

이러한 문제를 O(m+n)으로 해결가능하게 해주는 것이 바로 KMP 알고리즘 이다. KMP 알고리즘은 만든 사람의 Knuth, Morris, Prett를 따서 만들었다.

## 얼마나 좋아지는가?
* 문장 : "ABCDABCDABEE" / 찾을 단어 : "ABCDABE"
    * 일반적인 풀이
        * ```
            ABCDABCDABEE
            ABCDABE

            ABCDABCDABEE
            -ABCDABE

            ABCDABCDABEE
            --ABCDABE

            ABCDABCDABEE
            ---ABCDABE
            
            ABCDABCDABEE
            ----ABCDABE
            
            ABCDABCDABEE
            -----ABCDABE
          ```
    
    * KMP의 풀이
        * ```
            ABCDABCDABEE
            ABCDABE
            
            ABCDABCDABEE
            ----ABCDABE
          ```

## KMP 만들기
* 실패함수 pi배열
    * KMP를 사용하기 위해서는 찾을 단어(패턴)에 미리 연산이 필요함.
    * pi 배열은 문장과 패턴을 비교하는데 틀릴 경우 인덱스를 얼마나 스킵할지 미리 계산해 둔 것.
    * pi 배열은 각 패턴의 길이별 prefix와 suffix가 같은 부분이 몇개인지 정의한 것.
        * ```
            //ABCDABE -> pi=[0,0,0,0,1,2,0]
            A
            0
            
            AB
            0
            
            ABC
            0
            
            ABCD
            0
            
            ABCDA
            1
            
            ABCDAB
            2
            
            ABCDABE
            0
          ```

    * ```cpp
    vector<int> getPI(string pattern) {
        vector<int> result(pattern.length(),0);
        int j=0;
        for(int i=1; i<pattern.length(); i++) {
            while(j>0 && pattern[i]!=pattern[j])
                j = pi[j-1];
            if(pattern[i] == pattern[j])
                pi[i] = ++j;
        }
        return result;
    }
     ```
    * 위의 코드가지고 다른 예를 보자!
        * ```
        //BCBCBCBA
        [0]
    
        BCBCBCBA
        -BCBCBCB
        [0,0]
        
        BCBCBCBA
        --BCBCBC
        [0,0,1]
    
        BCBCBCBA
        --BCBCBC
        [0,0,1,2]
    
        BCBCBCBA
        --BCBCBC
        [0,0,1,2,3]
    
        BCBCBCBA
        --BCBCBC
        [0,0,1,2,3,4]
    
        BCBCBCBA
        --BCBCBC
        [0,0,1,2,3,4,5]
    
        BCBCBCBA
        --BCBCBC
        BCBCBCBA
        ----BCBC
        BCBCBCBA
        ------BC
        BCBCBCBA
        -------B
        [0,0,1,2,3,4,5,0]
        ```
* 구현
```cpp
    vector<int> KMP(string str, string pattern) {
        vector<int> result;
        vector<int> pi = getPI(pattern);
        int j=0;
        for(int i=0; i<str.length(); i++){
            while(j>0 && str[i] != pattern[j])
                j = pi[j-1];
            if(str[i] == pattern[j]) {
                if(j==pattern.length()-1){
                    result.push_back(i-pattern.length()+1);
                    j = pi[i];
                }
                else
                    j++;
            }
        }
        return result;
    }
```
* 위 함수를 이용하여 str 문장에서 pattern의 각 첫번째 인덱스들을 구할 수 있다! 
* 위에 내용이 이해가면 [리트코드(28번)](https://leetcode.com/problems/implement-strstr/)을 풀어보자!
