## 1. 检出代码

```java
git clone -b tag-v2.0 https://gitcode.net/KnowledgePlanet/s-pay-mall-ddd-market.git
git clone -b tag-v2.0 https://gitcode.net/KnowledgePlanet/group-buy-market.git
```

## 2. 更新代码

```java
git pull origin tag-v2.0
```

## 2. 打包代码

```java
mvn clean install
```

- 进入进入两个项目进行构建

## 3. 构建镜像

```java
cd s-pay-mall-ddd-market-app
chmod +x build.sh
```

```java
cd group-buy-market-app
chmod +x build.sh
```