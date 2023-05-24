## gremlin
### 연습 환경 설정
gremlin server
```shell
docker run -p 8182:8182 --name gremlin -d tinkerpop/gremlin-server 
```
gremlin console
```shell
docker run -d -it --name gremlin-console tinkerpop/gremlin-console        
```
```shell
docker exec -it gremlin-console /bin/bash
```
접속 후 
```shell
vim ./conf/remote.yml
```
`hosts: [localhost]` -> `hosts: gremlin` 변경 후 `./bin/gremlin.sh`로 실행