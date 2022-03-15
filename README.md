# model
- ## 1.user
    | 字段 | 类型 | 其他 |
    |----|----|----|
    |id|int; not null; primary_key; AUTO_INCREMENT|主键|
    |created_at|datetime; not null|创建时间|
    |updated_at|datetime; not null|更新时间|
    |deleted_at|datetime;         |删除时间, 索引, 可空 sql查询语句: deleted_at is NULL|
    |user_name|varchar(25); not null|用户名, 注册时随机一个用户名|
    |name|varchar(20); not null|姓名|
    |password|varchar(255); not null|密码, 加盐后的MD5|
    |email|varchar(50)|邮箱|
    |phone|varchar(50); not null|手机号, 登录时的账号, 不允许重复|
    |id_card|varchar(50); not null|身份证号|
    |balance|decimal; | 余额,钱包余额  CNY|
    |age|datetime; not null|年龄, 保存出生日期|
    |employment_status|int; not null|就业情况, 0 无业/失业/未完善信息, 1 就业,|
    |credit_status|int; not null| 失信状态 0失信,1未失信 |
- ## 2.recharge_record(充值记录)
    | 字段 | 类型 | 其他 |
    |----|----|----|
    |id|int; not null; primary_key; AUTO_INCREMENT|主键|
    |created_at|datetime; not null|创建时间|
    |updated_at|datetime; not null|更新时间|
    |deleted_at|datetime;         |删除时间, 索引, 可空 sql查询语句: deleted_at is NULL|
    |user_id|int; not null; ForeignKey_key;|外键|
    |recharge_amount|decimal;not null|充值金额 CNY|
    |recharge_method|int; not null|充值方式, 1 支付宝 2 微信 3 xxx ....|
- ## 3.overdue_records(逾期记录)
    | 字段 | 类型 | 其他 |
    |----|----|----|
    |id|int; not null; primary_key; AUTO_INCREMENT|主键|
    |created_at|datetime; not null|创建时间|
    |updated_at|datetime; not null|更新时间|
    |deleted_at|datetime;         |删除时间, 索引, 可空 sql查询语句: deleted_at is NULL|
    |user_id|int; not null; ForeignKey_key;|外键|
    |overdue_amount|decimal;not null|逾期金额|
    |repayment_status|decimal;not null|0已还清, xxx剩余xxx元|
- ## 4.filter_users(其他数据筛选, 年龄 ,就业情况 ,失信状态)
    | 字段 | 类型 | 其他 |
    |----|----|----|
    |id|int; not null; primary_key; AUTO_INCREMENT|主键|
    |created_at|datetime; not null|创建时间|
    |updated_at|datetime; not null|更新时间|
    |deleted_at|datetime;         |删除时间, 索引, 可空 sql查询语句: deleted_at is NULL|
    |user_id|int; not null; ForeignKey_key;|外键|
    |age|datetime; not null|年龄, 保存出生日期|
    |employment_status|int; not null|就业情况, 0 无业/失业/未完善信息, 1 就业,|
    |credit_status|int; not null| 失信状态 0失信,1未失信 |
- ## 5.items
    |字段|类型|其他|
    |----|----|----|
    |id|int; not null; primary_key; AUTO_INCREMENT|主键|
    |created_at|datetime; not null|创建时间|
    |updated_at|datetime; not null|更新时间|
    |deleted_at|datetime;         |删除时间, 索引, 可空 sql查询语句: deleted_at is NULL|
    |title|varchar(50); not null|商品标题|
    |stock|bigint; not null|总库存|
    |amount|bigint; not null|单价|
    |description|varchar(255);not null|商品描述|
    |start_time|datetime;not null|开始时间|
    |end_time|datetime;not null|结束时间|
    |remaining_stock|bigint; not null|剩余库存|
- ## 6.manager_user
    |字段|类型|其他|
    |----|----|----|
    |id|int; not null; primary_key; AUTO_INCREMENT|主键|
    |created_at|datetime; not null|创建时间|
    |updated_at|datetime; not null|更新时间|
    |deleted_at|datetime;         |删除时间, 索引, 可空 sql查询语句: deleted_at is NULL|
    |account|varchar(50); not null|账号|
    |password|varchar(50);not null|密码, 加盐MD5|
    |product_permissions|int; not null|商品权限 0不允许,1查,2增删改查|
    |seckill_record_permissions|int; not null|秒杀记录权限,0不允许,1可查|
    |recharge_record_permissions|int;not null|充值记录权限,0不允许,1可查|
    |add_admin_rights|int; not null|新增管理员权限,0不允许,1可添加新管理员|
    |....|||
- ## 7.seckill_record 用户秒杀记录
    |字段|类型|其他|
    |----|----|----|
    |id|int; not null; primary_key; AUTO_INCREMENT|主键|
    |created_at|datetime; not null|创建时间|
    |updated_at|datetime; not null|更新时间|
    |deleted_at|datetime;         |删除时间, 索引, 可空 sql查询语句: deleted_at is NULL|
    |user_id|int; not null; ForeignKey_key;|外键|
    |item_id|int; not null; ForeignKey_key;|外键|
    |seckill_status|int; not null|秒杀状态,0 失败,1成功|
    |error_message|varchar(255)|错误信息, 如果秒杀失败,如未满岁数,有逾期记录,库存不足等|
