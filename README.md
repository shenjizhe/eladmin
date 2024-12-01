# 代码工厂服务
## 重要概念

1. Redis docker-compose，复制到 /usr/local/docker/redis/docker-compose.yml
```
version: '3'
services:
  redis:
    image: redis:4.0.13
    container_name: redis_test
    restart: always
    command: --appendonly yes
    ports:
      - 6389:6379
    volumes:
      - ./redis_data:/data

```
2. mysql docker-compose,复制到 /usr/local/docker/mysql/docker-compose.yml
```

version: '3.1'
services:
  db:
    image: mysql
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: 123456
    command:
     --default-authentication-plugin=mysql_native_password
     --character-set-server=utf8mb4
     --collation-server=utf8mb4_general_ci
     --explicit_defaults_for_timestamp=true
     --lower_case_table_names=1
    ports:
     - 3306:3306
    volumes:
     - ./data:/var/lib/mysql
  adminer:
    image: adminer
    restart: always
    ports:
     - 8081:8080

```
3. 导入sql的脚本
```
在 sql目录下，eladmin.sql

```
4. 下载编译包 codefactory 和 eladmin,(如果不使用本地，需要就下载eladmin-web的运行目录)
5. 脚本启动
eladmin-all 
   1. 复制程序和脚本到 /usr/local/services/目录下
   2. 新建子目录，codefactory/ 和 eladmin/，修改 同名文件的目录权限 chmod +x 
   3. 修改好运行的目录(如果没有更改目录，则无需修改)
   4. 复制 shell/eladmin-all 到，/usr/local/services/目录下
   5. chmod 脚本权限


6. 运行 ./eladmin-all start
7. 然后运行 eladmin-web的 vue服务  ( npm run dev ),获取运行 nom run build:prod 然后复制到 tomcat运行目录