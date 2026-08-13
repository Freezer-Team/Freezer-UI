# Freezer UI

为用户安全着想，FileInterface的所有方法只能在/data/system_de/0/Freezer下执行操作，对于此目录之外的所有操作均将返回false
(readLargeString和ls返回emptyList，readString返回null)
