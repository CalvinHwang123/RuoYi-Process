### 一. 超级懒人版

只需要执行 xianlu_proc_last.sql 即可。

### 二. 强迫症患者版

1. 删除 act_xxx 和 biz_xxx （如果没数错一共是 29 张表）

2. admin 账号登录后，寻找菜单：流程管理 / 流程用户 和 流程用户组，配置成如下效果：

> 无流程用户组：chengxy 程序猿
>
> 部门领导：axianlu 一只闲鹿
>
> 人事：rensm 人事喵

3. 寻找菜单：流程管理  / 流程定义 / 部署流程定义，上传的文件在 RuoYi/bpmn 文件夹内（即 leave.bpmn 和 leave-countersign.bpmn）

4. 然后就可以以 chengxy 的账号提交 `普通请假` 和 `请假会签` 的流程 demo 了。