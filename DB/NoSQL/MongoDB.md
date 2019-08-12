# MongoDB

## Install Single Machine In CentOS
* yum repository 추가 ([Community Edition Version Check](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-red-hat/))
    ```shell
    vi /etc/yum.repos.d/mongodb-org-4.0.repo
    
    # append
    [mongodb-org-4.0]
    name=MongoDB Repository
    baseurl=https://repo.mongodb.org/yum/redhat/$releasever/mongodb-org/4.0/x86_64/
    gpgcheck=1
    enabled=1
    gpgkey=https://www.mongodb.org/static/pgp/server-4.0.asc
    ```
* Install
    ```shell
    sudo yum install -y mongodb-org
    ```
* Configuration
    * data directory
        * default : /var/lib/mongo
    * log directory
        * default : /var/log/mongodb
    * port
        * default : 27017
    * bindIp
        * default : 127.0.0.1
* Mkdir Directory 
```shell
mkdir -p /var/lib/mongo
chown -R mongod:mongod /var/lib/mongo
mkdir -p /var/log/mongodb
chown -R mongod:mongod /var/log/mongodb
```
* 시작
```shell
sudo service mongod start
```
* 종료
```shell
sudo service mongod stop
```