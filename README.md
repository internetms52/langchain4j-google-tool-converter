# Object Pool Library

Object Pool 是一個Java Library ，用於協助管理並建立Java物件的生命週期

## 特性
- 以Object Pool物件為主要生命週期，當Object Pool 進到回收，其管理的物件也一併回收
- 從Object Pool中取得物件時，若不存在管理中會嘗試創建物件
- 支持巢狀的物件創建行為，並且巢狀的物件自動創建也做的到
- 不允許循環創建物件本身，也不允許已創立的物件被覆蓋，會發生Exception

[Document](/maven-lib/README.md)

