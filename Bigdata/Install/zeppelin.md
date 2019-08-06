# Zeppelin Install
* Zeppelin
    * [apache zeppelin site](https://zeppelin.apache.org/download.html)

* Downloads
    * version 0.8.1 download & 압축해제 & soft link
    ```bash
$ wget http://mirror.apache-kr.org/zeppelin/zeppelin-0.8.1/zeppelin-0.8.1-bin-all.tgz
$ tar -zxf zeppelin-0.8.1-bin-all.tgz
$ ln -sf zeppelin-0.8.1-bin-all zeppelin
    ```
   
* Configure
    * zeppelin-site.xml
        * zeppelin.server.port : 서버 포트 (default : 8080)
    * .bashrc
        * JAVA_HOME, ZEPPELIN_HOME 추가
    * shiro.ini
        * 로그인 관리
        ```conf
        [users]
        admin = password!, admin
        user1 = password_user1, role1, role2
        user2 = password_user2, role2
        ```
        
    
* Nginx
    * proxy 사용시 유의사항
        * 한서버에 여러 web이 올라가다보니 domain 이름별로 proxy pass를 사용함.
        * zeppelin도 동일 방법으로 처리시 /ws에서 문제 발생.
        * /선언 위에 /ws에도 따로 선언 필요!
        ```conf
        server {
            listen       80;
            server_name  zeppelin.test.com;

            access_log  /log/nginx/zeppelin.test.com.access.log  main;
            error_log   /log/nginx/zeppelin.test.com.error.log;

            location /ws {  # For websocket support
            proxy_pass http://127.0.0.1:8282/ws;
            proxy_http_version 1.1;
            proxy_set_header Upgrade websocket;
            proxy_set_header Connection upgrade;
            proxy_read_timeout 86400;
            }

            location / {
                proxy_redirect     off;
                proxy_set_header   Host             $host;
                proxy_set_header   X-Real-IP        $remote_addr;
                proxy_set_header   X-Forwarded-For  $proxy_add_x_forwarded_for;
                proxy_buffers      32 4k;

                # Keepalive
                #proxy_http_version 1.1;
                #proxy_set_header Connection "";
                #proxy_pass http://keepalive_8082;

                proxy_pass http://127.0.0.1:8282;
            }
        }
        ```

* 실행
    * `./bin/zeppelin-daemon.sh start `