- ## 8.后台操作记录
    |字段|类型|其他|
    |----|----|----|
    |id|int; not null; primary_key; AUTO_INCREMENT|主键|
    |created_at|datetime; not null|创建时间|
    |updated_at|datetime; not null|更新时间|
    |deleted_at|datetime;         |删除时间, 索引, 可空 sql查询语句: deleted_at is NULL|
    |manager_user_id|int; not null; ForeignKey_key;|外键|
    |ip|varchar(50);not null|操作ip|
    |operate|varchar(255); not null|操作,0登录,1增,2删,3改,如果是登录操作,只填0,其他需提现出什么操作,操作了哪个表,影响到的id及是否成功,如1,manager_user,123,1代表成功新增了一个管理员,新增的id为123|

# 目前计划好的接口
## 1.guest
- ### api(返回数据没有出现的字段自由发挥)
    | 方式 | url | 前端数据 | 返回数据 | 注意事项 |
    |----|----|----|----|----|
    |POST|/api/auth/login|{phone:"xxxx",password:"xxxx"}|登录成功{"status":true,data:{token:"xxx"}},失败{"status":false,data:null}}|后续前端传回密码可能是加密过的,暂时按前端返回明文处理, 需要实现一个账号只能同时在一个客户端登录(被挤下线), 修改密码后需重新登录, 可能需要redis|
    |POST|/api/auth/register|{name:"姓名",password:"密码",phone:"手机号",id_card:"身份证号"}|注册成功{data:{token:"token"}}|后续前端传回密码可能是加密过的,暂时按前端返回明文处理,一个手机号只允许注册一个账号|
    |GET|/api/auth/check_version|无|登录有效{status:true,message:"查询成功"},登录无效{status:false,message:"登录失效"}||
    |POST|/api/auth/log_out|无|注销成功{status:true,message:"退出成功"},注销失败{status:false,message:"错误信息"}|token无效,token过期归为注销成功, 如果token有效, 退出后需实现此token无法再进行相关用户操作(token加入黑名单等,加入黑名单失败返回注销失败)|
    |GET|获取商品列表,url自行设计|{order:xxx}(1全部, 2未开始,3进行中,4已结束),|数组{title:"title",amount:xxxx,id:xxx,startTime:"YYYY-MM-DD hh:mm:ss",endTime:"YYYY-MM-DD hh:mm:ss"}|秒杀总览页, 只显示标题和金额,其他数据不予显示,分页查询,order:1所有项目按开始时间排序,2返回未开始的项目, 即将开始的优先排序,3按结束时间,即将结束的优先,4按结束时间排序|
    |POST|获取具体商品数据,url自行设计|{id:xxxx}|{title:"title",amount:xxxx,id:xxx,startTime:"YYYY-MM-DD hh:mm:ss",endTime:"YYYY-MM-DD hh:mm:ss",remainingStock:xxx,description:"xxxx"}|通过后通过id返回具体数据|
## 2.manager(如果api前缀冲突执行协商修改前缀.个别接口需要鉴别权限)
- ### api(返回数据没有出现的字段自由发挥)
    | 方式 | url | 前端数据 | 返回数据 | 注意事项 |
    |----|----|----|----|----|
    |POST|/manager/auth/login|{account:"xxxx",password:"xxxx"}|登录成功{"status":true,data:{token:"xxx",productPermissions:x,seckillRecordPermissions:x,rechargeRecordPermissions:x,addAdminRights:X}},失败{"status":false,data:null}}|后续前端传回密码可能是加密过的,暂时按前端返回明文处理, 登录成功留下一次后台记录(需记录ip), 一个账号允许多人同时登录, 修改密码后需重新登录|
    |GET|/manager/auth/check_version|无|登录有效{status:true,message:"查询成功"},登录无效{status:false,message:"登录失效"}||
    |POST|/manager/auth/log_out|无|注销成功{status:true,message:"退出成功"},注销失败{status:false,message:"错误信息"}|token无效,token过期归为注销成功, 如果token有效, 退出后需实现此token无法再进行相关用户操作(token加入黑名单等,加入黑名单失败返回注销失败)|
    |POST|/manager/item/get_item|{order:xxx}(1全部1, 2未开始,3进行中,4已结束,5全部2)|{title:"title",amount:xxxx,id:xxx,startTime:"YYYY-MM-DD hh:mm:ss",endTime:"YYYY-MM-DD hh:mm:ss",remainingStock:xxx,description:"xxxx",stock:xxxx,createdTime:"YYYY-MM-DD hh:mm:ss",updatedTime:"YYYY-MM-DD hh:mm:ss"}|产品管理, 分页查询,order:1所有项目按开始时间排序,2返回未开始的项目, 即将开始的优先排序,3按结束时间,即将结束的优先,4按结束时间排序,5按创建时间(id)排序|
    |POST|/manager/item/add_item|{title:"title",amount:xxxx,startTime:datetime,endTime:datetime,description:"xxxx",stock:xxxx}|{message:"成功or失败(失败信息)"}|库存&amount1~9位,剩余库存=总库存,开始时间与现在时间应大于两小时|
    |POST|修改商品信息,url自行设计|{title:"title",amount:xxxx,startTime:datetime,endTime:datetime,description:"xxxx",stock:xxxx}|{message:"成功or失败(失败信息)"}|库存&amount1~9位,剩余库存=总库存,新开始时间与现在时间应大于两小时, 距原开始时间小于两小时,已开始,已结束的商品不允许修改|
    